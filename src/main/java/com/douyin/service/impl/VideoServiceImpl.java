package com.douyin.service.impl;

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
import com.douyin.util.Entity2Model;
import com.douyin.util.JwtHelper;
import com.douyin.util.SnowFlake;
import com.douyin.util.VideoProcessing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

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
    @Value("${douyin.path}")
    private String resourcePath;

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
        List<Video> list = list(queryWrapper);
        for (Video video : list) {
            video.setPlayUrl(ipPath + video.getPlayUrl());
            video.setCoverUrl(ipPath + video.getCoverUrl());
        }
        return list;
    }

    @Override
    public Map<String, Object> feedVideo(String latestTime) {
        final String init = "0";
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        List<Video> videos;
        Map<String, Object> feedMap = new HashMap<>();
        if (init.equals(latestTime) || latestTime == null) {
            queryWrapper.last("limit " + limit);
            videos = baseMapper.selectList(queryWrapper);
        } else {
            Timestamp timestamp = new Timestamp(Long.parseLong(latestTime));
            queryWrapper.le("create_time", timestamp).last("limit " + limit);
            videos = videoService.list(queryWrapper);
            if (videos.size() == 0) {
                videos = baseMapper.selectList(new QueryWrapper<Video>().last("limit" + limit));
            }
        }
        if (videos.size() == 0) {
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

    /**
     * @param userId 作者id
     * @return 返回作者id视频模型
     */
    @Override
    public List<VideoModel> getPublishById(Long userId) {
        List<VideoModel> videoModelList = new ArrayList<>();
        List<Video> videoList = videoService.getVideo(userId);
        int size = videoList.size();
        if (size > 0) {
            // 得到userModel
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

    @Override
    public boolean publishVideo(MultipartFile data, String title, String token) {
        // 解析token得到用户ID
        Long userId = JwtHelper.getUserId(token);

        String filePath = resourcePath + "/" + "public/video" + "/" + userId;
        String picturePath = resourcePath + "/" + "public/picture" + "/" + userId;

        // 将文件存储到本地
        File fileLoad = new File(filePath);
        File pictureLoad = new File(picturePath);

        // 判断文件夹是否存在，如果不存在则创建新的文件夹
        if (!fileLoad.exists()) {
            if (!fileLoad.mkdir()) {
                return false;
            }
        }
        if (!pictureLoad.exists()) {
            if (!pictureLoad.mkdir()) {
                return false;
            }
        }

        //设置视频名称为UUID
        String uuid = UUID.randomUUID().toString();
        String videoPath = filePath + "/" + uuid + ".mp4";
        //设置图片名称
        String pictureName = picturePath + "/" + uuid + ".jpg";
        File filePathLoad = new File(videoPath);
        try {
            data.transferTo(filePathLoad);
        } catch (IOException e) {
            return false;
        }

        // 截取图片信息
        try {
            VideoProcessing.grabberVideoFramer(videoPath, pictureName);
        } catch (IOException e) {
            return false;
        }
        // 获取当前时间
        // 创建video对象导入数据库
        videoPath = "/video/" + userId + "/" + uuid + ".mp4";
        pictureName = "/picture/" + userId + "/" + uuid + ".jpg";
        Long videoId = SnowFlake.nextId();
        Video video = new Video(videoId, userId, videoPath, pictureName, title);
        videoService.save(video);
        return true;
    }
}
