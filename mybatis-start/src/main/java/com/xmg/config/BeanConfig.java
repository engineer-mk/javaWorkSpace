package com.xmg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author makui
 * @created on  2023/2/19
 **/
@Configuration
public class BeanConfig {
    @Bean
    public TaskScheduler taskScheduler() {
        final ConcurrentTaskScheduler concurrentTaskScheduler = new ConcurrentTaskScheduler();
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.setRemoveOnCancelPolicy(true);
        concurrentTaskScheduler.setScheduledExecutor(executor);
        return concurrentTaskScheduler;
    }
}
