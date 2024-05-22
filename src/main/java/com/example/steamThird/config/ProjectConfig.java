package com.example.steamThird.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 读取项目相关配置
 *
 * @author ruoyi
 */
@SuppressWarnings("unused")
@Configuration
public class ProjectConfig {

    //ERROR处理配置
    private static Boolean debug=false;

    public static boolean isDebug() {
        return debug;
    }

}
