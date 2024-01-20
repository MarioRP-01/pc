package Exercise2;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Lo siento por el spanglish pero algunas traducciones no me han salido al vuelo
 */
public class Main {

    public static Map<Integer, List<Integer>> storage = new ConcurrentHashMap<>();
    public static BlockingQueue<Integer> exitQueue = new LinkedBlockingQueue<Integer>();

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newCachedThreadPool();



        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

        scheduledExecutorService.scheduleAtFixedRate(
            new Camera(
                executorService,
                scheduledExecutorService
            ),
            0, 2, TimeUnit.SECONDS
        );
    }
}
