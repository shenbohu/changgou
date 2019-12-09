package com.changgouseckill.web.aspect;

import com.alibaba.fastjson.JSON;
import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Scope
@Aspect
public class AccessLimitAop {
    @Autowired
    private  HttpServletResponse response;
    // 设置令牌的生成树率
    private RateLimiter rateLimiter = RateLimiter.create(20.0); //每秒20个令牌存入桶中

    // 对自定义注解AccessLimit注解进行增强
    @Pointcut("@annotation(com.changgouseckill.web.aspect.AccessLimit)")
    public void limit() {
    }

    // 环绕增强
    @Around("limit()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) {


        boolean flag = rateLimiter.tryAcquire(); //判断能不能通过
        Object obj = null;
        if (flag) {
            // 允许访问
            try {
                obj = proceedingJoinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } else {
            // 不允许访问
          String  errorMessage = JSON.toJSONString(new Result<>(false, StatusCode.ACCESSERROR, "fail"));
            // 将信息返回到客户端
            this.outMessage(response,errorMessage);
        }
        return obj;
    }

    private void outMessage(HttpServletResponse response, String errorMessage) {
        ServletOutputStream outputStream = null;
        try {
            response.setContentType("application/json;charset=UTF-8");
            outputStream = response.getOutputStream();
            outputStream.write(errorMessage.getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
