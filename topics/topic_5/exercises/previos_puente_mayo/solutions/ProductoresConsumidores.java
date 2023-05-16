import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ProductoresConsumidores {
    static Exchanger<Integer> exchanger = new Exchanger<>();
    static int producido = 0;
    static ReentrantLock producidoEx = new ReentrantLock();
    static int recibido = 0;
    static ReentrantLock recibidoEx = new ReentrantLock();

    static ReentrantLock productor = new ReentrantLock();
    static ReentrantLock recibidor = new ReentrantLock();

    public static void nuevoProducto() {
        producidoEx.lock();
        producido += 1;
        producidoEx.unlock();
    }

    public static void productoRecibido() {
        recibidoEx.lock();
        recibido += 1;
        recibidoEx.unlock();
    }

    public static void ProductorWhile() {
        while (!Thread.interrupted()) { // Revisen la diferencia con Thread.currentThread().isInterrupted()
            nuevoProducto();
            productor.lock();
            try {
                exchanger.exchange(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                productor.unlock();
            }
        }
    }

    public static void ConsumidorWhile() {
        while (!Thread.interrupted()) { // Revisen la diferencia con Thread.currentThread().isInterrupted()
            recibidor.lock();
            try {
                exchanger.exchange(null);
                productoRecibido();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                recibidor.unlock();
            }

        }
    }


    public static void ProductorTarea(ExecutorService ex) {
        nuevoProducto();
        productor.lock();
        try {
            exchanger.exchange(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            productor.unlock();
        }
        ex.submit(() -> ProductorTarea(ex));
    }

    public static void ConsumidorTarea(ExecutorService ex) {
        recibidor.lock();
        try {
            exchanger.exchange(null);
            productoRecibido();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            recibidor.unlock();
        }
        ex.submit(() -> ConsumidorTarea(ex));
    }

    public static void Estadisticas() {
        System.out.format("%s-%s\n", producido, recibido);
    }

    public static void ProductoresConsummidoresWhile() {
        var executor = Executors.newScheduledThreadPool(8); // ¿Por qué necesitamos aquí al menos 8?

        for (int i = 0; i < 2; i++) {
            executor.submit(ProductoresConsumidores::ProductorWhile);
        }
        for (int i = 0; i < 5; i++) {
            executor.submit(ProductoresConsumidores::ConsumidorWhile);
        }

        executor.scheduleAtFixedRate(ProductoresConsumidores::Estadisticas, 0, 1, TimeUnit.SECONDS);

        try {
            Thread.sleep(5000);
            executor.shutdownNow(); // ¿Aquí con Now???
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public static void ProductoresConsummidoresTareas() {
        var executor = Executors.newScheduledThreadPool(6); // ¿Por qué aquí funciona con 6?

        for (int i = 0; i < 2; i++) {
            executor.submit(() -> ProductorTarea(executor));
        }
        for (int i = 0; i < 5; i++) {
            executor.submit(() -> ConsumidorTarea(executor));
        }


        executor.scheduleAtFixedRate(ProductoresConsumidores::Estadisticas, 0, 1, TimeUnit.SECONDS);

        try {
            Thread.sleep(5000);
            executor.shutdown(); // ¿Aquí sin Now???
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println("While");
        ProductoresConsummidoresWhile();
        System.out.println("Tareas");
        ProductoresConsummidoresTareas();
        // El segundo obtiene un relativo mejor rendimiento, aunque los costes de gestión son mejores
    }

}
