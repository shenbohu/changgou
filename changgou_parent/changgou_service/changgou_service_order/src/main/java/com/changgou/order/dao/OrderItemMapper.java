package com.changgou.order.dao;

import com.changgou.order.pojo.OrderItem;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OrderItemMapper extends Mapper<OrderItem> {
    @Select("select * from tb_order_item where order_id in(SELECT tb_order.id FROM `tb_order` WHERE username=#{username})")
    @Results({@Result(column = "id",property = "id"),
            @Result(column = "category_id1",property = "categoryId1"),
            @Result(column = "category_id2",property = "categoryId2"),
            @Result(column = "category_id3",property = "categoryId3"),
            @Result(column = "spu_id",property = "spuId"),
            @Result(column = "sku_id",property = "skuId"),
            @Result(column = "order_id",property = "orderId"),
            @Result(column = "name",property = "name"),
            @Result(column = "price",property = "price"),
            @Result(column = "num",property = "num"),
            @Result(column = "money",property = "money"),
            @Result(column = "pay_money",property = "payMoney"),
            @Result(column = "image",property = "image"),
            @Result(column = "weight",property = "weight"),
            @Result(column = "post_fee",property = "postFee"),
            @Result(column = "is_return",property = "isReturn")

    })
    List<OrderItem> orderList(String username);
}
