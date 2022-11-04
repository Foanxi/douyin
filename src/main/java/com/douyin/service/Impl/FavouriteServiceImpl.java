package com.douyin.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.douyin.mapper.FavouriteMapper;
import com.douyin.pojo.Favourite;
import com.douyin.service.FavouriteService;
import com.douyin.service.UserService;
import com.douyin.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public boolean getIsFavourite(String userId, Long id) {
        QueryWrapper<Favourite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("video_id", id);
        return baseMapper.selectOne(queryWrapper) != null;
    }
    public Favourite isExistFavourite(Long userId,Long videoId){
        QueryWrapper<Favourite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        queryWrapper.eq("video_id",videoId);
        Favourite favourite = super.getOne(queryWrapper);
        log.info("favourite:{}",favourite);
        return favourite;
    }
    public boolean updateFavourite(String actionType, Favourite favourite,Long userId,String videoId) {
        //        说明用户存在，此时是修改用户的点赞数据
        QueryWrapper<Favourite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        queryWrapper.eq("video_id",videoId);
        favourite.setVideoType(Integer.parseInt(actionType));
        boolean update = super.update(favourite, queryWrapper);
        videoService.updateVideoFavourite(Long.parseLong(videoId),userId,actionType);
        return update;
    }
}
