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
import com.douyin.util.Entity2Model;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author foanxi
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {
    @Value("${douyin.ip_path}")
    private String ipPath;

    @Value("${video.limit}")
    private Integer limit;

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
    @Autowired
    private Entity2Model entity2Model;

    @Override
    public List<Video> getVideo(Long userId) {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("author_id", userId);
        return list(queryWrapper);
    }

    @Override
    public Map<String, Object> feedVideo(String latestTime) {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        List<Video> videos;
        Map<String, Object> feedMap = new HashMap<>();
        if ("".equals(latestTime) || latestTime == null) {
            queryWrapper.last("limit " + limit);
            videos = baseMapper.selectList(queryWrapper);
        } else {
            queryWrapper.le("create_time", latestTime).last("limit " + limit);
            videos = videoService.list(queryWrapper);
        }
        if (videos == null) {
            return null;
        }

        for (Video video : videos) {
            video.setPlayUrl(ipPath + video.getPlayUrl());
            video.setCoverUrl(ipPath + video.getCoverUrl());
        }
        feedMap.put("nextTime", videos.get(videos.size() - 1).getCreateTime());
        List<VideoModel> videoModelList = new ArrayList<>();
        for (Video video : videos) {
            User user = userService.getById(video.getAuthorId());
            UserModel userModel = entity2Model.user2userModel(user, video.getVideoId());
            VideoModel videoModel = entity2Model.video2videoModel(video, userModel);
            videoModelList.add(videoModel);
        }
        feedMap.put("videoModelList", videoModelList);
        return feedMap;
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
     * @param userId 作者id
     * @return 返回作者id视频模型
     */
    @Override
    public List<VideoModel> getVideoById(Long userId) {
        List<VideoModel> videoModelList = new ArrayList<>();
        List<Video> videoList = videoService.getVideo(userId);
        int size = videoList.size();
        if (size > 0) {
//        得到userModel
            User user = userService.getById(userId);
            for (Video video : videoList) {
                UserModel userModel = entity2Model.user2userModel(user, video.getVideoId());
                VideoModel videoModel = entity2Model.video2videoModel(video, userModel);
                videoModelList.add(videoModel);

            }
            return videoModelList;
        }
        return null;
    }
}
