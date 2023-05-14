package topics.topic_2.examples;

import java.util.Random;

public class EA_ProdConsumidor2 {

    private static volatile boolean produced;
    private static volatile int product;

    public static void sleep(int bound) {
        try {
            Thread.sleep(new Random().nextInt(bound));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void producer() {
        while (true) {
            while (produced) Thread.onSpinWait();
            product = new Random().nextInt(100);
            System.out.println("Generado: "+product);
            sleep(5000);
            produced = true;
        }
    }

    public static void consumer() {
        while (true) {
            System.out.println("Esperando que se genere el producto");
            while (!produced) Thread.onSpinWait();
            System.out.println("El producto es: " + product);
            sleep(1000);
            produced = false;
        }
    }

    public static void main(String[] args) {
        produced = false;
        new Thread(() -> producer(), "Producer").start();
        new Thread(() -> consumer(), "Consumer").start();
    }
}
