package Exercise2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

public class Camera implements Runnable {

    private ExecutorService executorService;
    private ScheduledExecutorService scheduledExecutorService;

    public Camera(
        ExecutorService executorService, 
        ScheduledExecutorService scheduledExecutorService
    ) {
        this.executorService = executorService;
        this.scheduledExecutorService = scheduledExecutorService;
    }

    @Override
    public void run() {
        Main.storage.forEach((x, y) -> System.out.println(y.size()));
    }
    
}
