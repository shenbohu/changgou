package com.changgou.order.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class RabbitMQConfig {
    //添加积分任务交换机
    public static final String EX_BUYING_ADDPOINTUSER = "ex_buying_addpointuser";
    //添加积分消息队列
    public static final String CG_BUYING_ADDPOINT = "cg_buying_addpoint";
    //完成添加积分消息队列
    public static final String CG_BUYING_FINISHADDPOINT = "cg_buying_finishaddpoint";
    //添加积分路由key
    public static final String CG_BUYING_ADDPOINT_KEY = "addpoint";
    //完成添加积分路由key
    public static final String CG_BUYING_FINISHADDPOINT_KEY = "finishaddpoint";
        //微信的消息队列
    public static final  String ORDER_PAY = "order_pay";
    //自动收货
    private static final String ORDER_TACK = "order_tack";

    @Bean
    public Queue ORDER_TACK() {
        return new Queue(ORDER_TACK);
    }

    @Bean
    public Queue queue() {
        return new Queue(ORDER_PAY);
    }

// 声明交换机  durable(true) 开启持久化
    @Bean(EX_BUYING_ADDPOINTUSER)
    public Exchange EX_BUYING_ADDPOINTUSER() {
       return ExchangeBuilder.directExchange(EX_BUYING_ADDPOINTUSER).durable(true).build();
    }

    //声明队列
    @Bean(CG_BUYING_FINISHADDPOINT)
    public Queue CG_BUYING_FINISHADDPOINT() {
        Queue queue = new Queue(CG_BUYING_FINISHADDPOINT,true);  //开启持久化   消息默认开启持久化
        return queue;
    }
    //声明队列
    @Bean(CG_BUYING_ADDPOINT)
    public Queue CG_BUYING_ADDPOINT() {
        Queue queue = new Queue(CG_BUYING_ADDPOINT);
        return queue;
    }
    /**
     * 绑定队列到交换机 .
     * @param queue    the queue
     * @param exchange the exchange
     * @return the binding
     */
    //Qualifier Bean 的名称  @Qualifier 交换机的名称
    @Bean
    public Binding BINDING_QUEUE_FINISHADDPOINT(@Qualifier(CG_BUYING_FINISHADDPOINT) Queue queue, @Qualifier(EX_BUYING_ADDPOINTUSER) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(CG_BUYING_FINISHADDPOINT_KEY).noargs();
    }
    @Bean
    public Binding BINDING_QUEUE_ADDPOINT(@Qualifier(CG_BUYING_ADDPOINT) Queue queue, @Qualifier(EX_BUYING_ADDPOINTUSER) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(CG_BUYING_ADDPOINT_KEY).noargs();
    }


}
