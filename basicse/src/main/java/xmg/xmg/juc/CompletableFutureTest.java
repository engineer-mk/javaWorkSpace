package xmg.xmg.juc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CompletableFutureTest {
    private final static ThreadPoolExecutor executor = new ThreadPoolExecutor(
            10,
            10,
            1, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );
    //创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程
    private final static Executor cachedThreadPool = Executors.newCachedThreadPool();
    //创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待
    private final static Executor fixedThreadPool = Executors.newFixedThreadPool(10);
    //创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序执行
    private final static Executor singleThreadExecutor = Executors.newSingleThreadExecutor();
    //创建一个定长线程池，支持定时及周期性任务执行。
    private static final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

    @Test
    @DisplayName("CompletableFuture基本用法")
    public void test0() throws InterruptedException, ExecutionException {
        //不指定线程池，则使用ForkJoinPool.commonPool()
        final CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
                    System.out.println(Thread.currentThread().getName() + " 守护线程？" + Thread.currentThread().isDaemon());
                    return 2;
                }, executor)
                .thenApplyAsync(it -> {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + "守护线程？？" + Thread.currentThread().isDaemon());
                    int i = 10 / 0;
                    return it + 1;
                }, executor)
                .handleAsync((it,throwable) -> {
                    System.out.println(Thread.currentThread().getName() + " 守护线程？？？" + Thread.currentThread().isDaemon());
                    if (throwable != null) {
                        return 1;
                    }else {
                        return it + 1;
                    }
                }, executor)
                .thenCombineAsync(CompletableFuture.supplyAsync(() -> 100),
                        (a, b) -> {
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println(Thread.currentThread().getName() + " 守护线程？？？？" + Thread.currentThread().isDaemon());
                            return a + b;
                        }, executor)
                .whenComplete((r, e) -> System.out.println(r))
                .exceptionally(throwable -> {
                    throwable.printStackTrace();
                    return 0;
                });

        completableFuture.join();
//        completableFuture.get();
    }


    @Test
    @DisplayName("CompletableFuture组合做个异步任务")
    public void test1() {
        final CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("守护线程？" + Thread.currentThread().isDaemon());
            return 1;
        });
        final CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(() -> 2);
        final CompletableFuture<Integer> f3 = CompletableFuture.supplyAsync(() -> 3);
        CompletableFuture.allOf(f1, f2, f3)
                .whenComplete(new BiConsumer<Void, Throwable>() {
                    @Override
                    public void accept(Void unused, Throwable throwable) {
                        try {
                            System.out.println("守护线程？？" + Thread.currentThread().isDaemon());
                            System.out.println(f1.get() + f2.get() + f3.get());
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }


    @Test
    @DisplayName("CompletableFuture组合做个异步任务")
    public void test2() throws ExecutionException, InterruptedException {
        final long start = System.currentTimeMillis();
        AtomicInteger sum = new AtomicInteger();
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            //放入线程池中则为非守护线程，否则是守护线程
            System.out.println("守护线程？" + Thread.currentThread().isDaemon());
            sum.addAndGet(6);
            return 6;
        }, executor);
        for (int i = 0; i < 5; i++) {
            final int r = i;
            future = future.thenCombineAsync(CompletableFuture.supplyAsync(() -> {
                System.out.println("守护线程??" + Thread.currentThread().isDaemon());
                final long s = (r + 1) * 1000;
                try {
                    Thread.sleep(s);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return r;
            }, executor), (a, b) -> {
                System.out.println("守护线程???" + Thread.currentThread().isDaemon());
                sum.addAndGet(b);
                return a + b;
            }, executor);
        }
        final Integer result = future.get();
        final long end = System.currentTimeMillis();
        System.out.println("耗时:" + (start - end));
        System.out.println(result);
        System.out.println(sum.get());

    }

    @Test
    @DisplayName("CompletableFuture组合做个异步任务Stream流式编程")
    public void test3() {
        final long start = System.currentTimeMillis();
        final ArrayList<Supplier<Integer>> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            list.add(() -> {
                try {
                    Thread.sleep(1000 * finalI);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return ThreadLocalRandom.current().nextInt(90, 100);
            });
        }

        final List<Integer> result = list.stream().map(supplier -> CompletableFuture.supplyAsync(supplier,executor))
                .collect(Collectors.toList())//此处不能省略,否则变成同步
                .stream().map(CompletableFuture::join)
                .collect(Collectors.toList());
        System.out.println(result);
        System.out.println(System.currentTimeMillis() - start);
    }


    @Test
    @DisplayName("applyToEither 谁快返回谁")
    public void test4(){
        final CompletableFuture<Integer> futureA = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 1;
        });
        final CompletableFuture<Integer> futureB = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 2;
        });
        final CompletableFuture<Integer> future = futureB.applyToEither(futureA, r -> {
            System.out.println(r);
            return r;
        });
        future.join();
    }
}
