package com.changgou.goods.dao;

import com.changgou.goods.pojo.Sku;
import com.changgou.order.pojo.OrderItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface SkuMapper extends Mapper<Sku> {
    // 扣减库存增加向量
    @Update("update tb_sku set num = num - #{num}, sale_num = sale_num+#{num} where id = #{skuId} and num>=#{num}")
    int decerCount(OrderItem orderItem);

    //库存回滚
    @Update("update tb_sku set num = num + #{num}, sale_num = sale_num-#{num} where id = #{skuId} ")
    void resumeStockNum(@Param("skuId") String skuId, Integer num);
}
