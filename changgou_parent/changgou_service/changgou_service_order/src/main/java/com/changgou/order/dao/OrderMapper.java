package com.changgou.order.dao;

import com.changgou.order.pojo.Order;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface OrderMapper extends Mapper<Order> {

    public String order_status(String username);
}
