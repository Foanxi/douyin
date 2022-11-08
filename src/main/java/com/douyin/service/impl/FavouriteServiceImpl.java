package com.douyin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.douyin.mapper.FavouriteMapper;
import com.douyin.model.UserModel;
import com.douyin.model.VideoModel;
import com.douyin.pojo.Favourite;
import com.douyin.pojo.User;
import com.douyin.pojo.Video;
import com.douyin.service.FavouriteService;
import com.douyin.service.RelationService;
import com.douyin.service.UserService;
import com.douyin.service.VideoService;
import com.douyin.util.Entity2Model;
import com.douyin.util.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hongxiaobin
 */
@Slf4j
@Service("FavouriteServiceImpl")
@Transactional(rollbackFor = Exception.class)
public class FavouriteServiceImpl extends ServiceImpl<FavouriteMapper, Favourite> implements FavouriteService {

    @Value("${douyin.ip_path}")
    private String ipPath;
    @Autowired
    private UserService userService;
    @Autowired
    private VideoService videoService;
    @Autowired
    private RelationService relationService;
    @Autowired
    private FavouriteService favouriteService;
    @Autowired
    private Entity2Model entity2Model;

    @Override
    public Favourite isExistFavourite(Long userId, Long videoId) {
        QueryWrapper<Favourite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("video_id", videoId);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 获取当前用户点赞过的视频id集合
     *
     * @param id 用户id
     * @Return: 返回用户点赞过的所有视频集合
     */
    @Override
    public List<Favourite> getFavouriteByVideoId(Long id) {
        QueryWrapper<Favourite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", id);
        return baseMapper.selectList(queryWrapper);
    }


    /**
     * 通过用户id查询当前用户点赞过的所有视频
     *
     * @param userId 用户id
     * @return 返回用户点赞过的视频模型列表
     */
    @Override
    public List<VideoModel> getVideoByUser(String userId) {
        List<VideoModel> videoModelList = new ArrayList<>();
        Long id = Long.parseLong(userId);
        //当前用户点赞的视频id列表
        List<Favourite> videoIdList = getFavouriteByVideoId(id);
        List<Video> videoList = new ArrayList<>();
        int size = videoIdList.size();
        if (size > 0) {
            for (Favourite favourite : videoIdList) {
                Video video = videoService.getById(favourite.getVideoId());
                video.setPlayUrl(ipPath + video.getPlayUrl());
                video.setCoverUrl(ipPath + video.getCoverUrl());
                if (video == null) {
                    break;
                }
                videoList.add(video);
            }
            for (Video video : videoList) {
                User user = userService.getById(video.getAuthorId());
                UserModel userModel = entity2Model.user2userModel(user, video.getVideoId());
                VideoModel videoModel = entity2Model.video2videoModel(video, userModel);
                videoModelList.add(videoModel);
            }
            return videoModelList;
        }
        return null;
    }

    @Override
    public boolean doFavourite(Long videoId, Long userId) {
        Video video1 = videoService.getById(videoId);
        long id = SnowFlake.nextId();
        Favourite favourite = new Favourite(id, userId, videoId);
        if (1 == baseMapper.insert(favourite)) {
            UpdateWrapper<Video> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("video_id", videoId).set("favourite_count", video1.getFavouriteCount() + 1);
            return videoService.update(video1, updateWrapper);
        }
        return false;
    }

    @Override
    public boolean cancelFavourite(Long videoId, Long userId) {
        Video video1 = videoService.getById(videoId);
        QueryWrapper<Favourite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("video_id", videoId).eq("user_id", userId);
        if (favouriteService.remove(queryWrapper)) {
            // 然后再在视频表中将点赞数-1
            UpdateWrapper<Video> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("video_id", videoId).set("favourite_count", video1.getFavouriteCount() - 1);
            boolean update = videoService.update(video1, updateWrapper);
            log.info("视频表点赞数是否已-1：{}", update);
            return update;
        }
        return false;
    }
}