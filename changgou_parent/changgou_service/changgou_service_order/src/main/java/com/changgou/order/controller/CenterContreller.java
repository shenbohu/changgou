package com.changgou.order.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.order.config.TokenDecode;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/center")
@RestController
public class CenterContreller {
    @Autowired
    private CenterService centerService;
    @Autowired
    private TokenDecode tokenDecode;

    @GetMapping("/OrderItemlist")
    public Result<List<OrderItem>> list() {
        // 获取用户的信息
        String username = tokenDecode.getUserInfo().get("username");
        List<OrderItem> list = centerService.orderList(username);
        return new Result (true, StatusCode.OK, "成功", list);
    }

}
