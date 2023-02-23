package com.example.springboot3demo.utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.Random;

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

        } catch (SocketTimeoutException exception) {
            System.out.println("SocketTimeoutException " + hostName + ":" + port + ". " + exception.getMessage());
        } catch (IOException exception) {
            System.out.println(
                    "IOException - Unable to connect to " + hostName + ":" + port + ". " + exception.getMessage());
        }
        return isAlive;
    }


    public static Object getObjectFieldResult(Object object, String name) {
        try {
            Class<?> aClass = object.getClass();
            Field field = aClass.getDeclaredField(name);
            field.setAccessible(true);
            return field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }


}
