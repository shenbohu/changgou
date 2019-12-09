package com.changgou.consume.listener;

import com.alibaba.fastjson.JSON;
import com.changgou.consume.config.RabbitMQConfig;
import com.changgou.consume.service.SecKillOrderService;
import com.changgou.seckill.pojo.SeckillOrder;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ConsumeListener {
    @Autowired
    private SecKillOrderService secKillOrderService;

    @RabbitListener(queues = RabbitMQConfig.SECKILL_ORDER_KEY)
    public void receiveSecKillOrderMessage(Channel channel, Message message) {

        // 设置预抓取
        try {
            channel.basicQos(300);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //转换消息
        SeckillOrder seckillOrder = JSON.parseObject(message.getBody(), SeckillOrder.class);

        //同步mysql订单
        int rows = secKillOrderService.createOrder(seckillOrder);
        if (rows > 0) {
            //返回成功通知  向消息服务器
            try {
                // basicAck 返回成功通知  getMessageProperties 唯一表示          // 开启批处理
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //返回失败通知
            try {
                //第一个boolean true所有消费者都会拒绝这个消息，false代表只有当前消费者拒绝
                //第二个boolean true当前消息会进入到死信队列，false重新回到原有队列中，默认回到头部
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
