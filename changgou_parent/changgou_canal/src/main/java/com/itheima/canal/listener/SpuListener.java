package com.itheima.canal.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.itheima.canal.config.RabbitMQConfig;
import com.itheima.canal.util.CanalUtil;
import com.xpand.starter.canal.annotation.CanalEventListener;
import com.xpand.starter.canal.annotation.ListenPoint;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


// 声明当前类 是canal的监听类
@CanalEventListener
public class SpuListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @ListenPoint(schema = "changgou_goods",table = "tb_spu")
    public void goodsUP(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
//        List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
//        afterColumnsList.forEach(column -> System.out.println(column));
//        List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
//        beforeColumnsList.forEach(column -> System.out.println(column));

        Map<String, String> beforemap = CanalUtil.convertToMap(rowData.getBeforeColumnsList());
        Map<String, String> aftermap = CanalUtil.convertToMap(rowData.getAfterColumnsList());
        System.out.println(JSON.toJSONString(beforemap));
        System.out.println(JSON.toJSONString(aftermap));
        // 获取最近上架的商品
        if("0".equals(beforemap.get("is_marketable")) && "1".equals(aftermap.get("is_marketable"))) {
            // 将商品发送到mq
            rabbitTemplate.convertAndSend(RabbitMQConfig.GOODS_UP_EXCHANGE,"",aftermap.get("id"));
        }

        //如果下架状态修改前的1，修改后的是0，则属于上架操作
        if( "1".equals(beforemap.get( "is_marketable" ))  && "0".equals( aftermap.get( "is_marketable" ) )  ){
            System.out.println("监控到tb_spu表变化（下架操作），发送到mq");
            rabbitTemplate.convertAndSend( RabbitMQConfig.GOODS_DOWN_EXCHANGE,"", aftermap.get( "id" ) );
        }

        //获取最新审核商品
        if ("0".equals(beforemap.get("status")) && "1".equals(aftermap.get("status"))){
            //发送商品spuId
            rabbitTemplate.convertAndSend(RabbitMQConfig.GOODS_UP_EXCHANGE,"",aftermap.get("id"));
        }

    }

}
