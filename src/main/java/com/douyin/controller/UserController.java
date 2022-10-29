package com.douyin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.douyin.pojo.User;
import com.douyin.service.UserService;
import com.douyin.util.JwtHelper;
import com.douyin.util.Md5;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;


@RestController
@RequestMapping("/douyin/user")
@Slf4j
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
        log.info("用户id"+userId);
        log.info("用户token"+token);
        //校验token
        boolean expiration = JwtHelper.isExpiration(token);
/*        if (expiration) {
            jsonObject.put("status_code", 404);
            jsonObject.put("status_msg", "token失效");
            jsonObject.put("user", null);
            log.info("返回：{}",jsonObject);
            return jsonObject;
        }*/
        //查询数据
        User byId = userService.getUserById(userId);
        jsonObject.put("http_status",200);
        jsonObject.put("status_code", 0);
        jsonObject.put("status_msg", "查询成功");
        jsonObject.put("user", byId);
        log.info("返回的数据体为：{}",jsonObject);
        return jsonObject;
    }

    @PostMapping("/login")
    public JSON login(@RequestParam("username") String username,@RequestParam("password") String password) {
        JSONObject jsonObject = new JSONObject();
        String md5Password = Md5.encrypt(password);
        User u = userService.getUserByUsername(username);
        log.info("用户为：{}",u);
        if (u == null) {
            jsonObject.put("http_status",400);
            jsonObject.put("status_code", 1);
            jsonObject.put("status_msg", "用户不存在");
            return jsonObject;
        } else if (!u.getPassword().equals(md5Password)) {
            jsonObject.put("http_status",400);
            jsonObject.put("status_code", 1);
            jsonObject.put("status_msg", "用户密码错误");
            return jsonObject;
        } else {
            jsonObject.put("http_status",200);
            jsonObject.put("status_code", 0);
            jsonObject.put("status_msg", "登陆成功");
            jsonObject.put("user_id", u.getId().intValue());
            jsonObject.put("token", JwtHelper.createToken(u.getId()));
            log.info("jsonObject:{}",jsonObject);
            return jsonObject;
        }
    }

    @PostMapping("/register")
    public JSON register(@RequestParam("username") String username, @RequestParam("password") String password) {
        JSONObject jsonObject = new JSONObject();
        User user = null;
//        首先先判断数据库中是否有该用户。
        if (userService.getUserByUsername(username) != null) {
            log.info("已经有用户注册过了");
            user = userService.getUserByUsername(username);
//            返回错误信息
            jsonObject.put("http_status",400);
            jsonObject.put("status_code", 1);
            jsonObject.put("status_msg", "用户已存在");
            jsonObject.put("user_id", user.getId());
            jsonObject.put("token", JwtHelper.createToken(user.getId()));
            return jsonObject;
        }
//        没有该用户，可以进行注册
//        先对用户的密码进行加密，后将账号密码发送到数据库存储
        String md5password = DigestUtils.md5DigestAsHex(password.getBytes());
        log.info("用户加密后的密码为：{}", md5password);
//        由于用户初始注册，并没有关注数和被关注数，因此都设置为0
        user = new User(username, md5password, 0, 0);
        boolean save = userService.save(user);
        log.info("是否添加成功：{}",save);
        jsonObject.put("http_status",200);
        jsonObject.put("status_code", 0);
        jsonObject.put("status_msg", "添加成功");
        jsonObject.put("user_id", user.getId());
        jsonObject.put("token", JwtHelper.createToken(user.getId()));
        log.info("返回的数据体为：{}",jsonObject);
        return jsonObject;
    }
}
