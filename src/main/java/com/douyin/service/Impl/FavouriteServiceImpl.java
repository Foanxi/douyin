package com.douyin.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

    @Override
    public Favourite isExistFavourite(Long userId, Long videoId) {
        QueryWrapper<Favourite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("video_id", videoId);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean updateFavourite(String actionType, Favourite favourite, Long userId, String videoId) {
        //说明用户存在，此时是修改用户的点赞数据
        QueryWrapper<Favourite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("video_id", videoId);
        return super.update(favourite, queryWrapper);
    }

    /**
     * 获取当前用户点赞过的视频id集合
     *
     * @param id
     * @Param:
     * @Return:
     */
    @Override
    public List<Favourite> getVideoId(Long id) {
        QueryWrapper<Favourite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("favourite_id", id);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 点赞列表
     *
     * @return
     * @Param:
     * @Return:
     */
    @Override
    public List<VideoModel> getVideoByUser(String userId) {
        List<VideoModel> videoModelList = new ArrayList<>();
        Long id = Long.parseLong(userId);
//        当前用户点赞的视频id列表
        // TODO: 2022/11/4 优化：减少查询次数-多表连接
        List<Favourite> videoIdList = getVideoId(id);
        List<Video> videoList = null;
        assert videoList != null;
        int size = videoIdList.size();
        if (size > 0) {
            for (Favourite favourite : videoIdList) {
                List<Video> video = videoService.getVideo(favourite.getUserId());
                videoList.addAll(video);
            }
            for (Video video : videoList) {
                User user = userService.getById(video.getAuthorId());
                boolean isFavourite = isExistFavourite(id, video.getId()) != null;
                boolean isFollow = relationService.getIsFollow(id, video.getAuthorId());
                UserModel userModel = new UserModel(user.getUserId(), user.getName(), user.getFollowCount(), user.getFollowerCount(), isFollow);
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
