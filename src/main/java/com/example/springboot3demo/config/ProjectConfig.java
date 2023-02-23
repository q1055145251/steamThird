package com.example.springboot3demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 读取项目相关配置
 *
 * @author ruoyi
 */
@SuppressWarnings("unused")
@Configuration
@ConfigurationProperties(prefix = "config")
public class ProjectConfig {

    //ERROR处理配置
    private static Boolean debug;

    public static boolean isDebug() {
        return debug;
    }

}
