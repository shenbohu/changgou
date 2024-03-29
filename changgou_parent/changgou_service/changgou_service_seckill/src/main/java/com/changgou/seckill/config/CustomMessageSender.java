package com.changgou.seckill.config;

import com.alibaba.fastjson.JSON;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class CustomMessageSender  implements RabbitTemplate.ConfirmCallback {
    // 增强rabbitma
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    // 接受消息服务器rabbitmq返回的通知的  ack 成功还是失败
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack){
            //返回成功通知
            //删除redis中的相关数据
            redisTemplate.delete(correlationData.getId());
            redisTemplate.delete(MESSAGE_CONFIRM_+correlationData.getId());
        }else {
            //返回失败通知
            Map<String, String> map = (Map<String, String>) redisTemplate.opsForHash().entries(MESSAGE_CONFIRM_ + correlationData.getId());
            String exchange = map.get("exchange");
            String routingKey = map.get("routingKey");
            String sendMessage = map.get("sendMessage");

            //重新发送
            rabbitTemplate.convertAndSend(exchange, routingKey, JSON.toJSONString(sendMessage));
        }
    }

    public CustomMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setConfirmCallback(this);
    }
    private static final String MESSAGE_CONFIRM_="message_confirm_";
    //自定义发送方法
    public void sendMessage(String exchange,String routingKey,String message){

        //设置消息唯一标识并存入缓存 CorrelationData
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        redisTemplate.opsForValue().set(correlationData.getId(),message);

        //本次发送到相关元信息存入缓存
        Map<String, String> map = new HashMap<>();
        map.put("exchange", exchange);
        map.put("routingKey", routingKey);
        map.put("sendMessage", message);
        redisTemplate.opsForHash().putAll(MESSAGE_CONFIRM_+correlationData.getId(),map);

        //携带唯一标识发送消息  correlationData 必须写
        rabbitTemplate.convertAndSend(exchange,routingKey,message,correlationData);
    }
}
