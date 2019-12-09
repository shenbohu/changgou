package com.changgou.order.service;

import com.changgou.order.pojo.OrderItem;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CenterService {

    List<OrderItem> orderList(String username);

}
