package topics.topic_2.examples;

import java.util.Random;

public class EA_ProdConsumidor1 {

    private static volatile boolean continuar;
    private static volatile int product;

    public static void sleep(int bound) {
        try {
            Thread.sleep(new Random().nextInt(bound));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void producer() {
        product = new Random().nextInt(100);
        System.out.println("Producido: "+product);
        sleep(5000);
        continuar = true;
    }

    public static void consumer() {
        System.out.println("Esperando que se genere el producto");
        while (!continuar) Thread.onSpinWait();
        System.out.println("El producto es: "+product);
        sleep(1000);
    }

    public static void main(String[] args) {
        continuar = false;
        new Thread(() -> producer(), "Producer").start();
        new Thread(() -> consumer(), "Consumer").start();
    }
}
