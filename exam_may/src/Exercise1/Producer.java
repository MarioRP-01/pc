package Exercise1;

import java.util.Random;
import java.util.concurrent.Exchanger;

public class Producer implements Runnable {

    private final Random random = new Random();
    private final Exchanger<Integer> exchanger;

    public Producer(Exchanger<Integer> exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                int rawValue = random.nextInt(100);
                System.out.println("PRODUCED: " + rawValue);
                exchanger.exchange(rawValue);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }    
    }
    
}
