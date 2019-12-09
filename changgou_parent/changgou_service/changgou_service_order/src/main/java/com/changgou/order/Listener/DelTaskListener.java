package com.changgou.order.Listener;

import com.alibaba.fastjson.JSON;
import com.changgou.order.config.RabbitMQConfig;
import com.changgou.order.pojo.Task;
import com.changgou.order.service.TaskService;
import com.sun.jmx.snmp.tasks.TaskServer;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DelTaskListener {

    @Autowired
    private TaskService taskServer;
    @RabbitListener(queues = RabbitMQConfig.CG_BUYING_FINISHADDPOINT)
    public void receiveMessage(String message){
        System.out.println("删除任务");
        Task task = JSON.parseObject(message, Task.class);

        taskServer.delTask(task);
    }
}
