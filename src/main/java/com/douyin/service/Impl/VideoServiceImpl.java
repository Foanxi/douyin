package com.douyin.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.douyin.mapper.VideoMapper;
import com.douyin.model.UserModel;
import com.douyin.model.VideoModel;
import com.douyin.pojo.User;
import com.douyin.pojo.Video;
import com.douyin.service.FavouriteService;
import com.douyin.service.RelationService;
import com.douyin.service.UserService;
import com.douyin.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {
    @Autowired
    private VideoService videoService;
    @Autowired
    private UserService userService;
    @Autowired
    private FavouriteService favouriteService;
    @Autowired
    private RelationService relationService;


    @Override
    public List<Video> getVideo(String userId) {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("author_id", userId);
        return list(queryWrapper);
    }

    /**
     * 发布列表
     *
     * @Param:
     * @Return:
     */
    @Override
    public VideoModel[] getVideoUser(String userId) {
        User user = userService.getUserById(userId);
        List<Video> videoList = videoService.getVideo(userId);
        int size = videoList.size();
        if (size > 0) {
            VideoModel[] videoModel = new VideoModel[size];
            for (int i = 0; i < size; i++) {
                // TODO: 2022/11/3 优化减少查询次数
                boolean isFavourite = favouriteService.getIsFavourite(userId, videoList.get(i).getId());
                boolean isFolllow = relationService.getIsFollow(userId, videoList.get(i).getAuthorId());
                UserModel userModel = new UserModel(user.getId(), user.getName(), user.getFollowCount(), user.getFollowerCount(), isFolllow);
                videoModel[i] = new VideoModel(
                        videoList.get(i).getId(),
                        userModel,
                        videoList.get(i).getPlayUrl(),
                        videoList.get(i).getCoverUrl(),
                        videoList.get(i).getFavouriteCount(),
                        videoList.get(i).getCommentCount(),
                        isFavourite,
                        videoList.get(i).getTitle());
            }
            return videoModel;
        }
        return null;
    }
}
