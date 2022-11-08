package com.douyin.util;

import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author zhuanghaoxin
 */
@Slf4j
public class VideoProcessing {
    public static void grabberVideoFramer(String videoFileName, String pictureName) {
        File targetFile = new File(pictureName);
        try (FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videoFileName)) {
            try {
                ff.start();
            } catch (FrameGrabber.Exception e) {
                e.printStackTrace();
            }
            int length = ff.getLengthInFrames();
            int i = 0;
            Frame f = new Frame();
            while (i < length) {
                // 去掉前5帧，避免出现全黑的图片，依自己情况而定
                try {
                    f = ff.grabImage();
                } catch (FrameGrabber.Exception e) {
                    e.printStackTrace();
                }
                if ((i > 5) && (f.image != null)) {
                    break;
                }
                i++;
            }
            try {
                ImageIO.write(frameToBufferedImage(f), "jpg", targetFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                ff.stop();
            } catch (FrameGrabber.Exception e) {
                e.printStackTrace();
            }
        } catch (FrameGrabber.Exception e) {
            log.error("视频截图出现异常");
        }
    }

    public static BufferedImage frameToBufferedImage(Frame frame) {
        //创建BufferedImage对象
        Java2DFrameConverter converter = new Java2DFrameConverter();
        return converter.getBufferedImage(frame);
    }
}