package topics.topic_2.examples;

import java.util.Random;

public class EA_ClienteServidorNPeticiones {

    private static volatile boolean peticion;
    private static volatile boolean realizada;
    private static volatile double pedido;
    private final static int N = 10;

    public static void sleep(int bound) {
        try {
            Thread.sleep(new Random().nextInt(bound));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void cliente(){
        for (int i = 0; i < N; i++) {
            peticion = true;
            sleep(50);
            while(!realizada) Thread.onSpinWait();
            System.out.println("Pedido recibido: "+pedido);
        }
    }

    public static void servidor() {
        for (int i = 0; i < N; i++) {
            while (!peticion) Thread.onSpinWait();
            pedido = new Random().nextInt(1000);
            System.out.println("Peticion realizada: "+pedido);
            realizada = true;
            peticion = false;
        }
    }


    public static void main(String[] args) throws InterruptedException {
        peticion = false;
        realizada = false;

        new Thread(() -> cliente(), "Cliente").start();
        new Thread(() -> servidor(), "Servidor").start();
    }
}
