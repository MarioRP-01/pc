import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Fibonacci {
    public static int FibonacciResurviso(int i) {
        if (i == 1 || i == 2) return 1;
        else if (i == 0) return 0;
        else return FibonacciResurviso(i - 1) + FibonacciResurviso(i - 2);
    }

    public static int FibonacciResurviso(int i, ExecutorService ejecutor) {
        try {
            if (i == 1 || i == 2) return 1;
            else if (i == 0) return 0;
            else {
                var iMenos2 = ejecutor.submit(() -> FibonacciResurviso(i - 2, ejecutor));
                return FibonacciResurviso(i - 1, ejecutor) + iMenos2.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            return Integer.MIN_VALUE;
        }
    }


    static ConcurrentHashMap<Integer, Integer> cache = new ConcurrentHashMap<>();

    public static int FibonacciResurvisoMejorado(int i, ExecutorService ejecutor) {
        try {
            var valorCache = cache.get(i);
            if (valorCache != null) return valorCache;
            else {
                var iMenos2 = ejecutor.submit(() -> FibonacciResurvisoMejorado(i - 2, ejecutor));
                var valorFinal = FibonacciResurvisoMejorado(i - 1, ejecutor) + iMenos2.get();
                cache.putIfAbsent(i, valorFinal);
                return valorFinal;
            }
        } catch (InterruptedException | ExecutionException e) {
            return Integer.MIN_VALUE;
        }
    }

    public static int FibonacciResurvisoMejoradoInverso(int i, ExecutorService ejecutor) {
        try {
            for (int i1 = 0; i1 < i; i1++) {
                int finalI = i1;
                ejecutor.submit(() -> FibonacciResurvisoMejorado(finalI, ejecutor)).get();
            }
            return ejecutor.submit(() -> FibonacciResurvisoMejorado(i, ejecutor)).get();
        } catch (InterruptedException | ExecutionException e) {
            return Integer.MIN_VALUE;
        }
    }

    public static void main1(int i) throws ExecutionException, InterruptedException {
        // Idea básica lanzando una tarea
        var ejecutor = Executors.newFixedThreadPool(8);
        var futuro = ejecutor.submit(() -> FibonacciResurviso(i));
        ejecutor.shutdown();
        System.out.println(futuro.get());
    }

    public static void main2(int i) throws ExecutionException, InterruptedException {
        // Cada tarea va a lanzar otra tarea para el cómputo
        var ejecutor = Executors.newFixedThreadPool(8);
        var futuro = ejecutor.submit(() -> FibonacciResurviso(i, ejecutor));
        var valor = futuro.get();
        ejecutor.shutdown();
        System.out.println(valor);
    }

    public static void main3(int i) throws ExecutionException, InterruptedException {
        var ejecutor = Executors.newFixedThreadPool(8);
        cache.put(0,0);
        cache.put(1,1);
        cache.put(2,1);
        var futuro = ejecutor.submit(() -> FibonacciResurvisoMejorado(i, ejecutor));
        var valor = futuro.get();
        ejecutor.shutdown();
        System.out.println(valor);
    }


    public static void main4(int i) throws ExecutionException, InterruptedException {
        var ejecutor = Executors.newFixedThreadPool(8);
        cache.put(0,0);
        cache.put(1,1);
        cache.put(2,1);
        var futuro = ejecutor.submit(() -> FibonacciResurvisoMejoradoInverso(i, ejecutor));
        var valor = futuro.get();
        ejecutor.shutdown();
        System.out.println(valor);
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // Las excepciones las "ignoramos"
        var inicio = System.currentTimeMillis();
        main1(40);
        var parada1 = System.currentTimeMillis();
        main2(7);
        var parada2 = System.currentTimeMillis();
        main3(7);
        var parada3 = System.currentTimeMillis();
        main4(40);
        var parada4 = System.currentTimeMillis();
        var tiempo1 = (parada1 - inicio) / 1000d;
        var tiempo2 = (parada2 - parada1) / 1000d;
        var tiempo3 = (parada3 - parada2) / 1000d;
        var tiempo4 = (parada4 - parada3) / 1000d;
        // En este ejercicio pueden ver el coste de crear tareas desde tareas.
        // Mis tiempos:
        // Tiempos
        // tiempo1 = 0.205 F(40)
        // tiempo2 = 0.002 F(7)
        // tiempo3 = 0.003 F(7)
        // tiempo4 = 0.002 F(40)
        //
        // La primera versión es de lo mejor, pero debido a cómo lo hemos planteado, tiene un gran gasto computacional,
        //  si le añadimos la mejora puesta en la versión 4, claramente tardaría nada.
        // Las otras dos versiones no pueden comparar, pero si se puede ver que la gestión de tareas tiene un gran gasto
        System.out.format("Tiempos\n%s\n%s\n%s\n%s\n", tiempo1, tiempo2, tiempo3, tiempo4);
    }
}
