package ProducerConsumer;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    
    public static void main(String[] args) {
        
        ExecutorService executorService = Executors.newCachedThreadPool();
        Exchanger<Integer> exchanger = new Exchanger<>();

        for (int i = 0; i < 2; i++) {
            executorService.execute(new LoopConsumer(exchanger));
        }

        for (int i = 0; i < 5; i++) {
            executorService.execute(new LoopProducer(exchanger));
        }

        ScheduledExecutorService scheduledExecutorService =
            Executors.newScheduledThreadPool(1);

        System.out.println("Producer - Consumer");

        scheduledExecutorService.scheduleAtFixedRate(
            () -> {
                System.out.format("%d - %d\n", 
                    LoopProducer.getCounter(),
                    LoopConsumer.getCounter()
                );
            }
            , 1, 1, TimeUnit.SECONDS);

        scheduledExecutorService.schedule(
            () -> {
                executorService.shutdownNow();
                scheduledExecutorService.shutdown();
            }, 5, TimeUnit.SECONDS);
    }

}
