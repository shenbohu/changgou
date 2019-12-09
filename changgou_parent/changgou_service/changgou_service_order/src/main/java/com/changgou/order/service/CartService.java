package com.changgou.order.service;

import java.util.List;
import java.util.Map;

public interface CartService {
    // 添加购物车
    void addCart(String skuid,Integer num ,String username);
    // 查询购物车数据
      Map List(String username);
}
