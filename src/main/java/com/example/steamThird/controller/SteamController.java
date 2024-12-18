//package com.example.steamThird.controller;
//
//
//import com.alibaba.fastjson2.JSONObject;
//import com.example.steamThird.common.R;
//import com.example.steamThird.utils.SteamUtils;
//import com.example.steamThird.vo.App;
//import com.example.steamThird.vo.AppInfo;
//import javax.annotation.Resource;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//import java.net.URISyntaxException;
//import java.util.List;
//
//
//@RestController
//public class SteamController {
//
//    @Resource
//    private SteamUtils steamUtils;
//
//    @GetMapping("getAppList")
//    public R<List<App>> getAppList() {
//        return R.data(steamUtils.getAppList());
//    }
//
//    @GetMapping("getAppInfo")
//    public R<AppInfo> getAppInfo(Integer id) throws IOException, URISyntaxException {
//        return R.data(steamUtils.getAppInfo(id));
//    }
//
//    @GetMapping("getAppSlideshow")
//    public R<List<String>> getAppSlideshow(Integer id) throws IOException {
//        return R.data(steamUtils.getAppSlideshow(id));
//    }
//
//}
