package com.example.steamThird.common.api;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 第三方授权配置相关api
 *
 * @author Administrator
 * @date 2024/03/12
 */
@FeignClient(url = "https://api.steampowered.com", name = "apiSteamClient")
public interface ApiSteamApi {


    /**
     * 获取应用程序列表
     *
     * @return {@link String}
     */
    @GetMapping(value = "/ISteamApps/GetAppList/v2/")
    JSONObject getAppList();
}
