package cn;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.CountDownLatch;

public class RedissonTest {

    static RedissonClient redisson;
    static final String LOCK_NAME = "MY_LOCK";

    static {
        Config config = new Config();
        config.useClusterServers().addNodeAddress("redis://192.168.153.130:7001","redis://192.168.153.130:7002","redis://192.168.153.130:7003");
        //config.useSingleServer().setAddress("redis://192.168.153.130:6379").setPassword("123456");
        redisson = Redisson.create(config);
    }

    //加锁
    public static void lock(){
        RLock lock = redisson.getLock(LOCK_NAME);
        lock.lock();   //如果发生异常可能死锁
        //lock.lock(2, TimeUnit.SECONDS);   //设置锁过期时间为5秒，防止死锁
    }

    //释放锁
    public static void unlock(){
        RLock lock = redisson.getLock(LOCK_NAME);
        lock.unlock();
    }

    public static void main(String[] args) throws InterruptedException {
        int len = 1000;
        CountDownLatch countDownLatch = new CountDownLatch(len);
        System.out.println("start........");
        for (int i = 0; i < len; i++) {
            new Thread(() -> {
                addCount();
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        System.out.println(count);
        System.out.println("end........");
    }

    static int count;

    static void addCount() {
        lock();
        System.out.println(Thread.currentThread().getName() + ".. start");
        count ++;
        System.out.println(Thread.currentThread().getName() + ".. end");
        unlock();
    }
}
