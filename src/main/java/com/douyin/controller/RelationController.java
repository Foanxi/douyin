package com.douyin.controller;

import com.alibaba.fastjson.JSONObject;
import com.douyin.model.UserModel;
import com.douyin.service.RelationService;
import com.douyin.service.UserService;
import com.douyin.util.CreateJson;
import com.douyin.util.JwtHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @author hongxiaobin, zhuanghaoxin,foanxi
 */
@RestController
@RequestMapping("/douyin/relation")
@Slf4j
public class RelationController {
    @Autowired
    private RelationService relationService;
    @Autowired
    private UserService userService;

    @PostMapping("/action")
    public JSONObject action(@RequestParam("token") String token,
                             @RequestParam("to_user_id") String toUserId,
                             @RequestParam("action_type") String actionType) {
        log.info("action enter param token:{},toUserId:{},actionType:{}", token, toUserId, actionType);
        final String addType = "1";
        final String deleteType = "2";
        JSONObject json;
        if (JwtHelper.isExpiration(token)) {
            log.warn("action token: {} isExpiration", token);
            return CreateJson.createJson(200, 1, "用户token过期，请重新登陆");
        }
        Long userId = JwtHelper.getUserId(token);
        Long authorId = Long.parseLong(toUserId);
        if (Objects.equals(authorId, userId)) {
            log.warn("action User Operation failed");
            return CreateJson.createJson(200, 1, "用户不能关注自己");
        }

        if (addType.equals(actionType)) {
            boolean videoAndRelation = relationService.doFollow(authorId, userId);
            if (videoAndRelation) {
                json = CreateJson.createJson(200, 0, "关注成功");
                log.info("action return json:{}", JSONObject.toJSONString(json, true));
                return json;
            }
            log.warn("action relation failed");
            return CreateJson.createJson(200, 0, "关注失败");
        } else if (deleteType.equals(actionType)) {
            boolean cancelRelation = relationService.cancelFollow(authorId, userId);
            if (cancelRelation) {
                json = CreateJson.createJson(200, 0, "取消关注成功");
                log.info("action return json:{}", JSONObject.toJSONString(json, true));
                return json;
            }
            log.warn("action cancel relation failed");
            return CreateJson.createJson(200, 0, "取消关注失败");
        }
        log.warn("action invalid operation");
        return CreateJson.createJson(200, 0, "无效操作");
    }

    @GetMapping("/follow/list/")
    public JSONObject followList(@RequestParam("user_id") String userId,
                                 @RequestParam("token") String token) {
        log.info("followList enter param user_id: {},token: {}", userId, token);
        JSONObject json;
        if (JwtHelper.isExpiration(token)) {
            log.warn("followList token: {} isExpiration", token);
            return CreateJson.createJson(200, 1, "用户token过期，请重新登陆");
        }
        List<UserModel> followList = relationService.getFollowList(Long.valueOf(userId));
        if (followList == null) {
            log.info("followList is null,The current user has not followed anyone");
            return CreateJson.createJson(200, 0, "当前用户没有关注任何人");
        } else {
            json = CreateJson.createJson(200, 0, "");
            json.put("user_list", followList);
            log.info("followList return json: {}", JSONObject.toJSONString(json, true));
            return json;
        }

    }
}
