package org.example.config;

import lombok.NonNull;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author makui
 * @created 2024/4/23
 **/
public class BasicConfig {
    private static final AtomicInteger threadNumber = new AtomicInteger(1);
    public final static ThreadPoolExecutor executor = new ThreadPoolExecutor(
            10,
            10,
            1, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(),
            new ThreadFactory() {
                @Override
                public Thread newThread(@NonNull Runnable r) {
                    Thread t = new Thread(r, "myThread-" + threadNumber.getAndIncrement());
                    if (t.isDaemon())
                        t.setDaemon(false);
                    if (t.getPriority() != Thread.NORM_PRIORITY)
                        t.setPriority(Thread.NORM_PRIORITY);
                    return t;
                }
            },
            new ThreadPoolExecutor.CallerRunsPolicy()
    );
}
