package com.douyin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.douyin.model.VideoModel;
import com.douyin.pojo.Video;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


/**
 * @author foanxi, zhuanghaoxin
 */
public interface VideoService extends IService<Video> {
    /**
     * 获取对应的用户id的视频列表
     *
     * @param userId 用户id
     * @return 返回视频列表
     */
    List<Video> getVideo(Long userId);

    /**
     * 返回视频流的视频集合
     *
     * @param latestTime 上次返回的视频时间
     * @return 视频集合
     */
    Map<String, Object> feedVideo(String latestTime,String token);

    /**
     * 获取用户发布列表
     *
     * @param authorId 作者id
     * @param token    当前登录用户的token
     * @return 返回视频列表模型
     */
    List<VideoModel> getPublishById(Long authorId, String token);


    /**
     * 处理上传的视频
     *
     * @param title 视频标题
     * @param token 上传视频的作者的token
     * @param data  视频数据
     * @return 返回视频上传是否成功
     */
    boolean publishVideo(MultipartFile data, String title, String token);
}
