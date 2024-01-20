package Exercise1;

import java.util.concurrent.Exchanger;

public class Consumer implements Runnable {

    private final Exchanger<Float> exchanger;

    public Consumer(Exchanger<Float> exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                float value = exchanger.exchange(null);
                System.out.println("CONSUMED: " + value);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }}
    
}
