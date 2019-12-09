package com.changgou.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.order.feign.CartFeign;
import com.changgou.order.feign.CenterFeign;
import com.changgou.user.feign.FileFeign;
import com.changgou.user.feign.UserFeign;
import com.changgou.user.pojo.Userinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.jws.WebParam;
import java.util.Map;

@Controller
@RequestMapping("/wocenter")
public class CenterController {
    @Autowired
    private FileFeign fileFeign;
    @Autowired
    private UserFeign userFeign;
    //上传
    @PostMapping("/file")
    @ResponseBody
    public void list(Model model, MultipartFile file) {
        Result result = fileFeign.uploadFile(file);
        String img = (String) result.getData();
        model.addAttribute("img",img);

    }

    @GetMapping("/info")
    public String info(){
       return "center-setting-info";

    }

    @GetMapping("/data")
    @ResponseBody
    public void userinfoResult(@RequestBody Userinfo userinfo , Model model){
        Result modifieddata = userFeign.modifieddata(userinfo);
    }

    @GetMapping("/echo")
    @ResponseBody
    public void  userinfo(Model model) {
        Result<Userinfo> echo = userFeign.echo();
        Userinfo data = echo.getData();
        model.addAttribute("userinfo",data);
    }

}
