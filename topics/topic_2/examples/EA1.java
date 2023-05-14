package topics.topic_2.examples;

public class EA1 {

    private static double x;
    private static double y;
    private static double z;

    public static void sleep() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Thread(() -> {
            x = x + 1;
            System.out.println("x = x + 1");
            sleep();
            y = y * x;
            System.out.println("y = y * x");
            sleep();
            z = x % y;
            System.out.println("z = x % y");
            sleep();
            System.out.println("x = "+x);
        }).start();
        new Thread(() -> {
            x = 8;
            System.out.println("x = 8");
            sleep();
            y = Math.sqrt(x);
            System.out.println("y = sqrt(x)");
            sleep();
            z = y / 2;
            System.out.println("z = y / 2");
            sleep();
        }).start();

    }
}
