package com.example.steamThird.utils;


import com.alibaba.fastjson2.JSONObject;

import com.example.steamThird.common.api.ApiSteamApi;
import com.example.steamThird.common.api.SteamApi;
import com.example.steamThird.vo.App;
import com.example.steamThird.vo.AppInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SteamUtils {

    @Resource
    private ApiSteamApi apiSteamApi;

    @Resource
    private SteamApi steamApi;

    @Resource(name = "asyncExecutor")
    private ThreadPoolTaskExecutor executor;

    /**
     * 获取应用程序列表
     *
     * @return {@link List}<{@link JSONObject}>
     */
    public List<App> getAppList() {
        JSONObject appList = apiSteamApi.getAppList();
        return appList.getJSONObject("applist").getList("apps", App.class);
    }


    /**
     * 获取应用程序信息
     *
     * @param id 身份证件
     * @return {@link JSONObject}
     * @throws IOException IOException
     */
    public AppInfo getAppInfo(Integer id) throws IOException {

//        // 假设你的HTML文件位于项目的根目录下，名为"example.html"
//        String filePath = "C:/Users/Administrator/Desktop/2.html"; // 你需要根据你的文件位置修改这个路径
//
//        // 使用Java NIO的Files和Paths类来读取文件
//        byte[] fileContent = Files.readAllBytes(Paths.get(filePath));
//
//        // 将字节数组转换为字符串（这里使用UTF-8编码，但你可能需要根据你的HTML文件的实际编码来调整）
//        String html = new String(fileContent, StandardCharsets.UTF_8);

        String html = steamApi.getAppId(id);

        Document doc = Jsoup.parse(html);
        AppInfo appInfo = new AppInfo();


        //下载首页图片
        CompletableFuture<String> future = downloadImageToBase64Async("https://cdn.cloudflare.steamstatic.com/steam/apps/" + id + "/header_292x136.jpg?t=1713454839");

        //获取标签
        Elements labelElement = doc.select("div#genresAndManufacturer").select("span").select("a");
        List<String> label = labelElement.stream().map(Element::text).toList();
        appInfo.setLabel(label);

        //获取视频
        String video = doc.select("div.highlight_player_item.highlight_movie").attr("data-mp4-source");
        appInfo.setVideoUrl(video);

        appInfo.setHeaderImg(future.join());


        return appInfo;
    }


    public List<String> getAppSlideshow(Integer id) {
        String html = steamApi.getAppId(id);
        Document doc = Jsoup.parse(html);
        List<CompletableFuture<String>> futures = new ArrayList<>();
        //获取轮播图
        List<String> href = doc.select("a.highlight_screenshot_link").stream().map(item -> {
            String temp = item.attr("href");
            return URLDecoder.decode(temp.substring(temp.indexOf("u=") + 1), StandardCharsets.UTF_8);
        }).collect(Collectors.toList());
        href.forEach(item -> futures.add(downloadImageToBase64Async(item)));
        href.clear();
        //循环取出线程的地址
        for (CompletableFuture<String> future : futures) {
            href.add(future.join());
        }
        return href;
    }


    private CompletableFuture<String> downloadImageToBase64Async(String imageUrl) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");

                try (BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream())) {
                    byte[] imageBytes = bis.readAllBytes(); // Java 9+
                    // 如果你的Java版本低于9，请使用Apache Commons IO的IOUtils.toByteArray(bis)

                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                    return "data:image/jpeg;base64," + base64Image;
                } finally {
                    httpURLConnection.disconnect();
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to download image", e);
            }
        }, executor.getThreadPoolExecutor());
    }
}
