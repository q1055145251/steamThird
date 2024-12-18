package com.example.steamThird;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jcajce.provider.asymmetric.RSA;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Scheduled;

import javax.xml.bind.DatatypeConverter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.CRC32;

@SpringBootTest
@Slf4j
class MainApplicationTests {



    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入一些内容，输入'exit'结束：");

        String hexBufString = scanner.nextLine();
        byte[] buffer = DatatypeConverter.parseHexBinary(hexBufString.replace(" ", ""));
        buffer = Arrays.copyOfRange(buffer, 10, buffer.length);

        //解密密钥
        int key = buffer[1] & 0xFF;

        System.out.println(key);

        //加密后内容
        byte[] decValue = Arrays.copyOfRange(buffer, 2, buffer.length);

        log.info("bytesRead: {};\nhex:{}", buffer, hex(buffer));

        log.info(hex(decValue));

        byte[] qencrypt = qencrypt(decValue, key);
        qencrypt=DatatypeConverter.parseHexBinary("01"+hex(intToBytes(Integer.parseInt(new String(qencrypt)))).replace(" ", ""));
        qencrypt(qencrypt, key);

        CRC32 crc32 = new CRC32();
        crc32.update(qencrypt);

        // 如果需要固定长度的十六进制字符串，可以添加前导零
        String hexChecksum = String.format("%08X", crc32.getValue());
        qencrypt=DatatypeConverter.parseHexBinary(("57 53"+ hex(intToBytes(qencrypt.length)) +hexChecksum+hex(qencrypt)).replace(" ", ""));
        log.info("结果:{}", hex(qencrypt));

        log.info("{}",login(key));
    }


    public static String login(int key){
        byte[] qencrypt=DatatypeConverter.parseHexBinary(("0202"+hex(JSONObject.of("Userid","001","Password","12345678","Type",1).toJSONString().getBytes())).replace(" ", ""));
        qencrypt(qencrypt, key);

        CRC32 crc32 = new CRC32();
        crc32.update(qencrypt);
        String hexChecksum = String.format("%08X", crc32.getValue());
        qencrypt=DatatypeConverter.parseHexBinary(("57 53"+ hex(intToBytes(qencrypt.length)) +hexChecksum+hex(qencrypt)).replace(" ", ""));
        return hex(qencrypt);
    }


    /**
     * 将 int 类型转换为 byte 数组
     *
     * @param value 要转换的 int 值
     * @return 包含 int 值的 byte 数组
     */
    public static byte[] intToBytes(int value) {
        return new byte[]{
                (byte) (value >>> 24),
                (byte) (value >>> 16),
                (byte) (value >>> 8),
                (byte) value};
    }

    /**
     * 把字节数组转换成整数
     *
     * @param bytes 一个长度为4的字节数组
     * @return 转换得到的整数值
     */
    private static int bytesToInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).asIntBuffer().get();
    }

    //    @Test
    public static void main2(String[] args) {

        String serverAddress = "172.30.254.87"; // 服务器的IP地址
        int serverPort = 6671; // 服务器的端口号

        try {
            // 创建Socket对象，指定服务器地址和端口号
//            Socket socket = new Socket(serverAddress, serverPort);

//            // 获取输出流，用于向服务器发送数据
//            OutputStream outputStream = socket.getOutputStream();
//
//            // 向服务器发送数据
//            String message = "Hello, TCP Server!";
//            outputStream.write(message.getBytes());

            // 获取输入流，用于接收服务器返回的数据
//            InputStream inputStream = socket.getInputStream();
            //报文头
//            byte[] headerBuf = new byte[10];
//            inputStream.read(headerBuf, 0, 10);
            //报文长度
//            int bufferSize = ByteBuffer.wrap(headerBuf, 2, 4).getInt();
            // 报文内容
//            byte[] buffer = new byte[bufferSize];
//            int bytesRead = inputStream.read(buffer, 0, bufferSize);
            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入一些内容，输入'exit'结束：");
            while (true) {
                String hexBufString = scanner.nextLine();
                byte[] buffer = DatatypeConverter.parseHexBinary(hexBufString.replace(" ", ""));
                buffer = Arrays.copyOfRange(buffer, 10, buffer.length);

                //解密密钥
                int key = buffer[1] & 0xFF;

                //加密后内容
                byte[] decValue = Arrays.copyOfRange(buffer, 2, buffer.length);

                log.info("bytesRead: {};\nhex:{}", buffer, hex(buffer));

                byte[] qencrypt = qencrypt(decValue, key);

                log.info("解密后：{}", bytesToInt(qencrypt));
                String hexString = "01" + String.format("%08X", bytesToInt(qencrypt));
                hexString = spaced(hexString);
                System.out.println(hexString);
                byte[] bytes = DatatypeConverter.parseHexBinary(hexString.replace(" ", ""));
                CRC32 crc32 = new CRC32();
                crc32.update(bytes);

                // 如果需要固定长度的十六进制字符串，可以添加前导零
                String hexChecksum = String.format("%08X", crc32.getValue());

                hexChecksum = spaced(hexChecksum);


                String sendString = "57 53 00 00 00 05 " + hexChecksum + " " + hexString;

                log.info("解密后原文：{}\n转整数后转16进制:{}\nCRC校验值:{}", qencrypt, sendString, hexChecksum);
                System.out.println(sendString);
            }


            // 关闭连接
//            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //    @Test



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

    public static byte[] decBd(String str) {
        // 将字符串转换回无符号整数数组
        int[] newUnsignedBytes = new int[str.length()];
        for (int i = 0; i < str.length(); i++) {
            newUnsignedBytes[i] = str.charAt(i);
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
        return new String(bytes, StandardCharsets.UTF_8);
    }


    public static String hex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b)).append(" ");
        }
        return hexString.toString();
    }


    /**
     * 解密
     *
     * @param buf 缓冲器
     * @param key 钥匙
     * @return {@link String}
     */
    public static byte[] qencrypt(byte[] bytes, int key) {
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
//        log.info("源数据的hex：{}\n源数据的数组：{}\n源数据的文本：{}", hexStringss, bytes, dec(bytes));
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
//        log.info("普通偏移的hex：{}\n普通偏移的数组：{}\n普通偏移后的文本：{}", hexString, bytes, new String(bytes, StandardCharsets.UTF_8));
        return bytes;
    }

}
