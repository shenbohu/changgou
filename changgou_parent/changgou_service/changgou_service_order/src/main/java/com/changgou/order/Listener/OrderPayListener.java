package com.changgou.order.Listener;

import com.alibaba.fastjson.JSON;
import com.changgou.order.config.RabbitMQConfig;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.Task;
import com.changgou.order.service.OrderService;
import com.changgou.order.service.TaskService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OrderPayListener {
    @Autowired
    private OrderService orderService;

    @RabbitListener(queues = RabbitMQConfig.ORDER_PAY)
    public void receiveMessage(String message) {
        System.out.println(message);
        Map map = JSON.parseObject(message, Map.class);
        //修改数据
        orderService.updatePayStatus((String)map.get("orderId"),(String)map.get("transactionId"));


    }
}
