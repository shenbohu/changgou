package com.changgou.controller;

import com.alibaba.fastjson.JSON;
import com.changgou.entity.Result;
import com.changgou.order.feign.CartFeign;
import com.changgou.order.feign.OrderFeign;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderItem;
import com.changgou.user.feign.AddresFeign;
import com.changgou.user.pojo.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/worder")
public class OrderController {
    @Autowired
    private AddresFeign addresFeign;
    @Autowired
    private CartFeign cartFeign;

    @RequestMapping("/reday/order")
    public String readyOrder(Model model) {
        List<Address> data = addresFeign.list().getData();
        model.addAttribute("address", data);

        Map map = cartFeign.list();
        List<OrderItem> orderItemList = (List<OrderItem>) map.get("orderItemList");
        Integer totalNum = (Integer) map.get("totalNum");
        Integer totalMoney = (Integer) map.get("totalMoney");

        model.addAttribute("totalNum", totalNum);
        model.addAttribute("totalMoney", totalMoney);
        model.addAttribute("carts", orderItemList);

        for (Address address : data) {
            if ("1".equals(address.getIsDefault())) {
                model.addAttribute("deAdder", address);
                break;
            }
        }
        return "order";
    }

    @Autowired
    private OrderFeign orderFeign;

    @PostMapping("/add")
    @ResponseBody
    public Result add(@RequestBody Order order) {

        Result add = orderFeign.add(order);
        return add;
    }

    @GetMapping("/toPayPage")
    public String toPayPage(String orderId, Model model){
        //获取订单的相关信息
        Result<Order> orderResult = orderFeign.findById(orderId);
        Map map = (Map) orderResult.getData();
        String jsonString = JSON.toJSONString(map);
        Order order = JSON.parseObject(jsonString, Order.class);
        model.addAttribute("orderId",orderId);
        model.addAttribute("payMoney",order.getPayMoney());
        return "pay";
    }
}
