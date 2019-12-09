package com.changgouseckill.web.controller;

import com.changgou.entity.Result;
import com.changgou.seckill.feign.SeckillGoodsFeign;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/wseckillgoods")
public class SecKillGoodsController {

    // 跳转秒杀首页
    @RequestMapping("/toIndex")
    public String toIndex() {
        return "seckill-index";
    }

    // 获取秒杀字段的集合信息
    @RequestMapping("/timeMenus")
    @ResponseBody
    public List<String> dateMenus() {
        // 获取时间结合
        List<Date> dateMenus = DateUtil.getDateMenus();
        List<String> result = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Date dateMenu : dateMenus) {
            String format = simpleDateFormat.format(dateMenu);
            result.add(format);
        }
        return result;
    }

    @Autowired
    private SeckillGoodsFeign seckillGoodsFeign;

     // 获取秒杀商品列表

    @RequestMapping("/list")
    @ResponseBody
    public Result<List<SeckillGoods>> list(String time){
        Result<List<SeckillGoods>> listResult = seckillGoodsFeign.list(DateUtil.formatStr(time));
        return listResult;
    }


}
