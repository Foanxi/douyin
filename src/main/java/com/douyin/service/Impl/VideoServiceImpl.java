package com.douyin.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
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
    @Autowired
    private VideoMapper videoMapper;

    @Override
    public List<Video> getVideo(Long userId) {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("author_id", userId);
        return list(queryWrapper);
    }


    @Override
    public void updateVideoFavourite(Long videoId, Long userId, String actionType) {
        Video video = new Video();
//        获取当前视频的点赞数
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("video_id", videoId).eq("author_id", userId);
        Video video1 = videoMapper.selectOne(queryWrapper);
        log.info("video1:{}", video1);
        Integer favouriteCount = video1.getFavouriteCount();
//       判断当前的点击是点赞还是取消点赞
        if ("1".equals(actionType)) {
            video.setFavouriteCount(favouriteCount + 1);
            log.info("视频点赞……");
        } else if ("2".equals(actionType) && favouriteCount != 0) {
            video.setFavouriteCount(favouriteCount - 1);
            log.info("视频取消点赞……");
        }
        UpdateWrapper<Video> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("video_id", videoId).eq("author_id", userId);
        videoMapper.update(video, updateWrapper);
    }

    /**
     * 通过用户Id获取当前用户的发布列表
     *
     * @param userId
     * @Param: Long userId 用户id
     * @Return: List<VideoModel>
     */
    @Override
    public List<VideoModel> getVideoById(Long userId) {
        List<VideoModel> videoModelList = null;
        List<Video> videoList = videoService.getVideo(userId);
        int size = videoList.size();
        if (size > 0) {
//        得到userModel
            User user = userService.getById(userId);
            for (Video video : videoList) {
                boolean isFavourite = favouriteService.isExistFavourite(userId, video.getId()) != null;
                boolean isFollow = relationService.getIsFollow(userId, video.getAuthorId());
                UserModel userModel = new UserModel(user.getUserId(), user.getName(), user.getFollowCount(), user.getFollowerCount(), isFollow);
                assert videoModelList != null;
                videoModelList.add(new VideoModel(
                        video.getId(),
                        userModel,
                        video.getPlayUrl(),
                        video.getCoverUrl(),
                        video.getFavouriteCount(),
                        video.getCommentCount(),
                        isFavourite,
                        video.getTitle()));
            }
            return videoModelList;
        }
        return null;
    }

}
