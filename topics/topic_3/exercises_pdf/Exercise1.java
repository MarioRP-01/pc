package topics.topic_3.exercises_pdf;

import java.util.ArrayList;
import java.util.List;

public class Exercise1 {

    private static boolean alreadyA = false;
    private static boolean already1 = false;
    private static boolean alreadyB = false;
    private static boolean already2 = false;
    private static boolean alreadyC = false;
    private static boolean alreadyII = false;

    public static void thread1()
    {
        System.out.println("A");
        alreadyA = true;
        while(!already1) Thread.onSpinWait();
        System.out.println("B");
        alreadyB = true;
        System.out.println("C");
        alreadyC = true;
        System.out.println("D");
    }

    public static void thread2()
    {
        System.out.println("I");
        while(!alreadyB || !already2) Thread.onSpinWait();
        System.out.println("II");
        alreadyII = true;
        while(!alreadyC) Thread.onSpinWait();
        System.out.println("III");
    }

    public static void thread3()
    {
        while(!alreadyA) Thread.onSpinWait();
        System.out.println("1");
        already1 = true;
        while(!already1) Thread.onSpinWait();
        System.out.println("2");
        already2= true;
        while(!alreadyII) Thread.onSpinWait();
        System.out.println("3");
    }

    public static void main(String[] args) throws InterruptedException 
    {
        List<Thread> threads = new ArrayList<>();

        threads.add(new Thread(() -> thread1(), "thread1"));
        threads.add(new Thread(() -> thread2(), "thread2"));
        threads.add(new Thread(() -> thread3(), "thread3"));
        threads.forEach((thread) -> thread.start());
        Thread.sleep(1000);
        System.exit(0);
    }
}
