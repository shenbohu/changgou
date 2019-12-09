package com.changgou.controller;

import com.alibaba.fastjson.JSON;
import com.changgou.entity.Result;
import com.changgou.order.feign.OrderFeign;
import com.changgou.order.pojo.Order;
import com.changgou.pay.feign.WxPayFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/wxpay")
public class PayController {
    @Autowired
    private OrderFeign orderFeign;
    @Autowired
    private WxPayFeign wxPayFeign;
    @GetMapping
    public String wxPay(String orderId, Model model) {
        //根据 id查询订单
        Result<Order> orderResult = orderFeign.findById(orderId);
        if (orderResult.getData()==null) {
            return "fail";
        }
        //根据订单支付状态  不是为支付 错误
        Map map = (Map) orderResult.getData();
        String jsonString = JSON.toJSONString(map);
        Order order = JSON.parseObject(jsonString, Order.class);
        if(!"0".equals(order.getPayStatus())) {
            return "fail";
        }
        Integer payMoney = order.getPayMoney();
        //调用 下单二维码接口
        Result payResult = wxPayFeign.nativePay(orderId, payMoney);
        Map payMap = (Map) payResult.getData();
        if(payMap==null) {
            return "fail";
        }
        //封装数据

        payMap.put( "payMoney", payMoney);
        payMap.put( "orderId" ,orderId );
        model.addAllAttributes( payMap );
        return "wxpay";
        }

        //支付成功页面的跳转
    @RequestMapping("/toPaySuccess")
    public String toPaySuccess(String payMoney,Model model){
        model.addAttribute("payMoney",payMoney);
        return "paysuccess";
    }
}
