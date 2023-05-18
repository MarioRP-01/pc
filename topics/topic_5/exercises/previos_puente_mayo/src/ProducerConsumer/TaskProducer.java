package ProducerConsumer;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TaskProducer implements Runnable {

    private ExecutorService executorService;

    private static Integer counter = 0;
    private static Lock lockCounter = new ReentrantLock();  
    
    private Exchanger<Integer> exchanger;
    private static Lock lockExchanger = new ReentrantLock();

    TaskProducer(ExecutorService executorService, Exchanger<Integer> exchanger) {
        this.executorService = executorService;
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        try {
            produce();
        } catch (InterruptedException e) {
            return;
        }
        executorService.execute(this);}
    
    public static Integer getCounter() {
        return counter;
    }

    private void produce() throws InterruptedException {
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
