package com.douyin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.douyin.pojo.User;
import com.douyin.service.UserService;
import com.douyin.util.CreateJson;
import com.douyin.util.JwtHelper;
import com.douyin.util.Md5;
import com.douyin.util.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;


/**
 * @author foanxi, hongxiaobin
 */
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
        //校验token
        if (!JwtHelper.isExpiration(token)) {
            return CreateJson.createJson(200, 1, "用户token过期，请重新登陆");
        }
        //查询数据
        User byId = userService.getUserById(userId);
        JSONObject jsonObject = CreateJson.createJson(200, 0, "查询成功");
        jsonObject.put("user", byId);
        log.info("返回的数据体为：{}", jsonObject);
        return jsonObject;
    }

    @PostMapping("/login")
    public JSON login(@RequestParam("username") String username, @RequestParam("password") String password) {
        String md5Password = Md5.encrypt(password);
        User u = userService.getUserByUsername(username);
        log.info("用户为：{}", u);
        if (u == null) {
            return CreateJson.createJson(400, 1, "用户不存在");
        } else if (!u.getPassword().equals(md5Password)) {
            return CreateJson.createJson(400, 1, "用户密码错误");
        } else {
            JSONObject jsonObject = CreateJson.createJson(200, 0, "登陆成功");
            jsonObject.put("user_id", u.getUserId().intValue());
            jsonObject.put("token", JwtHelper.createToken(u.getUserId()));
            log.info("jsonObject:{}", jsonObject);
            return jsonObject;
        }
    }

    @PostMapping("/register")
    public JSON register(@RequestParam("username") String username, @RequestParam("password") String password) {
        User user;
        //首先先判断数据库中是否有该用户
        if (userService.getUserByUsername(username) != null) {
            log.info("已经有用户注册过了");
            user = userService.getUserByUsername(username);
            //返回错误信息
            JSONObject jsonObject = CreateJson.createJson(400, 1, "用户已存在");
            jsonObject.put("user_id", user.getUserId());
            jsonObject.put("token", JwtHelper.createToken(user.getUserId()));
            return jsonObject;
        }
//        没有该用户，可以进行注册
//        先对用户的密码进行加密，后将账号密码发送到数据库存储
        String md5password = DigestUtils.md5DigestAsHex(password.getBytes());
        log.info("用户加密后的密码为：{}", md5password);
//        由于用户初始注册，并没有关注数和被关注数，因此都设置为0
        user = new User(SnowFlake.nextId(), username, md5password, 0, 0);
        boolean save = userService.save(user);
        log.info("是否添加成功：{}", save);
        JSONObject jsonObject = CreateJson.createJson(200, 0, "添加成功");
        jsonObject.put("user_id", user.getUserId());
        jsonObject.put("token", JwtHelper.createToken(user.getUserId()));
        log.info("返回的数据体为：{}", jsonObject);
        return jsonObject;
    }
}
