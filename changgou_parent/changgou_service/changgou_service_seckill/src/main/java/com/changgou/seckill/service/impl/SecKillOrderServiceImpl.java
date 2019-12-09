package com.changgou.seckill.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.seckill.config.CustomMessageSender;
import com.changgou.seckill.config.RabbitMQConfig;
import com.changgou.seckill.dao.SeckillOrderMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.service.SecKillOrderService;
import com.changgou.util.IdWorker;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class SecKillOrderServiceImpl implements SecKillOrderService {

    @Autowired
    private RedisTemplate redisTemplate;
    private static final String SECKILL_GOODS_KEY = "seckill_goods_";

    private static final String SECKILL_GOODS_STOCK_COUNT_KEY = "seckill_goods_stock_count_key";
    @Autowired
    private IdWorker idWorker;

    @Autowired
    private CustomMessageSender customMessageSender;
    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Override
    public boolean add(Long id, String time, String username) {
        //防止用户恶意刷单
        String s = this.preventRepeatCommit(username, id);
        if ("fail".equals(s)) {
            return false;
        }

        // 防止 重复秒杀
        SeckillOrder secKillOrderByUserNameAndGoodsId = seckillOrderMapper.getSecKillOrderByUserNameAndGoodsId(username, id);
        if (secKillOrderByUserNameAndGoodsId != null) {
            return false;
        }
        //获取商品
        SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps(SECKILL_GOODS_KEY + time).get(id);
        // 获取库存
        String redisStock = (String) redisTemplate.opsForValue().get(SECKILL_GOODS_STOCK_COUNT_KEY + id);
        if (StringUtils.isEmpty(redisStock)) {
            return false;
        }
        int stock = Integer.parseInt(redisStock);
        if (seckillGoods == null || stock <= 0) {
            return false;
        }

        // 执行redis 扣减库存  decrement 原子性 妖魔都成功 妖魔都失败
        Long decrement = redisTemplate.opsForValue().decrement(SECKILL_GOODS_STOCK_COUNT_KEY + id);

        if (decrement <= 0) {

            //库存没了
            //删除商品信息
            redisTemplate.boundHashOps(SECKILL_GOODS_KEY + time).delete(id);

            //删除对应的库存信息
            redisTemplate.delete(SECKILL_GOODS_STOCK_COUNT_KEY + id);
        }


        //有库存
        //如果有库存，则创建秒杀商品订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setId(idWorker.nextId());
        seckillOrder.setSeckillId(id);
        seckillOrder.setMoney(seckillGoods.getCostPrice());
        seckillOrder.setUserId(username);
        seckillOrder.setSellerId(seckillGoods.getSellerId());
        seckillOrder.setCreateTime(new Date());
        seckillOrder.setStatus("0");
        // 发送消息
        customMessageSender.sendMessage("", RabbitMQConfig.SECKILL_ORDER_KEY, JSON.toJSONString(seckillOrder));
        return true;
    }


    public String preventRepeatCommit(String username, Long id) {

        String redisKey = "seckill_user_" + username + "_id_" + id;
        long count = redisTemplate.opsForValue().increment(redisKey, 1);
        if (count == 1) {
            //设置有效期五分钟
            redisTemplate.expire(redisKey, 5, TimeUnit.MINUTES);
            return "success";
        }

        if (count > 1) {
            return "fail";
        }

        return "fail";


    }

}

