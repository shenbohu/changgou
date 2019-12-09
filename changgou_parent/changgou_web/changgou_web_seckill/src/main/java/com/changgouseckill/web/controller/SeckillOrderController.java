package com.changgouseckill.web.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.seckill.feign.SeckillOrderFeign;
import com.changgou.util.RandomUtil;
import com.changgouseckill.web.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/wseckillorder")
public class SeckillOrderController {

    @Autowired
    private SeckillOrderFeign seckillOrderFeign;
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/add")
    public Result and(@RequestParam("time") String time, @RequestParam("id") Long id , String random)  {
        String cookieValue = this.readCookie();
        //校验密文有效
//        String randomcode = (String) redisTemplate.boundValueOps("randomcode").get();
        String randomcode = (String) redisTemplate.opsForValue().get("randomcode_" + cookieValue);

        if (StringUtils.isEmpty(randomcode) || !random.equals(randomcode)){
            return new Result(false, StatusCode.ERROR,"无效访问");
        }
        Result add = seckillOrderFeign.add(time, id);
        return add;
    }


    //接口加密
    // 生成随机数存入redis，10秒有效期

    @GetMapping("/getToken")
    @ResponseBody
    public String getToken() {
        String randomString = RandomUtil.getRandomString();

        String cookieValue = this.readCookie();
        redisTemplate.boundValueOps("randomcode_" + cookieValue).set(randomString, 10, TimeUnit.SECONDS);
        return randomString;
    }

    //读取cookie
    private String readCookie() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String cookieValue = CookieUtil.readCookie(request, "uid").get("uid");
        return cookieValue;
    }
}
