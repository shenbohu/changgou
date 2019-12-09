package com.changgou.order.service.impl;

import com.changgou.order.dao.OrderItemMapper;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CenterServiceImpl implements CenterService {
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Override
    public List<OrderItem> orderList(String username) {
        List<OrderItem> orderItemList = orderItemMapper.orderList(username);
        return orderItemList;
    }
}
