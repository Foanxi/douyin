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
import com.douyin.util.RedisUtil;
import com.douyin.util.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.douyin.util.RedisIdentification.*;

/**
 * @author hongxiaobin
 */
@Service("FavouriteServiceImpl")
@Slf4j
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
    @Autowired
    private Entity2Model entity2Model;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public Favourite isExistFavourite(Long userId, Long videoId) {
        QueryWrapper<Favourite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("video_id", videoId);
        Favourite favourite =favouriteService.getOne(queryWrapper);
        log.info("点赞是否存在：{}",favourite);
        return favourite;
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
     * @param token  当前用户token
     * @return 返回用户点赞过的视频模型列表
     */
    @Override
    public List<VideoModel> getVideoByUser(String userId, String token) {
        List<VideoModel> videoModelList = new ArrayList<>();
        Long id = Long.parseLong(userId);
        //当前用户点赞的视频id列表
        List<Favourite> videoIdList = getFavouriteByVideoId(id);
        List<Video> videoList = new ArrayList<>();
        int size = videoIdList.size();
        if (size > 0) {
            for (Favourite favourite : videoIdList) {
                Video video = redisUtil.queryWithoutPassThrough(VIDEO_QUERY_KEY, favourite.getVideoId(), Video.class, videoService::getById, VIDEO_QUERY_TTL, TimeUnit.MINUTES);
                videoList.add(video);
            }
            for (Video video : videoList) {
                User user = redisUtil.queryWithoutPassThrough(USER_QUERY_KEY, video.getAuthorId(), User.class, userService::getById, USER_QUERY_TTL, TimeUnit.MINUTES);
                UserModel userModel = entity2Model.user2userModel(user, video.getVideoId(), token);
                VideoModel videoModel = entity2Model.video2videoModel(video, userModel, token);
                videoModelList.add(videoModel);
            }
            return videoModelList;
        }
        return null;
    }

    @Override
    public boolean doFavourite(Long videoId, Long userId) {
        Video video = redisUtil.queryWithoutPassThrough(VIDEO_QUERY_KEY, videoId, Video.class, videoService::getById, VIDEO_QUERY_TTL, TimeUnit.MINUTES);
        Favourite favourite = new Favourite(SnowFlake.nextId(), userId, videoId);
        //先查询数据库是否已有该条记录
        QueryWrapper<Favourite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("video_id", videoId);
        Favourite selectOne = baseMapper.selectOne(queryWrapper);
        if (selectOne == null && baseMapper.insert(favourite) == 1) {
            UpdateWrapper<Video> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("video_id", videoId).set("favourite_count", video.getFavouriteCount() + 1);
            boolean update = videoService.update(video, updateWrapper);
            redisUtil.deleteRedisContent(VIDEO_QUERY_KEY, String.valueOf(videoId));
            redisUtil.queryWithoutPassThrough(VIDEO_QUERY_KEY,videoId,Video.class,videoService::getById,VIDEO_QUERY_TTL,TimeUnit.MINUTES);
            return update;
        }
        return false;
    }

    @Override
    public boolean cancelFavourite(Long videoId, Long userId) {
        Video video = redisUtil.queryWithoutPassThrough(VIDEO_QUERY_KEY, videoId, Video.class, videoService::getById, VIDEO_QUERY_TTL, TimeUnit.MINUTES);
        QueryWrapper<Favourite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("video_id", videoId).eq("user_id", userId);
        if (favouriteService.remove(queryWrapper)) {
            // 然后再在视频表中将点赞数-1
            UpdateWrapper<Video> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("video_id", videoId).set("favourite_count", video.getFavouriteCount() - 1);
            boolean update = videoService.update(video, updateWrapper);
            log.info("取消点赞：{}",update);
            redisUtil.deleteRedisContent(VIDEO_QUERY_KEY, String.valueOf(videoId));
            redisUtil.queryWithoutPassThrough(VIDEO_QUERY_KEY,videoId,Video.class,videoService::getById,VIDEO_QUERY_TTL,TimeUnit.MINUTES);
            return update;
        }
        return false;
    }
}