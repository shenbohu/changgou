package com.changgou.search.service;

public interface ESManagerService {
    // 创建索引库结构
    public void createIndexAndMapping();

    //导入全部数据进入es
    public void importAll();

    //根据spuid查询skulist在导入索引库
    public void importDataToESBySpuId(String spuId);

    //根据spuid删除数据

    void delSkuListBySpuId(String spuId);
}
