package com.changgou.order.Listener;

import com.changgou.order.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderTimeoutListener {
    @Autowired
    private OrderService orderService;


    // 回滚 订单
    @RabbitListener(queues = "queue.ordertimeout")  //queue.ordertimeout  queue.ordertimeout
    public void closeOrder(String message) {
        System.out.println("接收到关闭订单消息：" + message);
        try {
            orderService.closeOrder(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
