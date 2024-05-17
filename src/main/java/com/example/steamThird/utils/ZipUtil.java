package com.example.steamThird.utils;

import com.example.steamThird.Bo.ZipBo;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


@Slf4j
public class ZipUtil {

    /**
     * @param folderPath             要压缩的文件夹路径
     * @param zipPath                压缩后的zip文件路径
     * @param charset                字符编码，解决中文名称乱码
     * @param propertyChangeListener 进度通知
     */
    public static void zip(String folderPath, String zipPath, String charset, PropertyChangeListener propertyChangeListener) {
        long totalSize = getTotalSize(new File(folderPath));
        try (ZipOutputStream zipOutput = new ZipOutputStream(Files.newOutputStream(Paths.get(zipPath)), Charset.forName(charset)); BufferedOutputStream output = new BufferedOutputStream(zipOutput)) {
            File folder = new File(folderPath);
            zip(zipOutput, output, folder, folder.getName(), totalSize, 0, zipPath, propertyChangeListener);
        } catch (Exception e) {
            log.error("error", e);
        }
    }

    /**
     * @param filePath               要压缩的文件路径
     * @param fileNameSet            要压缩的文件名列表
     * @param zipPath                压缩后的zip文件路径
     * @param charset                字符编码，解决中文名称乱码
     * @param propertyChangeListener 进度通知
     * @throws Exception
     */
    public static void zip(String filePath, Set<String> fileNameSet, String zipPath, String charset, ZipBo zipBo, PropertyChangeListener propertyChangeListener) {
        long totalSize = getTotalSize(filePath, fileNameSet);
        File[] files = getFiles(filePath, fileNameSet);
        try (ZipOutputStream zipOutput = new ZipOutputStream(Files.newOutputStream(Paths.get(zipPath)), Charset.forName(charset)); BufferedOutputStream output = new BufferedOutputStream(zipOutput)) {
            zip(zipOutput, output, files, null, zipPath, totalSize, 0, zipPath, zipBo, propertyChangeListener);
        } catch (Exception e) {
            log.error("error", e);
        }
    }

    private static File[] getFiles(String filePath, Set<String> fileNameSet) {
        File[] files = new File[fileNameSet.size()];
        int i = 0;
        for (String fileName : fileNameSet) {
            files[i] = new File(filePath + "/" + fileName);
            i++;
        }
        return files;
    }


    private static long zip(ZipOutputStream zipOutput, BufferedOutputStream output, File[] files, File file, String sourceName,
                            long totalSize, long readSize, String zipPath, ZipBo zipBo,
                            PropertyChangeListener propertyChangeListener) throws IOException {
        if (file == null) {
            zipBo.setZipPath(zipPath);
            zipBo.setFileTotalSize(files.length);
            if (files.length == 0) {
                zipBo.setSize(100);
                zipBo.setFileTotalSize(0);
                zipBo.setTotalSize(100);
                zipBo.setFileSize(0);
            } else {
                for (int i = 0; i < files.length; i++) {
                    zipBo.setFileSize(i + 1);
                    String path = files[i].getPath();
                    path = path.substring(path.indexOf("upload"));
                    zipBo.setPath(path);
                    readSize = zip(zipOutput, output, null, files[i], path, totalSize, readSize, zipPath, zipBo, propertyChangeListener);
                }
                zipBo.setTotalSize(100);//执行完成添加进度为100
            }
        } else {
            zipOutput.putNextEntry(new ZipEntry(sourceName));
            try (BufferedInputStream input = new BufferedInputStream(Files.newInputStream(file.toPath()))) {
                long i = 0;
                long maxSize = Files.size(file.toPath());
                byte[] b = new byte[1024];
                for (int len = input.read(b); len > 0; len = input.read(b)) {
                    output.write(b, 0, len);
                    i += len;
                    if (zipBo != null) {// 通知调用者压缩进度发生改变
                        zipBo.setSize((int) ((i * 1.0 / maxSize) * 100));
                    }
                }
            } catch (Exception e) {
                log.error("error", e);
            }
            readSize += file.length();// 累加字节长度
            Integer newSize = (int) ((readSize * 1.0 / totalSize) * 100);// 已压缩的字节大小占总字节的大小的百分比
            if (zipBo != null) {// 通知调用者压缩进度发生改变
                zipBo.setTotalSize(newSize);
            }
        }
        return readSize;
    }


