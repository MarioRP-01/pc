package LegalDownloads;

import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    
    public static void main(String[] args) {
        
        Semaphore download_limits = new Semaphore(6, true);
        CyclicBarrier max_downloaders = new CyclicBarrier(
            5, 
            () -> System.out.println("Download batch complete!")
        );

        IntFunction<Callable<Void>> download_initializer = (int n) -> () -> {
            download_limits.acquireUninterruptibly();
            System.out.println("Downloading file " + n);
            downloadFile();
            System.out.println(" -> Downloaded file " + n);
            max_downloaders.await();
            download_limits.release();
            return null;
        };

        var tareas = IntStream.range(0, 100)
            .mapToObj(download_initializer)
            .collect(Collectors.toList());

        ExecutorService executorService = 
            Executors.newFixedThreadPool(5);

        try {
            executorService.invokeAll(tareas);
            executorService.shutdown();
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }

    }

    public static void downloadFile() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }   
    }
}
