package com.example.steamThird.utils;


import com.alibaba.fastjson2.JSONObject;

import com.example.steamThird.common.api.ApiSteamApi;
import com.example.steamThird.common.api.SteamApi;
import com.example.steamThird.vo.App;
import com.example.steamThird.vo.AppInfo;

import javax.annotation.Resource;

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
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
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
     * cookie 设置中文以及生日时间
     */
    private static final String COOKIE = "Steam_Language=schinese;birthtime=-1861948799;";

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
    public AppInfo getAppInfo(Integer id) throws IOException, URISyntaxException {

//        String html = steamApi.getTest(new URI("http://steam.yossqv2.top"), "game.html");
//        String html = steamApi.getTest(new URI("http://steam.yossqv2.top"), "old_game.html");
        String html = steamApi.getAppId(COOKIE, id);
        Document doc = Jsoup.parse(html);
        AppInfo appInfo = new AppInfo();

        //下载首页图片
        CompletableFuture<String> future = downloadImageToBase64Async("https://cdn.cloudflare.steamstatic.com/steam/apps/" + id + "/header_292x136.jpg?t=1713454839");

        //获取游戏配置
        Element configElement = doc.select("div.game_area_sys_req.sysreq_content.active").first();
        if (configElement != null) {
            appInfo.setConfig(configElement.toString());
        }

        //获取标签
        Elements labelElement = doc.select("div#genresAndManufacturer").select("span").select("a");
        List<String> label = labelElement.stream().map(Element::text).toList();
        appInfo.setLabel(label);

        //获取游戏简述
        String text = doc.select("div.game_description_snippet").text();
        appInfo.setSketch(text);

        //获取发行时间
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy 年 M 月 d 日");
        long issueTimestamp = LocalDate.parse(doc.select("div.date").text(), dtf).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        appInfo.setIssueTimestamp(issueTimestamp);
        //获取视频
        String video = doc.select("div.highlight_player_item.highlight_movie").attr("data-mp4-source");
        appInfo.setVideoUrl(video);

        appInfo.setHeaderImg(future.join());


        return appInfo;
    }


    public List<String> getAppSlideshow(Integer id) {
        String html = steamApi.getAppId(COOKIE, id);
        Document doc = Jsoup.parse(html);
        List<CompletableFuture<String>> futures = new ArrayList<>();
        //获取轮播图
        List<String> href = doc.select("a.highlight_screenshot_link").stream().map(item -> {
            String temp = item.attr("href");
            return URLDecoder.decode(temp.substring(temp.indexOf("u=") + 1), StandardCharsets.UTF_8);
        }).collect(Collectors.toList());
        href = href.size() > 5 ? href.subList(0, 5) : href;
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
