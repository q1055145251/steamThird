package com.example.springboot3demo.utils;
import com.example.springboot3demo.common.enumType.ErrorCode;
import com.example.springboot3demo.exceptionhandler.MessageException;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class MyUtils {


    /**
     * 生成一段随机的字符串（默认种子 a-zA-Z0-9）
     *
     * @param value 前缀文本
     * @param i     生成数（不包括前缀文本）
     * @return 随机生成的字符串
     */
    public static String getRangeString(String value, int i) {
        if (value == null) {
            value = "";
        }
        return getRangeString(new StringBuffer(value), i, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
    }

    /**
     * 生成一段随机的字符串
     *
     * @param value       前缀文本
     * @param length      生成数（不包括前缀文本）
     * @param rangeString 随机种子
     * @return 随机生成的字符串
     */
    public static String getRangeString(StringBuffer value, int length, String rangeString) {
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            value.append(rangeString.charAt(number));
        }
        return value.toString();
    }


    /**
     * 判断主机端口
     *
     * @param hostName 主机名   本地为localhost
     * @param port     端口
     * @return boolean - true/false
     */
    public static boolean isSocketAliveSuitabilityCrunchy(String hostName, int port) {
        boolean isAlive = false;

        // 创建一个套接字
        SocketAddress socketAddress = new InetSocketAddress(hostName, port);
        Socket socket = new Socket();

        // 超时设置，单位毫秒
        int timeout = 500;

        try {
            socket.connect(socketAddress, timeout);
            socket.close();
            isAlive = true;

        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return isAlive;
    }


    /**
     * 切割数组
     *
     * @param array     数组
     * @param groupSize 组大小
     * @return {@link List}<{@link List}<{@link T}>>
     */
    public static <T> List<List<T>> groupArray(Collection<T> array, int groupSize) {
        int numOfGroups = (int) Math.ceil((double) array.size() / groupSize);
        List<T> list = new ArrayList<>(array);
        return IntStream.range(0, numOfGroups)
                .mapToObj(i -> list.subList(i * groupSize, Math.min((i + 1) * groupSize, list.size())))
                .collect(Collectors.toList());
    }

    /**
     * 计算时间
     * 使用此方法将会环绕记录代码的耗时时长，并输出到日志
     *
     * @param action 需要执行的代码
     * @param name   方法记录名
     */
    public static void computingTime(Runnable action, String name) {
        long startTime = System.currentTimeMillis();
        action.run();
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        log.info("[{}]方法执行完毕!耗时:{}", name, formatTime(elapsedTime));
    }


    /**
     * 格式时间单位
     * 使用正确的单位显示时间大小，最大到分秒
     *
     * @param time 时间
     * @return {@link String}
     */
    public static String formatTime(long time) {
        if (time > 60000) {
            long minutes = time / 60000;
            long seconds = (time % 60000) / 1000;
            return minutes + "分" + seconds + "秒";
        } else if (time > 1000) {
            return time / 1000 + "秒";
        } else {
            return time + "毫秒";
        }
    }

    /**
     * 格式文件大小
     * 使用正确的单位显示文件大小，最大到GB，保留两位小数
     *
     * @param size 大小
     * @return {@link String}
     */
    public static String formatSize(long size) {
        double fileSize = (double) size;
        // 以GB为单位，并保留两位小数
        DecimalFormat df = new DecimalFormat("0.00");
        if (fileSize >= 1024 * 1024 * 1024) {
            double gbSize = fileSize / (1024 * 1024 * 1024);
            return df.format(gbSize) + " GB";
        } else if (fileSize >= 1024 * 1024) {
            double mbSize = fileSize / (1024 * 1024);
            return df.format(mbSize) + " MB";
        } else if (fileSize >= 1024) {
            double kbSize = fileSize / 1024;
            return df.format(kbSize) + " KB";
        } else {
            return size + " Byte";
        }
    }

    /**
     * 图片裁剪
     *
     * @param inputStream 图片输入流
     * @param scale       裁剪比例%
     * @return {@link byte[]}
     * @throws IOException ioexception
     */
    public static byte[] scaleImg(InputStream inputStream, Integer scale) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        if (bufferedImage == null) {
            throw new MessageException(ErrorCode.FILE_IMG_ERROR);
        }
        // 计算缩略图的宽度和高度
        int thumbnailWidth = (int) (bufferedImage.getWidth() * ((double) scale / 100));
        int thumbnailHeight = (int) (bufferedImage.getHeight() * ((double) scale / 100));
        BufferedImage image = new BufferedImage(thumbnailWidth, thumbnailHeight, bufferedImage.getType()); // 保持与原始图像类型一致
        Graphics2D graphics = image.createGraphics();
        graphics.drawImage(bufferedImage, 0, 0, thumbnailWidth, thumbnailHeight, null); // 将原始图像复制到新图像中
        graphics.dispose(); // 释放资源
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "JPEG", outputStream);
        return outputStream.toByteArray();
    }
}
