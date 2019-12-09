package com.itheima.canal.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.itheima.canal.config.RabbitMQConfig;
import com.itheima.canal.util.CanalUtil;
import com.xpand.starter.canal.annotation.CanalEventListener;
import com.xpand.starter.canal.annotation.ListenPoint;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author ZJ
 */

@CanalEventListener  // 声明当前类是canal的监听类
public class BusinessListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //eventType 当前操作哪一个数据库
    //rowData 当前操作数据库的数据
    @ListenPoint(schema = "changgou_business", table = {"tb_ad"})
    public void adUpdate(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        System.err.println("广告数据发生变化");

        //修改前数据
        //rowData.getBeforeColumnsList().forEach((column -> System.out.println(column.getName()+"++++++"+column.getValue())));
        Map<String, String> map = CanalUtil.convertToMap(rowData.getBeforeColumnsList());
        System.out.println(JSON.toJSONString(map));


        //修改后数据
        rowData.getAfterColumnsList().forEach((column -> System.out.println(column.getName()+"++++++"+column.getValue())));
        Map<String, String> afterMap = CanalUtil.convertToMap( rowData.getAfterColumnsList()); //修改后数据
        System.out.println( "修改后数据"+JSON.toJSONString(afterMap));
        for (CanalEntry.Column column : rowData.getAfterColumnsList()) {
            if ("position".equals(column.getName())) {
                System.out.println("++"+column.getValue());
                rabbitTemplate.convertAndSend("", RabbitMQConfig.AD_UPDATE_QUEUE,column.getValue());
            }

        }
    }
}
