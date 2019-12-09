package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.dao.ESManagerMapper;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.ESManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;

@Service
public class ESManagerServiceImpl implements ESManagerService {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private SkuFeign skuFeign;
    @Autowired
    private ESManagerMapper esManagerMapper;

    // 创建索引库结构
    @Override
    public void createIndexAndMapping() {
        // 创建索引创建映射
        elasticsearchTemplate.createIndex(SkuInfo.class);
        elasticsearchTemplate.putMapping(SkuInfo.class);
    }

    //导入全部数据进入es
    @Override
    public void importAll() {
        // 查询sku集合
        List<Sku> skuList = skuFeign.findListBySpuId("all");
        if (skuList == null || skuList.size() <= 0) {
            throw new RuntimeException("没有数据");
        }
        // 转化为json
        String jsonskuList = JSON.toJSONString(skuList);
        //转换为skuinf
        List<SkuInfo> skuInfos = JSON.parseArray(jsonskuList, SkuInfo.class);
        for (SkuInfo skuInfo : skuInfos) {
            // 将规格转换为map
            //String spec = skuInfo.getSpec();
            Map map = JSON.parseObject(skuInfo.getSpec(), Map.class);
            skuInfo.setSpecMap(map);
        }

        esManagerMapper.saveAll(skuInfos);

    }

    //根据spuid删除数据
    @Override
    public void delSkuListBySpuId(String spuId) {
        List<Sku> listBySpuId = skuFeign.findListBySpuId(spuId);
        if (listBySpuId != null) {
            for (Sku sku : listBySpuId) {
                esManagerMapper.deleteById(Long.parseLong(sku.getId()));
            }
        }

    }

    //根据spuid查询skulist在导入索引库
    @Override
    public void importDataToESBySpuId(String spuId) {
        List<Sku> skuList = skuFeign.findListBySpuId(spuId);
        if (skuList == null || skuList.size() <= 0) {
            throw new RuntimeException("没有数据");
        }
        String jsonString = JSON.toJSONString(skuList);
        List<SkuInfo> skuInfos = JSON.parseArray(jsonString, SkuInfo.class);
        for (SkuInfo skuInfo : skuInfos) {
            Map map = JSON.parseObject(skuInfo.getSpec(), Map.class);
            skuInfo.setSpecMap(map);
        }
        esManagerMapper.saveAll(skuInfos);
    }
}
