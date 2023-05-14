package topics.topic_3.exercises_pdf.solutions;

public class Ejercicio1 {
    static volatile boolean despuesDeA = false;
    static volatile boolean despuesDeB = false;
    static volatile boolean despuesDeC = false;
    static volatile boolean despuesDe1 = false;
    static volatile boolean despuesDe2 = false;
    static volatile boolean despuesDeII = false;

    public static void main(String[] args) {

        var hilo1 = new Thread(() -> {
            System.out.println("A");
            despuesDeA = true;
            while (!despuesDe1) Thread.onSpinWait();
            System.out.println("B");
            despuesDeB = true;
            System.out.println("C");
            despuesDeC = true;
            System.out.println("D");
        }, "hilo1");
        var hilo2 = new Thread(() -> {
            System.out.println("I");
            while (!despuesDe2 || !despuesDeB) Thread.onSpinWait();
            System.out.println("II");
            despuesDeII = true;
            while (!despuesDeC) Thread.onSpinWait();
            System.out.println("III");
        }, "hilo1");

        var hilo3 = new Thread(() -> {
            while (!despuesDeA) Thread.onSpinWait();
            System.out.println("1");
            despuesDe1 = true;
            System.out.println("2");
            despuesDe2 = true;
            while (!despuesDeII) Thread.onSpinWait();
            System.out.println("3");
            ;
        }, "hilo3");
        hilo1.start();
        hilo2.start();
        hilo3.start();

        try {
            hilo1.join();
            hilo2.join();
            hilo3.join();
        } catch (InterruptedException ignore) {
            // No hay otra alternativa
            System.exit(1);
        }
    }
}
