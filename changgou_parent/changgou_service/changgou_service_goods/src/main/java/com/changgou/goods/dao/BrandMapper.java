package com.changgou.goods.dao;

import com.changgou.goods.pojo.Brand;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface BrandMapper extends Mapper<Brand> {

    // 根据分类名称查询品牌列表
    @Select("select  b.*  FROM tb_category c ,tb_category_brand tcb ,tb_brand b where c.id=tcb.category_id and tcb.brand_id = b.id and c.`name`=#{name}")
    public List<Brand> findListByCategoryName(@Param("name") String name);



}
