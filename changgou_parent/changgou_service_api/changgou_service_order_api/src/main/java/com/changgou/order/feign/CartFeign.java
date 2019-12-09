package com.changgou.order.feign;

import com.changgou.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

// 调用哪一个服务
@FeignClient("order")
@RequestMapping("cart")
public interface CartFeign {
    @GetMapping("addCart")
    public Result addCart(@RequestParam("skuId") String skuId, @RequestParam("num") Integer num);


    @GetMapping("/list")
    public Map list();
}
