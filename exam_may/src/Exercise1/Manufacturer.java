package Exercise1;

import java.util.concurrent.Exchanger;

public class Manufacturer implements Runnable {

    private final Exchanger<Integer> exchangerProducer;
    private final Exchanger<Float> exchangerConsumer;

    public Manufacturer(
        Exchanger<Integer> exchangerProducer,
        Exchanger<Float> exchangerConsumer
    ) {
        this.exchangerProducer = exchangerProducer;
        this.exchangerConsumer = exchangerConsumer;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                var rawValue = exchangerProducer.exchange(null);
                float value = rawValue ^ 2;
                System.out.println("MANUFACTURED: " + value);
                exchangerConsumer.exchange(value);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
}
