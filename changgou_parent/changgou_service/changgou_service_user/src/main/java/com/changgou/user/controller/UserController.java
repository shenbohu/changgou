package com.changgou.user.controller;

import com.changgou.entity.PageResult;
import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.user.config.TokenDecode;
import com.changgou.user.dao.UserinfoMapper;
import com.changgou.user.pojo.Userinfo;
import com.changgou.user.service.UserService;
import com.changgou.user.pojo.User;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;

    /**
     * 查询全部数据
     *
     * @return
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin')")
    public Result findAll() {
        List<User> userList = userService.findAll();
        return new Result(true, StatusCode.OK, "查询成功", userList);
    }

    /***
     * 根据ID查询数据
     * @param username
     * @return
     */
    @GetMapping("/{username}")
    public Result findById(@PathVariable String username) {
        User user = userService.findById(username);
        return new Result(true, StatusCode.OK, "查询成功", user);
    }


    /***
     * 新增数据
     * @param user
     * @return
     */
    @PostMapping
    public Result add(@RequestBody User user) {
        userService.add(user);
        return new Result(true, StatusCode.OK, "添加成功");
    }


    /***
     * 修改数据
     * @param user
     * @param username
     * @return
     */
    @PutMapping(value = "/{username}")
    public Result update(@RequestBody User user, @PathVariable String username) {
        user.setUsername(username);
        userService.update(user);
        return new Result(true, StatusCode.OK, "修改成功");
    }


    /***
     * 根据ID删除品牌数据
     * @param username
     * @return
     */
    @DeleteMapping(value = "/{username}")
    public Result delete(@PathVariable String username) {
        userService.delete(username);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    @GetMapping("/load/{username}")
    public User findUserInfo(@PathVariable("username") String username) {
        return userService.findById(username);
    }

    /***
     * 多条件搜索品牌数据
     * @param searchMap
     * @return
     */
    @GetMapping(value = "/search")
    public Result findList(@RequestParam Map searchMap) {
        List<User> list = userService.findList(searchMap);
        return new Result(true, StatusCode.OK, "查询成功", list);
    }


    /***
     * 分页搜索实现
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}")
    public Result findPage(@RequestParam Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<User> pageList = userService.findPage(searchMap, page, size);
        PageResult pageResult = new PageResult(pageList.getTotal(), pageList.getResult());
        return new Result(true, StatusCode.OK, "查询成功", pageResult);
    }

    // 修改资料
    //
    @Autowired
    private TokenDecode tokenDecode;
    @Autowired
    private UserinfoMapper userinfoMapper;
    @GetMapping("/data")
    public Result modifieddata(@RequestBody Userinfo userinfo) {
        String username = tokenDecode.getUserInfo().get("username");
        int i = userinfoMapper.updateByPrimaryKeySelective(userinfo);
        if(i<=0) {
            return new Result(false, StatusCode.OK, "修改bu成功");
        }
        return new Result(true, StatusCode.OK, "修改成功");
    }
    // 回显资料
    @GetMapping("/echo")
    public Result<Userinfo> echo() {
        String username = tokenDecode.getUserInfo().get("username");
        Example example = new Example(Userinfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username",username);
        List<Userinfo> userinfos = userinfoMapper.selectByExample(example);
        Userinfo userinfo = userinfos.get(0);
        return new Result(true, StatusCode.OK, "",userinfo);
    }

}
