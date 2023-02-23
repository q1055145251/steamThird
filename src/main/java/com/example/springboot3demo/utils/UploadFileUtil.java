package com.example.springboot3demo.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;

/**
 * 文件上传
 */
@Component
public class UploadFileUtil {

    /**
     * 上传文件
     *
     * @param multipartFile 文件对象
     * @param dir           上传目录
     * @return 路径
     */
    public static boolean uploadFile(MultipartFile multipartFile, String dir, String fileName) {
        try {
            if (multipartFile.isEmpty()) {
                return false;
            }
            // 根路径，在 resources/static/upload
            String basePath = dir;
            basePath = URLDecoder.decode(basePath, "UTF-8");
            // 创建新的文件
            File fileExist = new File(basePath);
            // 文件夹不存在，则新建
            if (!fileExist.exists()) {
                //noinspection ResultOfMethodCallIgnored
                fileExist.mkdirs();
            }
            // 完成文件的上传
            try {
                InputStream inputStream = multipartFile.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(basePath + fileName);
                int read;
                byte[] bytes = new byte[1024];
                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 获取文件对象
            File file = new File(basePath, fileName);
            file.setWritable(false);
            // 返回绝对路径
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}

