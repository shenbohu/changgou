package com.changgou.order.service.impl;


import com.changgou.order.dao.TaskHisMapper;
import com.changgou.order.dao.TaskMapper;
import com.changgou.order.pojo.Task;
import com.changgou.order.pojo.TaskHis;
import com.changgou.order.service.TaskService;
import com.sun.jmx.snmp.tasks.TaskServer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskHisMapper taskHisMapper;
    @Autowired
    private TaskMapper taskMapper;
    @Override
    public void delTask(Task task) {
    //1;记录删除时间
        task.setDeleteTime(new Date());
        Long taskId = task.getId();
        task.setId(null);

        //bean 拷贝.
        TaskHis taskHis = new TaskHis();
        BeanUtils.copyProperties(task,taskHis);

        //记录任务信息
        taskHisMapper.insertSelective(taskHis);
        //删除原任务
        task.setId(taskId);
        taskMapper.deleteByPrimaryKey(task);
    }
}
