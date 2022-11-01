package com.douyin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.douyin.pojo.User;
import com.douyin.pojo.Video;
import com.douyin.pojo.Videouser;
import com.douyin.service.UserService;
import com.douyin.service.VideoService;
import com.douyin.util.JwtHelper;
import com.douyin.util.VideoProcessing;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/douyin/publish")
@Slf4j
public class PublishController {

    @Autowired
    private VideoService videoService;
    @Autowired
    private UserService userService;
    @Value("${douyin.path}")
    private String resourcePath;

    /**
     * 发布列表
     *
     * @Param:
     * @Return:
     */
    @GetMapping("/list")
    public JSON getUserList(@RequestParam("token") String token,
                            @RequestParam("user_id") String userId) {
        JSONObject jsonObject = new JSONObject();
        boolean expiration = JwtHelper.isExpiration(token);
    /*    if (expiration) {
            jsonObject.put("status_code", 404);
            jsonObject.put("status_msg", "token失效");
            jsonObject.put("user", null);
            return jsonObject;
        }*/
        User user = userService.getUserById(userId);
        List<Video> videoList = videoService.getVideo(userId);
        List<Videouser> list = new ArrayList<>();
        for (Video video : videoList) {
            Videouser videouser = new Videouser(
                    video.getId(),
                    user,
                    video.getPlayUrl(),
                    video.getCoverUrl(),
                    video.getFavouriteCount(),
                    video.getCommentCount(),
                    video.getCreateTime(),
                    video.getTitle()
            );
            list.add(videouser);
        }
        System.out.println(list);
        jsonObject.put("http_status", 200);
        jsonObject.put("status_code", 0);
        jsonObject.put("status_msg", "视频列表展示成功");
        jsonObject.put("video_list", list);
        log.info("返回的数据体为:{}", jsonObject);
        return jsonObject;
    }

    @PostMapping("/action")
    public JSON uploadVideo(HttpServletRequest request, MultipartFile data, @RequestParam("title") String title,
                            @RequestParam("token") String token) {
        JSONObject jsonObject = new JSONObject();
        log.info("传输视频的用户的token是：{}", token);
//        校验token
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            jsonObject.put("status_code", 400);
            jsonObject.put("status_msg", "用户验证已过期");
            return jsonObject;
        }
//        解析token得到用户ID
        String userId = JwtHelper.getUserId(token).toString();
        log.info("解析出的用户ID是：{}", userId);

//        本地视频的地址
        String filePath = resourcePath + "\\" + "video" + "\\" + userId;
        String picturePath = resourcePath + "\\" + "picture" + "\\" + userId;
        System.out.println(filePath);
//        将文件存储到本地
        File fileLoad = new File(filePath);
        File pictureLoad = new File(picturePath);

//        判断文件夹是否存在，如果不存在则创建新的文件夹
        if (!fileLoad.exists()) {
            fileLoad.mkdir();
        }
        if (!pictureLoad.exists()) {
            pictureLoad.mkdir();
        }
        //设置视频名称为UUID
        String uuid = UUID.randomUUID().toString();
        log.info("该视频的UUID为：{}", uuid);
        String videoPath = filePath + "\\" + uuid + ".mp4";
        //设置图片名称
        String pictureName = picturePath + "\\" + uuid + ".jpg";
        File filePathLoad = new File(videoPath);
        try {
            data.transferTo(filePathLoad);
        } catch (IOException e) {
            e.printStackTrace();
        }


//      截取图片信息
        VideoProcessing.grabberVideoFramer(videoPath, pictureName);
        log.info("该视频的截图为：{}", pictureName);
//      获取当前时间
        long now = System.currentTimeMillis();
//        log.info("当前时间为：{}",);
//      创建video对象导入数据库
//        "\\3\\139fade2-4375-4a91-ab76-7f7a5a570c9e.mp4"
        videoPath = userId + "/" + uuid + ".mp4";
        pictureName = userId + "/" + uuid + ".jpg";
        Video video = new Video(0L, userId, videoPath, pictureName, 0, 0, now, title);
        videoService.save(video);
        jsonObject.put("http_status", 200);
        jsonObject.put("status_code", 0);
        jsonObject.put("status_msg", "视频上传成功");
        return jsonObject;
    }
}
