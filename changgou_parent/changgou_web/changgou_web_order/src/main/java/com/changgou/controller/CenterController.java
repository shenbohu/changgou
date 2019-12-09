package com.changgou.controller;

import com.changgou.entity.Result;
import com.changgou.order.feign.CenterFeign;
import com.changgou.order.pojo.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/wcenter")
public class CenterController {
    @Autowired
    private CenterFeign centerFeign;

    //查询
    @GetMapping("/list")
    public String list(Model model) {
        Result list = centerFeign.list();
        List<OrderItem> data = (List<OrderItem>) list.getData();
        model.addAttribute("list", data);
        return "center-index";
    }

}
