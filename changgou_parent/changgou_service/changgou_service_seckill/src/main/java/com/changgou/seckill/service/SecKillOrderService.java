package com.changgou.seckill.service;

public interface SecKillOrderService {
    //秒杀
    boolean add(Long id, String time, String username);
}
