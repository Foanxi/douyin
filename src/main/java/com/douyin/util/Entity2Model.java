package com.douyin.util;

import com.douyin.model.UserModel;
import com.douyin.pojo.User;
import com.douyin.service.Impl.RelationServiceImpl;
import com.douyin.service.Impl.VideoServiceImpl;
import com.douyin.service.RelationService;
import com.douyin.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author foanxi
 */
@Component
@Slf4j
public class Entity2Model {

    static RelationService relationService = new RelationServiceImpl();
    static VideoService videoService = new VideoServiceImpl();

    public static UserModel user2userModel(User user, String videoId) {
        log.info("user2userModel的videoId为：{}",videoId);
        Long authorId = videoService.getById(Long.parseLong(videoId)).getAuthorId();
        boolean isFollow = relationService.getIsFollow(user.getUserId(), authorId);
        return new UserModel(user.getUserId(), user.getName(), user.getFollowCount(), user.getFollowerCount(), isFollow);
    }
}
