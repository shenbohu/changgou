package com.changgou.order.feign;

import com.changgou.entity.Result;
import com.changgou.order.pojo.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "order") //服务的名称
@RequestMapping("/order")
public interface OrderFeign {
    @PostMapping
    public Result add(@RequestBody Order order);

    @GetMapping("/{id}")
    public Result findById(@PathVariable String id);
}
