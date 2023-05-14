package topics.topic_2.examples;

public class EA2 {

    private static volatile boolean continuar;

    public static void sleep() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void procA() {
        new Thread(() -> {
            System.out.println("PA1");
            sleep();
            continuar = true;
            System.out.println("PA2");
            sleep();
        }).start();
    }

    public static void procB() {
        new Thread(() -> {
            System.out.println("PB1");
            sleep();
            while (!continuar) Thread.onSpinWait();
            System.out.println("PB2");
            sleep();
        }).start();
    }

    public static void main(String[] args) {
        continuar = false;
        procA();
        procB();
    }
}
