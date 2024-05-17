package com.example.steamThird.common.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 第三方授权配置相关api
 *
 * @author Administrator
 * @date 2024/03/12
 */
@FeignClient(url = "https://store.steampowered.com", name = "steamClient")
public interface SteamApi {


    @GetMapping(value = "/app/{id}")
    String getAppId(@PathVariable("id") Integer id);
}
