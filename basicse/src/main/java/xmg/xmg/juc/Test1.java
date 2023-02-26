package xmg.xmg.juc;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

/**
 * @author makui
 * @created 2022/3/23
 * <p>
 * 指令集重排序问题
 **/
public class Test1 {
    private static int num = 0;
    private static boolean ready = false;

    static class ReadThread extends Thread {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                if (ready) {
                    System.out.println(num + num);
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    static class WriteThread extends Thread {
        @Override
        public void run() {
            num = 2;
            //ready加上volatile可以保证ready=ture 不会被重排序到num=2 之前
            ready = true;
            System.out.println("writeThread set over...");
        }
    }

    @RepeatedTest(10)
    @Test
    public void test() throws InterruptedException {
        final ReadThread readThread = new ReadThread();
        readThread.start();
        final WriteThread writeThread = new WriteThread();
        writeThread.start();
    }


    //中断异常
    public static void main(String[] args) {
        final Thread thread = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("线程中断");
                    break;
                }
                try {
                    Thread.sleep(1000);
                    System.out.println("---");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
//        try {
//            Thread.sleep(10);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        //happens-before规则 保证该操作优先与52行的中断检测操作优先执行
        //类似由于没有happens-before规则 notifyAll就会比wait先执行
        thread.interrupt();
    }
}
