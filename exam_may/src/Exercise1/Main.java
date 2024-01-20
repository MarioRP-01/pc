package Exercise1;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 
 */
public class Main {

    public static void main(String[] args) {

        Exchanger<Integer> exchangerProducer = new Exchanger<>();
        Exchanger<Float> exchangerConsumer = new Exchanger<>();

        ExecutorService executorService = 
            Executors.newFixedThreadPool(3);

        executorService.execute(new Producer(exchangerProducer));
        executorService.execute(new Manufacturer(
            exchangerProducer,
            exchangerConsumer
        ));
        executorService.execute(new Consumer(
            exchangerConsumer
        ));

        ScheduledExecutorService scheduledExecutorService =
            Executors.newScheduledThreadPool(1);

        scheduledExecutorService.schedule(
            () -> {
                executorService.shutdownNow();
                scheduledExecutorService.shutdown();
            }, 5, TimeUnit.SECONDS);
    }
}