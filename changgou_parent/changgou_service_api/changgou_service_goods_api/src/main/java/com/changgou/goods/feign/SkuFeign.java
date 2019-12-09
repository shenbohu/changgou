package com.changgou.goods.feign;

import com.changgou.entity.Result;
import com.changgou.goods.pojo.Sku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="goods")
@RequestMapping("/sku")
public interface SkuFeign {

    /***
     * 多条件搜索品牌数据
     * @param spuId
     * @return
     */
    @GetMapping(value = "/spu/{spuId}" )
    public List<Sku> findListBySpuId(@PathVariable("spuId") String spuId);

    @GetMapping("/{id}")
    public Result<Sku> findById(@PathVariable("id") String id);

    // 减少库存 家销量
    @PostMapping("/decer/count")
    public Result decerCount(@RequestParam("username") String username);
    //回滚
    @RequestMapping("/resumeStockNum")
    public Result resumeStockNum(@RequestParam("skuId") String skuId , @RequestParam("num") Integer num);
}