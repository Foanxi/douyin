package com.douyin.util;

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
public class VideoProcessing {

    // 获取要取得的帧数
    private static final int fifthFrame = 5;

    /**
     * 将视频的字节流截图
     *
     * @param
     * @return
     */
    public static InputStream grabberVideoFramer(InputStream videofile) {
        FFmpegFrameGrabber grabber;
        InputStream img = null;
        try {
            grabber = new FFmpegFrameGrabber(videofile);
            grabber.start();
            // 视频总帧数
            int videoLength = grabber.getLengthInFrames();

            Frame frame = null;
            int i = 0;
            while (i < videoLength) {
                // 过滤前5帧,因为前5帧可能是全黑的
                frame = grabber.grabFrame();
                if ((i > fifthFrame) && (frame.image != null)) {
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
            e.printStackTrace();
        }
        return img;
    }

    /**
     * 将BufferedImage转换为InputStream
     *
     * @param image
     * @return
     */
    public static InputStream bufferedImageToInputStream(BufferedImage image) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", os);
            InputStream input = new ByteArrayInputStream(os.toByteArray());
            return input;
        } catch (IOException e) {

        }
        return null;
    }

}