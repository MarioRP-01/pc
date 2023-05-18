package ProducerConsumer;

import java.util.concurrent.Exchanger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LoopConsumer implements Runnable {

    private static Integer counter = 0;
    private static final Lock lockCounter = new ReentrantLock();
    private Exchanger<Integer> exchanger;
    private static final Lock lockExchanger = new ReentrantLock();

    LoopConsumer(Exchanger<Integer> exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                consume();
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public static Integer getCounter() {
        return counter;
    }

    private void consume() throws InterruptedException {
        lockExchanger.lock();
        try {
            exchanger.exchange(null);
            lockCounter.lock();
            counter++;
            lockCounter.unlock();
        } 
        finally {
            lockExchanger.unlock();
        }
    }
}
