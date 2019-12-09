package com.changgou.order.task;

import com.alibaba.fastjson.JSON;
import com.changgou.order.config.RabbitMQConfig;
import com.changgou.order.dao.TaskMapper;
import com.changgou.order.pojo.Task;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
@Component
public class QueryPointTask {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private TaskMapper taskMapper;

    @Scheduled(cron = "0 0/2 * * * ?")
    public void queryTask() {

        //1.获取小于系统当前时间数据
        List<Task> taskList = taskMapper.findTaskLessTanCurrentTime(new Date());

        if (taskList != null && taskList.size() > 0) {
            //将任务数据发送到消息队列
            for (Task task : taskList) {
                rabbitTemplate.convertAndSend(RabbitMQConfig.EX_BUYING_ADDPOINTUSER, RabbitMQConfig.CG_BUYING_ADDPOINT_KEY, JSON.toJSONString(task));
                System.out.println("订单服务向添加积分队列发送消息");
            }
        }
    }
}