    private static long zip(ZipOutputStream zipOutput, BufferedOutputStream output, File source, String sourceName, long totalSize, long readSize, String zipPath,
                            PropertyChangeListener propertyChangeListener) throws IOException {
        if (source.isDirectory()) {
            File[] flist = source.listFiles();
            assert flist != null;
            if (flist.length == 0) {
                zipOutput.putNextEntry(new ZipEntry(sourceName + "/"));
            } else {
                for (File file : flist) {
                    readSize = zip(zipOutput, output, file, sourceName + "/" + file.getName(), totalSize, readSize, zipPath, propertyChangeListener);
                }
            }
            return readSize;
        } else {
            zipOutput.putNextEntry(new ZipEntry(sourceName));
            try (BufferedInputStream input = new BufferedInputStream(Files.newInputStream(source.toPath()))) {
                byte[] b = new byte[1024];
                for (int len = input.read(b); len > 0; len = input.read(b)) {
                    output.write(b, 0, len);
                }
            } catch (Exception e) {
                log.error("error", e);
            }
            Integer oldValue = (int) ((readSize * 1.0 / totalSize) * 100);// 已压缩的字节大小占总字节的大小的百分比
            readSize += source.length();// 累加字节长度
            Integer newValue = (int) ((readSize * 1.0 / totalSize) * 100);// 已压缩的字节大小占总字节的大小的百分比
            if (propertyChangeListener != null) {// 通知调用者压缩进度发生改变
                propertyChangeListener.propertyChange(new PropertyChangeEvent(zipPath, "progress", oldValue, newValue));
            }
            return readSize;
        }
    }

    private static long getTotalSize(File file) {
        if (file.isFile()) {
            return file.length();
        }
        File[] list = file.listFiles();
        long total = 0;
        if (list != null) {
            for (File f : list) {
                total += getTotalSize(f);
            }
        }
        return total;
    }

    private static long getTotalSize(String filePath, Set<String> fileNameSet) {
        long total = 0;
        Iterator<String> it = fileNameSet.iterator();
        while (it.hasNext()) {
            String fileName = it.next();
            File file = new File(filePath + "/" + fileName);
            if (!file.exists()) {//如果是不存在的文件则删除原数组的路径
                it.remove();
            } else {
                total += file.length();
            }
        }
        return total;
    }

    /**
     * 解压
     *
     * @param zipPath                要解压的zip文件路径
     * @param targetPath             存放解压后文件的目录
     * @param charset                字符编码，解决中文名称乱码
     * @param propertyChangeListener 进度通知
     */
    public static void unzip(String zipPath, String targetPath, String charset, PropertyChangeListener propertyChangeListener) {
        long totalSize = new File(zipPath).length();// 总大小
        long readSize = 0;
        try (ZipInputStream zipInput = new ZipInputStream(Files.newInputStream(Paths.get(zipPath)), Charset.forName(charset))) {
            for (ZipEntry zipItem = zipInput.getNextEntry(); zipItem != null; zipItem = zipInput.getNextEntry()) {
                if (!zipItem.isDirectory()) {
                    File file = new File(targetPath, zipItem.getName());
                    if (!file.exists()) {
                        new File(file.getParent()).mkdirs();// 创建此文件的上级目录
                    }
                    try (BufferedOutputStream out = new BufferedOutputStream(Files.newOutputStream(file.toPath()))) {
                        byte[] b = new byte[1024];
                        for (int len = zipInput.read(b); len > 0; len = zipInput.read(b)) {
                            out.write(b, 0, len);
                        }
                    } catch (Exception e) {
                        log.error("error", e);
                    }
                    Integer oldValue = (int) ((readSize * 1.0 / totalSize) * 100);// 已解压的字节大小占总字节的大小的百分比
                    readSize += zipItem.getCompressedSize();// 累加字节长度
                    Integer newValue = (int) ((readSize * 1.0 / totalSize) * 100);// 已解压的字节大小占总字节的大小的百分比
                    if (propertyChangeListener != null) {// 通知调用者解压进度发生改变
                        propertyChangeListener.propertyChange(new PropertyChangeEvent(zipPath, "progress", oldValue, newValue));
                    }
                }
            }
        } catch (Exception e) {
            log.error("error", e);
        }
    }
}
