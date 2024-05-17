package com.example.steamThird.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@Order(1)
@Slf4j
@EnableAsync
public class ExecutorConfig {
    /**
     * 核心线程数量，默认cpu核心数量
     */
    @Value("${executor.corePoolSize}")
    private int CORE_POOL_SIZE = 0;

    /**
     * 最大线程数量，默认cpu核心数量两倍;
     */
    @Value("${executor.maxPoolSize}")
    private int MAX_POOL_SIZE = 0;

    /**
     * 空闲线程存活时间 默认60秒
     */
    @Value("${executor.keepAliveSeconds}")
    private int KEEP_ALIVE_SECONDS = 60;

    /**
     * 线程阻塞队列容量,默认100
     */
    @Value("${executor.queueCapacity}")
    private int QUEUE_CAPACITY = 100;

    /**
     * 是否允许核心线程超时 默认否
     */
    @Value("${executor.allowCoreThreadTimeout}")
    private boolean ALLOW_CORE_THREAD_TIMEOUT = false;


    @Bean("asyncExecutor")
    public ThreadPoolTaskExecutor asyncServiceExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int corePoolSize = Runtime.getRuntime().availableProcessors();//系统cpu核心数
        //配置核心线程数
        if (CORE_POOL_SIZE == 0) {
            executor.setCorePoolSize(corePoolSize);
        } else {
            executor.setCorePoolSize(CORE_POOL_SIZE);

        }
        //配置最大线程数
        if (MAX_POOL_SIZE == 0) {
            executor.setMaxPoolSize(corePoolSize * 2);
        } else {
            executor.setMaxPoolSize(MAX_POOL_SIZE);

        }
        //配置队列大小
        executor.setQueueCapacity(QUEUE_CAPACITY);
        //配置超时时间
        executor.setKeepAliveSeconds(KEEP_ALIVE_SECONDS);
        //配置是否允许核心线程超时
        executor.setAllowCoreThreadTimeOut(ALLOW_CORE_THREAD_TIMEOUT);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("logThread-");
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务

        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        log.info(String.valueOf(executor));
        return executor;
    }
}