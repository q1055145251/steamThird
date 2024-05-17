package com.example.steamThird.controller;


import com.alibaba.fastjson2.JSONObject;
import com.example.steamThird.common.R;
import com.example.steamThird.utils.SteamUtils;
import com.example.steamThird.vo.App;
import com.example.steamThird.vo.AppInfo;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;


@ApiSupport(order = 1)
@RestController
public class SteamController {

    @Resource
    private SteamUtils steamUtils;

    @GetMapping("getAppList")
    public R<List<App>> getAppList() {
        return R.data(steamUtils.getAppList());
    }

    @GetMapping("getAppInfo")
    public R<AppInfo> getAppInfo(Integer id) throws IOException {
        return R.data(steamUtils.getAppInfo(id));
    }

    @GetMapping("getAppSlideshow")
    public R<List<String>> getAppSlideshow(Integer id) throws IOException {
        return R.data(steamUtils.getAppSlideshow(id));
    }

}
