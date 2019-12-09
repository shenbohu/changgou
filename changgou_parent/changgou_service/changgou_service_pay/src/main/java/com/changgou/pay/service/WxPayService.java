package com.changgou.pay.service;

import java.util.HashMap;
import java.util.Map;

public interface WxPayService {

    /**
     * 生成微信支付二维码
     * @param orderId
     * @param money
     * @return
     */
    Map nativePay(String orderId, Integer money);


    //基于微信查询订单

    public Map queryOrder(String orderId) ;

    //基于微信查询订单关闭订单

    Map closeOrder(String orderId);
}
