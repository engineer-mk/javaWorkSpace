package xmg.xmg.juc;

import java.util.concurrent.*;

//线程创建的四种方式
public class ThreadCreateTest {
    public static void main(String[] args) {

        //1.继承Thread
        final ThreadA threadA = new ThreadA();
        //threadA.start();

        //2.实现Runnable接口
        final Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("threadB....." + Thread.currentThread());
            }
        },"threadB");
        //threadB.start();

        //3.FutureTask + Callable方式
        final FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return "hello " + Thread.currentThread().getName();
            }
        });
        final Thread threadC = new Thread(futureTask,"threadC");
        threadC.start();
        try {
            System.out.println(futureTask.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        //4.线程池
        final Future<String> future = executor.submit(new Callable<String>() {
            @Override
            public String call() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return "hello " + Thread.currentThread().getName() +" isDaemon? "+ Thread.currentThread().isDaemon();
            }
        });
        try {
            System.out.println(future.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Thread.currentThread().getName()+" " +Thread.currentThread().isDaemon());
    }

    private final static ThreadPoolExecutor executor = new ThreadPoolExecutor(
            10,
            10,
            1, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>());
    private final static Executor cachedThreadPool = Executors.newCachedThreadPool();


    static class ThreadA extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("threadA....." + Thread.currentThread());
        }

        public ThreadA() {
            super("threadA");
        }
    }

}
