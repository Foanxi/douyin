package com.douyin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.douyin.model.UserModel;
import com.douyin.pojo.User;
import com.douyin.service.UserService;
import com.douyin.util.CreateJson;
import com.douyin.util.JwtHelper;
import com.douyin.util.Md5;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


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
     * @param userId 用户唯一标识id
     * @param token  用户令牌
     * @return 返回用户信息
     */
    @GetMapping("/")
    public JSON getUserInformation(@RequestParam("user_id") String userId,
                                   @RequestParam("token") String token) {
        log.info("getUserInformation enter param token: {}, userId: {}", token, userId);
        // 校验token
        if (JwtHelper.isExpiration(token)) {
            log.warn("getUserInformation token: {} isExpiration", token);
            return CreateJson.createJson(200, 1, "用户token过期，请重新登陆");
        }
        // 当前用户id
        Long id = JwtHelper.getUserId(token);
        // 查询数据
        UserModel byIdList = userService.getUserById(id, Long.valueOf(userId));
        log.info("getUserInformation list: {}", byIdList);
        JSONObject jsonObject = CreateJson.createJson(200, 0, "查询成功");
        jsonObject.put("user", byIdList);
        log.info("getUserInformation out param json:{}", JSONObject.toJSONString(jsonObject, true));
        return jsonObject;
    }

    @PostMapping("/login")
    public JSON login(@RequestParam("username") String username, @RequestParam("password") String password) {
        log.info("login enter param username: {},password: {}", username, password);
        String md5Password = Md5.encrypt(password);
        User u = userService.getUserByUsername(username);
        if (u == null) {
            log.warn("login operation failed,username: {} is not exist", username);
            return CreateJson.createJson(200, 1, "用户不存在");
        } else if (!u.getPassword().equals(md5Password)) {
            log.warn("login operation failed,username: {} and password: {} are inconsistent", username, password);
            return CreateJson.createJson(200, 1, "用户密码错误");
        } else {
            JSONObject jsonObject = CreateJson.createJson(200, 0, "登陆成功");
            jsonObject.put("user_id", u.getUserId());
            jsonObject.put("token", JwtHelper.createToken(u.getUserId()));
            log.info("login return json: {}", JSONObject.toJSONString(jsonObject, true));
            return jsonObject;
        }
    }

    @PostMapping("/register")
    public JSON register(@RequestParam("username") String username, @RequestParam("password") String password) {

        final Integer success = 1;
        final Integer exist = -1;
        final String statusCode = "statusCode";

        Map<String, Object> map = userService.register(username, password);
        JSONObject json;
        //首先先判断数据库中是否有该用户
        if (map.get(statusCode).equals(exist)) {
            //返回错误信息
            return CreateJson.createJson(200, 1, "用户已存在");
        }

        if (map.get(statusCode).equals(success)) {
            json = CreateJson.createJson(200, 0, "添加成功");
            json.put("user_id", map.get("userId"));
            json.put("token", map.get("token"));
        } else {
            json = CreateJson.createJson(200, 1, "注册失败");
        }
        return json;
    }
}
