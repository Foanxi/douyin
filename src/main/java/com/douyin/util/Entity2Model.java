package com.douyin.util;

import com.douyin.model.UserModel;
import com.douyin.model.VideoModel;
import com.douyin.pojo.Relation;
import com.douyin.pojo.User;
import com.douyin.pojo.Video;
import com.douyin.service.FavouriteService;
import com.douyin.service.RelationService;
import com.douyin.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

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
    @Autowired
    private RedisUtil redisUtil;

    public UserModel user2userModel(User user, Long videoId, String token) {
        log.info("User:{},video:{},token:{}",user,videoId,token);
        boolean isFollow = false;
        if (token != null) {
            Long userId = JwtHelper.getUserId(token);
            Video video = redisUtil.queryWithoutPassThrough(RedisIdentification.VIDEO_QUERY_KEY, videoId, Video.class, videoService::getById, RedisIdentification.VIDEO_QUERY_TTL, TimeUnit.MINUTES);
            Long authorId = video.getAuthorId();
            Relation relation = relationService.getIsFollow(userId, authorId);
            if (relation != null){isFollow = true;}
            log.info("是否关注：{}",isFollow);
        }
        return new UserModel(user.getUserId(), user.getName(), user.getFollowCount(), user.getFollowerCount(), true);
    }

    public VideoModel video2videoModel(Video video, UserModel userModel, String token) {

        boolean isFavourite = false;
        if (token != null && !"".equals(token)) {
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
