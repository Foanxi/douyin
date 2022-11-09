package com.douyin.controller;

import com.alibaba.fastjson.JSONObject;
import com.douyin.service.RelationService;
import com.douyin.service.UserService;
import com.douyin.util.CreateJson;
import com.douyin.util.JwtHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author hongxiaobin, zhuanghaoxin
 */
@RestController
@RequestMapping("/douyin/relation")
@Slf4j
public class RelationController {
    @Autowired
    private RelationService relationService;
    @Autowired
    private UserService userService;
    /**
     * 关注操作
     * param token 用来鉴权用户是否登陆以及获取关注用户的Id
     * param toUserId 被关注的用户Id
     * param actionType 当值为1时为关注，为2时为取消关注
     * return
     */
    @PostMapping("/action")
    public JSONObject action(@RequestParam("token")String token,
                             @RequestParam("to_user_id")String toUserId,
                             @RequestParam("action_type")String actionType){
        log.info("action enter param token:{},toUserId:{},actionType:{}",token,toUserId,actionType);
        final String addType = "1";
        final String deleteType = "2";
        JSONObject json;
        if (JwtHelper.isExpiration(token)){
            log.warn("action token: {} isExpiration", token);
            return CreateJson.createJson(200, 1, "用户token过期，请重新登陆");
        }
        Long authorId = JwtHelper.getUserId(token);
        Long userId = Long.parseLong(toUserId);
        if (Objects.equals(authorId, userId)){
            log.warn("action User Operation failed");
            return CreateJson.createJson(200,1,"用户不能关注自己");
        }

        if (addType.equals(actionType)){
            boolean videoAndRelation = relationService.updateVideoAndRelation(authorId, userId);
            if (videoAndRelation){
                json = CreateJson.createJson(200,0,"关注成功");
                log.info("action return json:{}",json);
                return json;
            }
            log.warn("action relation failed");
            return CreateJson.createJson(200,0,"关注失败");
        }else if (deleteType.equals(actionType)){
            boolean cancelRelation = relationService.cancelRelation(authorId, userId);
            if (cancelRelation){
                json = CreateJson.createJson(200,0,"取消关注成功");
                log.info("action return json:{}",json);
                return json;
            }
            log.warn("action cancel relation failed");
            return CreateJson.createJson(200,0,"取消关注失败");
        }
        log.warn("action invalid operation");
        return CreateJson.createJson(200,0,"无效操作");
    }
}
