package ProducerConsumer;

import java.util.concurrent.Exchanger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LoopProducer implements Runnable {

    private static int counter = 0;
    private static final Lock lockCounter = new ReentrantLock();

    private final Exchanger<Integer> exchanger;
    private static final Lock lockExchanger = new ReentrantLock();

    LoopProducer(Exchanger<Integer> exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                produce();
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public static int getCounter() {
        return counter;
    }

    public void produce() throws InterruptedException {
        lockExchanger.lock();
        try {
            exchanger.exchange(1);
            lockCounter.lock();
            counter++;
            lockCounter.unlock();
        } 
        finally {
            lockExchanger.unlock();
        }
    }
}
