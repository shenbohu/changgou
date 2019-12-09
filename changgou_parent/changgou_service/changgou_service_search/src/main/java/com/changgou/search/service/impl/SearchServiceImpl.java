package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.search.config.RabbitMQConfig;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SearchService;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private RabbitMQConfig rabbitMQConfig;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public Map search(Map<String, String> paramMap) {
        Map<String, Object> resultMap = new HashMap<>();
        if (null != paramMap) {
            // 构建查询条件
            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();


            //按照关键字查询  isNotEmpty  不为空
            if (StringUtils.isNotEmpty(paramMap.get("keywords"))) {
                //matchQuery 模糊查询  operator 条件的拼接 and &&
                boolQuery.must(QueryBuilders.matchQuery("name", paramMap.get("keywords")).operator(Operator.AND));
            }
            //品牌 termQuery 精确查询
            if (StringUtils.isNotEmpty(paramMap.get("brand"))) {
                boolQuery.filter(QueryBuilders.termQuery("brandName", paramMap.get("brand")));
            }

            //2:条件 规格   startsWith 已spec 开始
            for (String key : paramMap.keySet()) {
                if (key.startsWith("spec_")) {   //replace 转换
                    String value = paramMap.get(key).replace("%2B", "+");     //keyword 不变
                    boolQuery.filter(QueryBuilders.termQuery("specMap." + key.substring(5) + ".keyword", value));
                }
            }

            //3:条件 价格
            if (StringUtils.isNotEmpty(paramMap.get("price"))) {
                String[] p = paramMap.get("price").split("-");
                if (p.length == 2) {   //rangeQuery 区间过滤范围查询   lte 小于等于
                    boolQuery.filter(QueryBuilders.rangeQuery("price").lte(p[1]));
                }//gte >=
                boolQuery.filter(QueryBuilders.rangeQuery("price").gte(p[0]));
            }


            nativeSearchQueryBuilder.withQuery(boolQuery);


            //5:高亮
            HighlightBuilder.Field field = new HighlightBuilder
                    .Field("name")
                    .preTags("<span style='color:red'>")
                    .postTags("</span>");
            nativeSearchQueryBuilder.withHighlightFields(field);

            //6. 品牌聚合(分组)查询   addAggregation 添加聚合   terms  结果列明     field  按哪一个列分组
            String skuBrand = "skuBrand";
            nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(skuBrand).field("brandName"));

            //7. 规格聚合(分组)查询
            String skuSpec = "skuSpec";
            nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(skuSpec).field("spec.keyword"));


            //8: 排序
            if (!StringUtils.isEmpty(paramMap.get("sortField"))) {
                if ("ASC".equals(paramMap.get("sortRule"))) {
                    nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(paramMap.get("sortField")).order(SortOrder.ASC));
                } else {

                    nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(paramMap.get("sortField")).order(SortOrder.DESC));
                }

            }

            String pageNum = paramMap.get("pageNum");
            if (null == pageNum) {
                pageNum = "1";
            }
            String pageSize = paramMap.get("pageSize");
            if (null == pageSize) {
                pageSize = "30";
            }
            //9: 分页
            nativeSearchQueryBuilder.withPageable(PageRequest.of(Integer.parseInt(pageNum) - 1, Integer.parseInt(pageSize)));

            AggregatedPage<SkuInfo> resultInfo = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), SkuInfo.class, new SearchResultMapper() {
                @Override
                ////参数: 1:条件构建对象 2:实体类  3:new SearchResultMapper
                public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                    //查询结果的操作
                    List<T> list = new ArrayList<>();


                    // 获取查询的结果数据  SearchHits 结果集合
                    SearchHits hits = searchResponse.getHits();
                    if (hits != null) {
                        //SearchHit  每一条记录
                        for (SearchHit hit : hits) {
                            // 将SearchHit 转换为skuinf
                            SkuInfo skuInfo = JSON.parseObject(hit.getSourceAsString(), SkuInfo.class);
                            // 得到所有的高亮域
                            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                            if (null != highlightFields && highlightFields.size() > 0) {
                                //替换  getFragments 获取内容 0  第一个值
                                skuInfo.setName(highlightFields.get("name").getFragments()[0].toString());
                            }
                            list.add((T) skuInfo);

                        }
                    }           // 数据集合   分页对象    总记录数
                    return new AggregatedPageImpl<T>(list, pageable, hits.getTotalHits(), searchResponse.getAggregations());
                }
            });

            // 封装返回结果
            resultMap.put("total", resultInfo.getTotalElements());  //总记录数
            resultMap.put("totalPage", resultInfo.getTotalPages());  //总也数
            resultMap.put("rows", resultInfo.getContent());  //数据


            //14.getAggregation 获取品牌聚合结果
            StringTerms brandTerms = (StringTerms) resultInfo.getAggregation(skuBrand);
            // 流运算  getBuckets 得到相对应的结果  stream 开启流运算  getKeyAsString 每一个品牌   collect 转换为集合
            List<String> brandList = brandTerms.getBuckets().stream().map(bucket -> bucket.getKeyAsString()).collect(Collectors.toList());
            resultMap.put("brandList", brandList);


            //15. 获取规格聚合结果
            StringTerms specTerms = (StringTerms) resultInfo.getAggregation(skuSpec);
            List<String> specList = specTerms.getBuckets().stream().map(bucket -> bucket.getKeyAsString()).collect(Collectors.toList());
            resultMap.put("specList", this.specList(specList));

            // 当前页
            resultMap.put("pageNum", pageNum);

            return resultMap;
        }
        return null;
    }

    //处理规格集合
    public Map<String, Set<String>> specList(List<String> specList) {
        Map<String,Set<String>>  resultMap = new HashMap<>();
        if(specList!=null && specList.size()>0) {
            for (String spec : specList) {
                Map<String,String> specmap = JSON.parseObject(spec, Map.class);
                for (String s : specmap.keySet()) {
                    Set<String> specSet = resultMap.get(s);
                    if(specSet==null) {
                        specSet = new HashSet<>();
                    }
                    specSet.add(specmap.get(s));
                    resultMap.put(s,specSet);
                }

            }
        }
       return resultMap;
    }
}