package com.changgou.goods.service;

import com.changgou.goods.pojo.Goods;
import com.changgou.goods.pojo.Spu;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface SpuService {

    /***
     * 查询所有
     * @return
     */
    List<Spu> findAll();

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    Spu findById(String id);

    /***
     * 新增
     * @param goods
     */
    void add(Goods goods);

    /***
     * 修改
     * @param spu
     */
    void update(Spu spu);

    /***
     * 删除
     * @param id
     */
    void delete(String id);

    /***
     * 多条件搜索
     * @param searchMap
     * @return
     */
    List<Spu> findList(Map<String, Object> searchMap);

    /***
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    Page<Spu> findPage(int page, int size);

    /***
     * 多条件分页查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    Page<Spu> findPage(Map<String, Object> searchMap, int page, int size);


    /**
     * 根据ID查询商品
     *
     * @param id
     * @return
     */

    Goods findGoodsById(String id);

    /***
     * 修改数据
     * @param goods
     */
    void update(Goods goods);

    /**
     * 审核
     *
     * @param id
     */
    public void audit(String id);

    //    下架商品
    public void pull(String id);

    //    shang架商品
    public void put(String id);

    /**
     * 恢复数据
     * @param id
     */
    public void restore(String id);

    /**
     * 物理删除
     * @param id
     */
    public void realDelete(String id);
}
