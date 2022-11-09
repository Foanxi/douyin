package com.douyin.util;

import com.douyin.model.UserModel;
import com.douyin.model.VideoModel;
import com.douyin.pojo.User;
import com.douyin.pojo.Video;
import com.douyin.service.FavouriteService;
import com.douyin.service.RelationService;
import com.douyin.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author foanxi
 */
@Component
@Slf4j
public class Entity2Model {

    @Autowired
    private RelationService relationService;
    @Autowired
    private VideoService videoService;
    @Autowired
    private FavouriteService favouriteService;

    public UserModel user2userModel(User user, Long videoId) {
        Long authorId = videoService.getById(videoId).getAuthorId();
        boolean isFollow = relationService.getIsFollow(user.getUserId(), authorId);
        return new UserModel(user.getUserId(), user.getName(), user.getFollowCount(), user.getFollowerCount(), isFollow);
    }

    public VideoModel video2videoModel(Video video, UserModel userModel,String token) {

        boolean isFavourite = false;
        if (token != null && !"".equals(token)){
            isFavourite = favouriteService.isExistFavourite(userModel.getId(), video.getVideoId()) != null;
        }
        return new VideoModel(
                video.getVideoId(),
                userModel,
                video.getPlayUrl(),
                video.getCoverUrl(),
                video.getFavouriteCount(),
                video.getCommentCount(),
                isFavourite,
                video.getTitle());
    }
}
