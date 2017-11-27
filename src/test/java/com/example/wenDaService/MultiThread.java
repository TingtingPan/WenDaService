package com.example.wenDaService;

import java.util.concurrent.*;

/***总结：
 * 1、Thread、Runnable、
 * 2、BlockingQueue
 * 3、ThreadLocal
 * 4、Executor：任务框架
 * 5、Atomic类
 * 6、Future
 */

/***Executor：
 * 1、提供一个运行任务的框架
 * 2、将任务和如何运行任务解耦
 * 3、常用于提供线程池或定时任务服务
 */

/***Future：
 * 1、返回异步结果
 * 2、阻塞等待返回结果
 * 3、timeout
 * 4、获取线程中的exception
 */
class MyThread extends Thread {
    private int tid;

    public MyThread(int tid) {
//        super(name);
        this.tid = tid;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(1000);
                System.out.println(String.format("%d:%d", tid, i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

//消费者线程
class Consumer implements Runnable {
    BlockingQueue<String> q;

    public Consumer(BlockingQueue<String> q) {
        this.q = q;
    }

    @Override
    public void run() {
        try {
            while (true) {
                System.out.println(Thread.currentThread().getName() + ":" + q.take());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Producer implements Runnable {
    BlockingQueue<String> q;

    public Producer(BlockingQueue<String> q) {
        this.q = q;
    }

    @Override
    public void run() {
        try {
            while (true) {
                //生产工作
                for (int i = 0; i < 100; i++) {
                    Thread.sleep(1000);
                    q.put(String.valueOf(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public class MultiThread {
    public static void testThread() {
        for (int i = 0; i < 10; i++) {
            new MyThread(i).start();
        }
    }

    public static void main(String[] args) {
//       testThread();
//        testBlockingQueue();
//        testThreadLocal();
//        testExecutor();
        testFuture();
    }





    public static void testBlockingQueue() {
        BlockingQueue<String> q = new ArrayBlockingQueue<String>(10);
        new Thread(new Producer(q)).start();
        new Thread(new Consumer(q)).start();
        new Thread(new Consumer(q)).start();
    }

    private static int userId;
    private static ThreadLocal<Integer> threadLocalUserId = new ThreadLocal<>();

    public static void testThreadLocal() {
        for (int i = 0; i < 10; i++) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    threadLocalUserId.set(finalI);
                    try {
                        Thread.sleep(1000);
                        System.out.println("ThreadLocal:" + threadLocalUserId.get());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static void testExecutor() {
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(1000);
                        System.out.println("Executor1:" + i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        service.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(1000);
                        System.out.println("Executor2:" + i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }
    public static void testFuture(){
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Integer> future = service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Thread.sleep(1000);
                return 1;
//                return null;
            }
        });
        try{
            System.out.println(future.get());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
