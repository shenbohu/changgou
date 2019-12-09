package com.changgou.order.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name="tb_task")
    public class Task {
    public void setId(Long id) {
        this.id = id;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public void setMqExchange(String mqExchange) {
        this.mqExchange = mqExchange;
    }

    public void setMqRoutingkey(String mqRoutingkey) {
        this.mqRoutingkey = mqRoutingkey;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    @Id
        private Long id;

        @Column(name = "create_time")
        private Date createTime;

        @Column(name = "update_time")
        private Date updateTime;

        @Column(name = "delete_time")
        private Date deleteTime;

        @Column(name = "task_type")
        private String taskType;

        @Column(name = "mq_exchange")
        private String mqExchange;

        @Column(name = "mq_routingkey")
        private String mqRoutingkey;

        @Column(name = "request_body")
        private String requestBody;

        @Column(name = "status")
        private String status;

        @Column(name = "errormsg")
        private String errormsg;

        //getter，setter略

    public Task(Long id, Date createTime, Date updateTime, Date deleteTime, String taskType, String mqExchange, String mqRoutingkey, String requestBody, String status, String errormsg) {
        this.id = id;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.deleteTime = deleteTime;
        this.taskType = taskType;
        this.mqExchange = mqExchange;
        this.mqRoutingkey = mqRoutingkey;
        this.requestBody = requestBody;
        this.status = status;
        this.errormsg = errormsg;
    }

    public Task() {
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", deleteTime=" + deleteTime +
                ", taskType='" + taskType + '\'' +
                ", mqExchange='" + mqExchange + '\'' +
                ", mqRoutingkey='" + mqRoutingkey + '\'' +
                ", requestBody='" + requestBody + '\'' +
                ", status='" + status + '\'' +
                ", errormsg='" + errormsg + '\'' +
                '}';
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public String getTaskType() {
        return taskType;
    }

    public String getMqExchange() {
        return mqExchange;
    }

    public String getMqRoutingkey() {
        return mqRoutingkey;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public String getStatus() {
        return status;
    }

    public String getErrormsg() {
        return errormsg;
    }
}
