package com.douyin.util;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author zhuanghaoxin
 */
public class VideoProcessing {
    public static void grabberVideoFramer(String videoFileName, String pictureName) throws IOException {
        File targetFile = new File(pictureName);
        try (FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videoFileName)) {
            ff.start();
            int length = ff.getLengthInFrames();
            int i = 0;
            Frame f = new Frame();
            while (i < length) {
                // 去掉前5帧，避免出现全黑的图片，依自己情况而定
                f = ff.grabImage();
                if ((i > 5) && (f.image != null)) {
                    break;
                }
                i++;
            }
            ImageIO.write(frameToBufferedImage(f), "jpg", targetFile);
            ff.stop();
        } catch (IOException e) {
            throw new IOException();
        }
    }

    public static BufferedImage frameToBufferedImage(Frame frame) {
        //创建BufferedImage对象
        Java2DFrameConverter converter = new Java2DFrameConverter();
        return converter.getBufferedImage(frame);
    }
}