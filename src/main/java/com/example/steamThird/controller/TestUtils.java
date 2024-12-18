package com.example.steamThird.controller;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;



@Slf4j
public class TestUtils {



    /**
     * 解密
     *
     * @param buf 缓冲器
     * @param key 钥匙
     * @return {@link String}
     */
    public static String qencrypt(byte[] bytes, int key) {
        for (int i = 0; i < 64; ++i) {
            key = key << 1;
            int k = ((key >> 3) & 1) ^ ((key >> 15) & 1) ^ ((key >> 23) & 1) ^ ((key >> 7) & 1);
            if (1 == k)
                key |= 1;
        }

        StringBuilder hexStringss = new StringBuilder();
        for (byte b : bytes) {
            hexStringss.append(String.format("%02x", b));
        }

        int buflen = bytes.length;
        int charlen = 8;


        for (int i = 0; i < buflen; ++i) {
            byte mkey = 0;
            for (int j = 0; j < charlen; ++j) {
                if (1 == ((key >> 31) & 1))
                    mkey |= 1;
                mkey = (byte) (mkey << 1);
                key = key << 1;
                int k = ((key >> 3) & 1) ^ ((key >> 15) & 1) ^ ((key >> 23) & 1) ^ ((key >> 7) & 1);
                if (1 == k)
                    key = key | 1;
            }
            bytes[i] ^= mkey;
        }
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }



    public static String spaced(String str) {
        // 每两个字符之间添加空格
        StringBuilder spacedHex = new StringBuilder();
        for (int i = 0; i < str.length(); i += 2) {
            if (i > 0) {
                spacedHex.append(" ");
            }
            spacedHex.append(str.substring(i, Math.min(i + 2, str.length())));
        }
        return spacedHex.toString();
    }


    public static String dec(byte[] bytes) {
        // 将有符号字节转换为无符号整数数组
        int[] unsignedBytes = new int[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            unsignedBytes[i] = bytes[i] & 0xFF;
        }
        // 将无符号整数数组转换为字符串
        StringBuilder sb = new StringBuilder();
        for (int b : unsignedBytes) {
            sb.append((char) b);
        }
        return sb.toString();
    }

    public static byte[] decB(String str) {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        // 将字符串转换回无符号整数数组
        int[] newUnsignedBytes = new int[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            newUnsignedBytes[i] = (char) bytes[i];
        }

        // 将无符号整数数组转换为有符号字节数组
        byte[] newBytes = new byte[newUnsignedBytes.length];
        for (int i = 0; i < newUnsignedBytes.length; i++) {
            newBytes[i] = (byte) newUnsignedBytes[i];
        }
        return newBytes;
    }

    /**
     * 加密
     *
     * @param buf 缓冲器
     * @param key 钥匙
     * @return {@link String}
     */
    public static String qencrypt(String buf, int key) {
        for (int i = 0; i < 64; ++i) {
            key = key << 1;
            int k = ((key >> 3) & 1) ^ ((key >> 15) & 1) ^ ((key >> 23) & 1) ^ ((key >> 7) & 1);
            if (1 == k)
                key |= 1;
        }

        byte[] bytes = decB(buf);
        StringBuilder hexStringss = new StringBuilder();
        for (byte b : bytes) {
            hexStringss.append(String.format("%02x", b));
        }
        log.info("源数据的hex：{}\n源数据的数组：{}\n源数据的文本：{}", hexStringss, bytes, buf);
        int buflen = bytes.length;
        System.out.println(buflen);
        int charlen = 8;


        for (int i = 0; i < buflen; ++i) {
            byte mkey = 0;
            for (int j = 0; j < charlen; ++j) {
                if (1 == ((key >> 31) & 1))
                    mkey |= 1;
                mkey = (byte) (mkey << 1);
                key = key << 1;
                int k = ((key >> 3) & 1) ^ ((key >> 15) & 1) ^ ((key >> 23) & 1) ^ ((key >> 7) & 1);
                if (1 == k)
                    key = key | 1;
            }
            bytes[i] ^= mkey;
        }
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        log.info("普通偏移的hex：{}\n普通偏移的数组：{}\n普通偏移后的文本：{}", hexString, bytes, dec(bytes));
        return hexString.toString();
    }


}
