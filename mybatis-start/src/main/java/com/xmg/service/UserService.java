package com.xmg.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ScheduledFuture;

/**
 * @author makui
 * @created on  2023/2/19
 **/
@Service
@RequiredArgsConstructor
public class UserService implements InitializingBean {
    private final TaskScheduler taskScheduler;
    private static int minute = 43;
    private static int hour = 22;
    private ScheduledFuture<?> task;

    public void task() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        final LocalDateTime now = LocalDateTime.now();
        System.out.println(now.format(format));
        System.out.println("------------------");
    }

    public void cancel() {
        this.task.cancel(true);
    }

    public void updateTask(int minute, int hour) {
        UserService.minute = minute;
        UserService.hour = hour;
        cancel();
        createScheduler();
    }

    public void createScheduler() {
        String str = "0 " + minute + " " + hour + " * * ?";
        CronTrigger t = new CronTrigger(str);
        task = taskScheduler.schedule(this::task, t);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        createScheduler();
    }

    public static void main(String[] args) {

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        final LocalDateTime now = LocalDateTime.now();
        System.out.println(now.format(format));
    }
}
