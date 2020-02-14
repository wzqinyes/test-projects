package wu;

/**
 * 两个线程交替打印01010101010101......，并且每个线程执行1秒
 */
public class ThreadTT {

    static int i = 0;

    static final Object lock = new Object();

    public static void main(String[] args){

        Runnable runnable = () -> {
            synchronized (lock) {
                while (i < 100) {
                    long l1 = System.currentTimeMillis();
                    System.out.println(Thread.currentThread().getName() + ": " +  (i++ % 2) );
                    long l = System.currentTimeMillis() - l1;
                    if (l < 1000) {
                        try {
                            Thread.sleep(1000 - l);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    lock.notify();
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                lock.notifyAll();
            }
        };

        for (int j = 1; j <= 2; j++) {
            Thread thread = new Thread(runnable,"T-" + j);
            thread.start();
        }
    }
}
