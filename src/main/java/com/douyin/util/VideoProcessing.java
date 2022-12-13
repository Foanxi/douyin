package com.douyin.util;

import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author zhuanghaoxin hongxiaobin
 */
@Slf4j
public class VideoProcessing {

    /**
     * 获取要取得的帧数
     */
    private static final int FIFTH_FRAME = 5;

    /**
     * 将视频的字节流截图
     *
     * @param videoFile 需要为ByteArrayInputStream的文件流输入
     * @return InputStream 返回截取的图片流
     */
    public static InputStream grabberVideoFramer(InputStream videoFile) {
        FFmpegFrameGrabber grabber;
        InputStream img = null;
        try {
            grabber = new FFmpegFrameGrabber(videoFile);
            grabber.start();
            // 视频总帧数
            int videoLength = grabber.getLengthInFrames();

            Frame frame = null;
            int i = 0;
            while (i < videoLength) {
                // 过滤前5帧,因为前5帧可能是全黑的
                frame = grabber.grabFrame();
                if ((i > FIFTH_FRAME) && (frame.image != null)) {
                    break;
                }
                i++;
            }

            Java2DFrameConverter converter = new Java2DFrameConverter();
            // 绘制图片
            BufferedImage bufferedImage = converter.getBufferedImage(frame);

            img = bufferedImageToInputStream(bufferedImage);
            grabber.stop();
            grabber.close();
        } catch (IOException e) {
            log.error("VideoProcessing.grabberVideoFramer", "Stream close failed");
        }
        return img;
    }

    /**
     * 将BufferedImage转换为InputStream
     *
     * @param image 输入BufferedImage类型的输入流
     * @return 返回InputStream流
     */
    public static InputStream bufferedImageToInputStream(BufferedImage image) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", os);
            return new ByteArrayInputStream(os.toByteArray());
        } catch (IOException e) {
            log.error("VideoProcessing.bufferedImageToInputStream", "Type conversion failed");
        }
        return null;
    }

}