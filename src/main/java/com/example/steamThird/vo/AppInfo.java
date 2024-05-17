package com.example.steamThird.vo;

import lombok.Data;

import java.util.List;

@Data
public class AppInfo {

    /**
     * 标签
     */
    private List<String> label;

    /**
     * 视频url
     */
    private String videoUrl;

    /**
     * 首页图片
     */
    private String headerImg;

}