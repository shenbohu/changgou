package com.changgou.order.feign;

import com.changgou.entity.Result;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "order") //服务的名称
@RequestMapping("/center")
public interface CenterFeign {
    @GetMapping("/OrderItemlist")
    public Result<List<OrderItem>> list();
}
