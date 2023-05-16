package fibonacci;

import java.util.concurrent.ForkJoinPool;

public class Main {

    private static final Integer N = 10;
    private static final Boolean SIMPLE = true;

    public static void main(String[] args) {

        if (SIMPLE) {
            try (ForkJoinPool pool = new ForkJoinPool()) {

                int result = pool.invoke(new FibonacciSimple(N));
                System.out.println("Fibonacci: " + result);
                pool.shutdown();
            }
        } else {
            try (ForkJoinPool pool = new ForkJoinPool()) {

                int result = pool.invoke(new FibonacciBrute(N));
                System.out.println("Fibonacci: " + result);
                pool.shutdown();
            }
        }
        
    }
    
}
