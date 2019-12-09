package com.changgou.user.feign;

import com.changgou.entity.Result;
import com.changgou.user.pojo.User;
import com.changgou.user.pojo.Userinfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "user")
@RequestMapping("/user")
public interface UserFeign {
    @GetMapping("/load/{username}")
    public User findUserInfo(@PathVariable("username") String username);

    @GetMapping("/data")
    public Result modifieddata(@RequestBody Userinfo userinfo);

    // 回显资料
    @GetMapping("/echo")
    public Result<Userinfo> echo();

}
