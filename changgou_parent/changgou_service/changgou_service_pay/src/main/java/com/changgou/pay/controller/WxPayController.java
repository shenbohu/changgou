package com.changgou.pay.controller;

import com.alibaba.fastjson.JSON;
import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.pay.service.WxPayService;
import com.changgou.util.ConvertUtils;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.print.DocFlavor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/wxpay")
public class WxPayController {

    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 下单
     *
     * @param orderId
     * @param money
     * @return
     */
    @GetMapping("/nativePay")
    public Result nativePay(@RequestParam("orderId") String orderId, @RequestParam("money") Integer money) {
        Map map = wxPayService.nativePay(orderId, money);
        return new Result(true, StatusCode.OK, "", map);
    }

    /**
     * 回调
     */
    @RequestMapping("/notify")
    public void notifyLogic(HttpServletRequest request,HttpServletResponse response) {
        System.out.println("支付成功回调。。。。");
        try {
            // 输入流转换为字符串
            String xml = ConvertUtils.convertToString(request.getInputStream());
            System.out.println(xml);
            //基于微信发送的通知内容,完成后续的处理
            Map<String, String> map = WXPayUtil.xmlToMap(xml);
            if("SUCCESS".equals(map.get( "result_code" ))) {
                // 成功 支付
                // 查询订单
                Map result = wxPayService.queryOrder(map.get("out_trade_no"));
                System.out.println("查询订单的结果"+request);
                if("SUCCESS".equals( result.get( "result_code" ) )) {
                    // 成功  发送消息到mq
                    Map m=new HashMap();
                    m.put( "orderId",result.get( "out_trade_no" ) );
                    m.put( "transactionId",result.get( "transaction_id" ));
                    // 发送消息
                    rabbitTemplate.convertAndSend( "","order_pay", JSON.toJSONString(m));

                    // 完成双向通信

                    rabbitTemplate.convertAndSend("paynotify","",result.get( "out_trade_no" ));

                } else {
                    System.out.println(map.get( "err_code_des" )); //错误信息描述
                }
            } else {
                System.out.println(map.get( "err_code_des" )); //错误信息描述
            }
            //给微信结果通知
            response.setContentType("text/xml");
            String data = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
            response.getWriter().write(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 基于微信查询订单
    @GetMapping("query/{orderId}")
    public Result queryOrder(@PathVariable("orderId") String  orderId) {
        Map map = wxPayService.queryOrder(orderId);
        return new Result(true,StatusCode.OK, "查询成功",map);
    }

    // 关闭订单
    @PostMapping("/close/{orderId}")
    public Result closeOrder(@PathVariable("orderId") String orderId) {
        Map map = wxPayService.closeOrder(orderId);
        return new Result(true,StatusCode.OK, "查询成功",map);
    }
}
