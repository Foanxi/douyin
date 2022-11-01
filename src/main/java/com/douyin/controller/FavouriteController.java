package com.douyin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.douyin.service.FavouriteService;
import com.douyin.util.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/douyin/favorite")
public class FavouriteController {

    @Autowired
    private FavouriteService favouriteService;
    /** 点赞列表
     * @Param:
     * @Return:
     */
    @GetMapping("/list")
    public JSON getFavouriteList(@RequestParam("token") String token,
                                 @RequestParam("user_id") String userId){
        JSONObject jsonObject = new JSONObject();
        boolean expiration = JwtHelper.isExpiration(token);
        return favouriteService.getFavouriteListService(userId);
    }
}
