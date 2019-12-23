package cn.executor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class ExecutorServiceTest {

    public static void main(String[] args) throws Exception {

        ThreadFactory threadFactory = new ThreadFactory() {
            private final AtomicLong count = new AtomicLong();
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(null, r, "demoThread-" + count.incrementAndGet());
            }
        };

        //corePoolSize 最小核心线程5
        //maximumPoolSize 最大允许线程数20
        //keepAliveTime 池中总线程数大于核心线程数时，空闲线程等待1分钟（若依然空闲）被回收；
        //unit 对keepAliveTime的时间单位，这里是分钟
        //workQueue 等待分配线程的任务队列；如果无可用线程，任务会被塞进队列；如果队列已满，则如果继续加任务；
        //          每一个被执行的任务会被踢出队列，队列长度减1
        //threadFactory 线程工厂，默认有Executors.defaultThreadFactory()
        ExecutorService executorService = new ThreadPoolExecutor(
            5, 20, 1L, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(2000), threadFactory);

        //execute方法是void类型
        executorService.execute(() -> System.out.println(Thread.currentThread().getName()));

        //submit方式是Future类型
        Future<?> future = executorService.submit(() -> System.out.println(Thread.currentThread().getName()));
        Future<?> future1 = executorService.submit(() -> System.out.println(Thread.currentThread().getName()), "aaa");

        Object o = future.get();
        System.out.println(o);

        Object o1 = future1.get(3, TimeUnit.SECONDS);
        System.out.println(o1);

        executorService.shutdown();
    }
}
