import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

public class DescargasLegales {

    public static void Dormir(){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        var limite = new Semaphore(6, true);
        var cyclicBarrier = new CyclicBarrier(5, () -> System.out.println("Acumulando descargas"));

        // Función que va a crear las tareas
        IntFunction<Callable<Void>> creadorTareas = (int numero) -> () -> {
            limite.acquireUninterruptibly();
            System.out.println("Descargando " + numero);
            Dormir();
            System.out.println("-Descargado " + numero);
            cyclicBarrier.await(); // Y la pregunta es: ¿por qué lo ponemos aquí?
            limite.release();
            // cyclicBarrier.await(); // ¿Y si lo ponemos aquí?
            // Ahora, que hemos llegado hasta aquí, tal y como está el código en estos momentos,
            //  ¿hace algo ese semáforo? Les dejo pensar.
            return null;
        };

        // Creamos 100 tareas y las ponemos a ejecutar
        var tareas = IntStream.range(0, 100).mapToObj(creadorTareas).toList();
        var ejecutor = Executors.newCachedThreadPool();
        try {
            ejecutor.invokeAll(tareas);
            ejecutor.shutdown();
        } catch (InterruptedException e) {
            ejecutor.shutdownNow();
        }
    }
}
