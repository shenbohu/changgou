package com.changgou.consume.service.impl;

import com.changgou.consume.dao.SeckillGoodsMapper;
import com.changgou.consume.dao.SeckillOrderMapper;
import com.changgou.consume.service.SecKillOrderService;
import com.changgou.seckill.pojo.SeckillOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SecKillOrderServiceImpl implements SecKillOrderService {

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

     // 添加订单  同步到mysql

    @Override
    @Transactional
    public int createOrder(SeckillOrder seckillOrder) {
        // 库存扣减
        int result =seckillGoodsMapper.updateStockCount(seckillOrder.getSeckillId());
        if (result<=0){
            return result;
        }
        // 新增秒杀订单
        result =seckillOrderMapper.insertSelective(seckillOrder);
        if (result<=0){
            return result;
        }

        return 1;
    }
}
