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
import com.douyin.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.douyin.util.RedisIdentification.*;

/**
 * @author foanxi hongxiaobin
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {
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
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 获取指定用户的视频
     *
     * @param userId 用户id
     * @return 返回视频列表
     */
    @Override
    public List<Video> getVideo(Long userId) {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("author_id", userId);
        List<Video> list = list(queryWrapper);
        return list;
    }

    @Override
    public Map<String, Object> feedVideo(String latestTime, String token) {
        final String init = "0";
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        List<Video> videos;
        Map<String, Object> feedMap = new HashMap<>();
        if (init.equals(latestTime) || latestTime == null) {
            queryWrapper.orderByDesc("create_time").last("limit " + limit);
            videos = baseMapper.selectList(queryWrapper);
        } else {
            Timestamp timestamp = new Timestamp(Long.parseLong(latestTime));
            queryWrapper.lt("create_time", timestamp).orderByDesc("create_time").last("limit " + limit);
            videos = videoService.list(queryWrapper);
            if (videos.size() == 0) {
                videos = baseMapper.selectList(new QueryWrapper<Video>().orderByDesc("create_time").last("limit " + limit));
            }
        }
        if (videos.size() == 0) {
            return null;
        }
        feedMap.put("nextTime", videos.get(videos.size() - 1).getCreateTime());
        List<VideoModel> videoModelList = new ArrayList<>();
        for (Video video : videos) {
            User author = redisUtil.queryWithoutPassThrough(USER_QUERY_KEY, video.getAuthorId(), User.class, userService::getById, USER_QUERY_TTL, TimeUnit.MINUTES);
            UserModel userModel = entity2Model.user2userModel(author, video.getVideoId(), token);
            VideoModel videoModel = entity2Model.video2videoModel(video, userModel, token);
            videoModelList.add(videoModel);
        }
        feedMap.put("videoModelList", videoModelList);
        return feedMap;
    }

    /**
     * @param authorId 作者id
     * @return 返回作者id视频模型
     */
    @Override
    public List<VideoModel> getPublishById(Long authorId, String token) {
        List<VideoModel> videoModelList = new ArrayList<>();
        List<Video> videoList = videoService.getVideo(authorId);
        int size = videoList.size();
        if (size > 0) {
            // 得到userModel
            User author = userService.getById(authorId);
            for (Video video : videoList) {
                UserModel userModel = entity2Model.user2userModel(author, video.getVideoId(), token);
                VideoModel videoModel = entity2Model.video2videoModel(video, userModel, token);
                videoModelList.add(videoModel);
            }
            return videoModelList;
        }
        return null;
    }

    /**
     * 处理上传视频和图片
     *
     * @Param: data 前端传过来的视频数据
     * @Param: title 视频说明
     * @Param: token 用户鉴权
     * @Return: boolean 是否上传成功 true-成功 false-失败
     */
    @Override
    public boolean publishVideo(MultipartFile data, String title, String token) {
        // 解析token得到用户ID
        Long userId = JwtHelper.getUserId(token);

        String filePackage = "video" + "/" + userId;
        String picturePackage = "picture" + "/" + userId;

        //设置视频名称为UUID
        String uuid = UUID.randomUUID().toString();
        String videoName = filePackage + "/" + uuid + ".mp4";
        //设置图片名称
        String pictureName = picturePackage + "/" + uuid + ".jpg";

//        上传视频和图片
        String videoPath = null;
        String picturePath = null;
        try {
            InputStream videoInputStream = data.getInputStream();
            videoPath = OssClient.uploadSource(videoInputStream, videoName);
            byte[] bytes = data.getBytes();
            InputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            InputStream pictureInputStream = VideoProcessing.grabberVideoFramer(byteArrayInputStream);
            picturePath = OssClient.uploadSource(pictureInputStream, pictureName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Long videoId = SnowFlake.nextId();
        Video video = new Video(videoId, userId, videoPath, picturePath, title);
        videoService.save(video);
        //添加视频时将其加入到缓存中
        redisUtil.queryWithoutPassThrough(VIDEO_QUERY_KEY, videoId, Video.class, videoService::getById, VIDEO_QUERY_TTL, TimeUnit.MINUTES);
        return true;
    }
}
