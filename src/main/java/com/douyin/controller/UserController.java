package com.douyin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.douyin.pojo.User;
import com.douyin.service.UserService;
import com.douyin.util.JwtHelper;
import com.douyin.util.Md5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/douyin/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 用户信息
     *
     * @Param:
     * @Return:
     */
    @GetMapping("/")
    public JSON getUserInformation(@RequestParam("user_id") String userId,
                                   @RequestParam("token") String token) {
        JSONObject jsonObject = new JSONObject();
        //校验token
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            jsonObject.put("status_code", 404);
            jsonObject.put("status_msg", "token失效");
            jsonObject.put("user", null);
            return jsonObject;
        }
        //查询数据
        User byId = userService.getById(userId);
        jsonObject.put("status_code", 200);
        jsonObject.put("status_msg", "查询成功");
        jsonObject.put("user", byId);
        return jsonObject;
    }

    @PostMapping("/login")
    public JSON login(@RequestBody User user) {
        JSONObject jsonObject = new JSONObject();
        String password = user.getPassword();
        String md5Password = Md5.encrypt(password);
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getName, user.getName());
        User u = userService.getOne(lqw);

        if (u == null) {
            jsonObject.put("status_code", 200);
            jsonObject.put("status_msg", "用户不存在");
            return jsonObject;
        } else if (!u.getPassword().equals(md5Password)) {
            jsonObject.put("status_code", 200);
            jsonObject.put("status_msg", "用户密码错误");
            return jsonObject;
        }else {
            jsonObject.put("status_code", 200);
            jsonObject.put("status_msg", "登陆成功");
            jsonObject.put("user_id",u.getId());
            jsonObject.put("token",JwtHelper.createToken(u.getId()));
            return jsonObject;
        }
    }
}
