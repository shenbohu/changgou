package com.changgou.search.test;

import com.alibaba.fastjson.JSON;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SearchService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//@Service
public class SearchSerciceImplTest implements SearchService {
    Map<String, Object> resultMap = new HashMap<>();
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    private static final String PageNum = "1";
    private static final String pageSize = "30";

    @Override
    public Map search(Map<String, String> paramMap) {
        if (paramMap!=null) {
            // 构建查询条件
            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            // 按照关键字查询
            if(!paramMap.get("keywords").isEmpty()) {
                boolQuery.must(QueryBuilders.matchQuery("name",paramMap.get("keywords")).operator(Operator.AND));
            }


            nativeSearchQueryBuilder.withQuery(boolQuery);
            AggregatedPage<SkuInfo> resultInfo = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), SkuInfo.class, new SearchResultMapper() {
                @Override
                public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                    //查询结果的操作
                    List<T> list = new ArrayList<>();
                    SearchHits hits = searchResponse.getHits();
                    if (hits != null) {
                        for (SearchHit hit : hits) {
                            SkuInfo skuInfo = JSON.parseObject(hit.getSourceAsString(), SkuInfo.class);
                            list.add((T) skuInfo);
                        }

                    }
                    return new AggregatedPageImpl<>(list,pageable,hits.totalHits,searchResponse.getAggregations());
                }
            });

            // 封装返回结果
            resultMap.put("total", resultInfo.getTotalElements());  //总记录数
            resultMap.put("totalPage", resultInfo.getTotalPages());  //总也数
            resultMap.put("rows", resultInfo.getContent());  //数据
            return resultMap;

        }
        return null;
    }
}
