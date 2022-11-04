package com.douyin.util;

import com.douyin.model.UserModel;
import com.douyin.pojo.User;
import com.douyin.service.Impl.RelationServiceImpl;
import com.douyin.service.Impl.VideoServiceImpl;
import com.douyin.service.RelationService;
import com.douyin.service.VideoService;
import org.springframework.stereotype.Component;

/**
 * @author foanxi
 */
@Component
public class Entity2Model {

    static RelationService relationService = new RelationServiceImpl();
    static VideoService videoService = new VideoServiceImpl();

    public static UserModel user2userModel(User user, String videoId) {
        Long authorId = videoService.getById(Long.parseLong(videoId)).getAuthorId();
        boolean isFollow = relationService.getIsFollow(user.getId(), authorId);
        return new UserModel(user.getId(), user.getName(), user.getFollowCount(), user.getFollowerCount(), isFollow);
    }
}
