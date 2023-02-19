package com.xmg.juc;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

//各种方法测试
//wait
//join
public class MethodsTest {

    static Random random = new Random();

    static final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(10);

    //生产者线程
    static class Consumer implements Runnable {

        @Override
        public void run() {
            while (true) {
                synchronized (queue) {
                    while (queue.size() == 10) {
                        try {
                            //调用共享变量wait时,该线程被阻塞挂旗并释放锁
                            //直到其它线程调用该共享变量的notify()或notifyAll()
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(100);
                        final int i = random.nextInt();
                        queue.put(i);
                        //通知消费者
                        queue.notifyAll();
                        System.out.println("生产：" + i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //消费者线程
    static class Producer implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (queue) {
                    while (queue.size() == 0) {
                        try {
                            //调用共享变量wait时,该线程被阻塞挂起并释放锁
                            //直到其它线程调用改共享变量的notify()或notifyAll()
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(100);
                        final Integer i = queue.take();
                        System.out.println("消费" + i);
                        //通知生产者
                        queue.notifyAll();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }

        }
    }


    public static void main(String[] args) throws InterruptedException {

        /*------------------wait 阻塞当前获取该共享变量的线程， notifyAll() notify() 唤醒阻塞于该共享变量的线程---------------------*/
//        for (int i = 0; i < 1; i++) {
//            final Thread producer = new Thread(new Producer());
//            producer.start();
//        }
//        final Thread consumer = new Thread(new Consumer());
//        consumer.start();

        /*------------------- join 等待线程执行完成，调用该方法的线程会被阻塞-------------------------*/
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("执行中....");
                }

            }
        });
//        thread.start();
//        //等待thread执行完毕
//        thread.join();//此处main线程被阻塞
//        System.out.println("thread执行完成");

        /*------------------- interrupt 设置线程中断标志
        线程实际未被中断，如果线程A调用wait,jon,sleep而被阻塞挂起,
        此时B中断A线程会在这些方法抛出异常而返回，恢复到激活状态----------------------*/
        final Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("线程A执行中....");
                }
            }
        });
//        threadA.start();
//        Thread.sleep(2000);
//        当阻塞方法收到中断请求的时候就会抛出InterruptedException异常
//        //main线程设置threadA中断标志，此时sleep抛出异常线程恢复激活态,没有sleep则顺利退出
//        threadA.interrupt();

         /*------------------- setDaemon() 设置为守护线程，用户线程结束守护线程自行结束----------------------*/
        final Thread threadC = new Thread(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {

                }
            }
        });
        //设置为守护线程 守护线程是否结束不影响JVM的退出
        threadC.setDaemon(true);
        threadC.start();
        Thread.sleep(1000);
        System.out.println("main thread is over");

        /*------------------- yield()让出CPU执行权，当前线程处于就绪状态----------------------*/
    }
}
