//package com.xmg.reactor;
//
//import java.util.Random;
//import java.util.concurrent.Flow;
//import java.util.concurrent.SubmissionPublisher;
//
//public class SimpleDemo {
//    public static void main(String[] args) {
//        final Random random = new Random();
//        //发布者
//        SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();
//        //订阅者
//        Flow.Subscriber<Integer> subscriber = new Flow.Subscriber<>() {
//            Flow.Subscription subscription;
//
//            @Override
//            public void onSubscribe(Flow.Subscription subscription) {
//                this.subscription = subscription;
//                System.out.println("建立订阅关系");
//                subscription.request(1);
//            }
//
//            @Override
//            public void onNext(Integer item) {
//                System.out.println("接收到数据:" + item);
//                //处理业务
//                //通知上游继续发布数据
//                subscription.request(1);
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                System.out.println("发生异常");
//                throwable.printStackTrace();
//            }
//
//            @Override
//            public void onComplete() {
//                System.out.println("数据接受完成");
//            }
//        };
//        // 建立订阅关系
//        publisher.subscribe(subscriber);
//        while (true) {
//            //发布数据
//            publisher.submit(random.nextInt(100));
//        }
//
//    }
//}
