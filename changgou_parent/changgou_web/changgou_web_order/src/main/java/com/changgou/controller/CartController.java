package com.changgou.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.order.feign.CartFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/wcart")
public class CartController {
    @Autowired
    private CartFeign cartFeign;
    //查询
    @GetMapping("/list")
    public String list(Model model) {
        Map list = cartFeign.list();
        model.addAttribute("items",list);
        return "cart";
    }

    @GetMapping("/add")
    @ResponseBody
    public Result<Map> mapResult(String id,Integer num){
        cartFeign.addCart(id,num);
        Map list = cartFeign.list();
        return new Result<>(true, StatusCode.OK,"添加成功");

    }

}
