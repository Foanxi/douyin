package com.douyin.service.Impl;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserService userService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private RelationService relationService;
    @Autowired
    private FavouriteService favouriteService;
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
    public List<Favourite> getVideoId(Long id) {
        QueryWrapper<Favourite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", id);
        return baseMapper.selectList(queryWrapper);
    }


    /**
     * @param userId 用户id
     * @return 返回用户点赞过的视频模型列表
     */
    @Override
    public List<VideoModel> getVideoByUser(String userId) {
        List<VideoModel> videoModelList = new ArrayList<>();
        Long id = Long.parseLong(userId);
//        当前用户点赞的视频id列表
        // TODO: 2022/11/4 优化：减少查询次数-多表连接
        List<Favourite> videoIdList = getVideoId(id);
        List<Video> videoList = new ArrayList<>();
        int size = videoIdList.size();
        if (size > 0) {
            for (Favourite favourite : videoIdList) {

                List<Video> video = videoService.getVideo(favourite.getUserId());
                log.info("提取出的视频为：{}",video);
                if(video == null){
                    log.info("video为空");
                    break;
                }
                videoList.addAll(video);
                log.info("videoList:{}",videoList);
            }
            for (Video video : videoList) {
                User user = userService.getById(video.getAuthorId());
                boolean isFavourite = isExistFavourite(id, video.getVideoId()) != null;
                boolean isFollow = relationService.getIsFollow(id, video.getAuthorId());
                UserModel userModel = new UserModel(user.getUserId(), user.getName(), user.getFollowCount(), user.getFollowerCount(), isFollow);
                videoModelList.add(new VideoModel(
                        video.getVideoId(),
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
    @Override
    public boolean giveFavourite(String actionType,Long videoId,Long userId) {
        Video video1 = videoService.getById(videoId);
        Favourite favourite1 = new Favourite();
        favourite1.setVideoId(videoId);
        favourite1.setUserId(userId);
        boolean save = favouriteService.save(favourite1);
        log.info("点赞列是否创建成功：{}", save);
        // 然后再在视频表中将点赞数+1
        UpdateWrapper<Video> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("video_id", videoId).set("favourite_count", video1.getFavouriteCount() + 1);
        boolean update = videoService.update(video1, updateWrapper);
        log.info("视频表点赞数是否已+1：{}", update);
        return update;
    }
    public boolean notGiveFavourite(String actionType, Long videoId, Long userId) {
        Video video1 = videoService.getById(videoId);
        QueryWrapper<Favourite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("video_id", videoId).eq("user_id", userId);
        boolean remove = favouriteService.remove(queryWrapper);
        log.info("点赞列是否移除成功：{}", remove);
        //然后再在视频表中将点赞数-1
        UpdateWrapper<Video> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("video_id", videoId).set("favourite_count", video1.getFavouriteCount() - 1);
        boolean update = videoService.update(video1, updateWrapper);
        log.info("视频表点赞数是否已-1：{}", update);
        return update;
    }
}
