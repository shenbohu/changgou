package com.changgou.search.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.search.service.impl.ESManagerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manager")
public class ESManagerController {
    @Autowired
    private ESManagerServiceImpl esManagerService;
    // 创建索引库
    @GetMapping("/creat")
   public Result creat() {
        esManagerService.createIndexAndMapping();
        return new Result(true, StatusCode.OK,"创建成功");
    }
    @GetMapping("/importAll")
    public Result importAll() {
        esManagerService.importAll();
        return new Result(true, StatusCode.OK,"导入成功");
    }


}
