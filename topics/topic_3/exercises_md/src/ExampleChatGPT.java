import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExampleChatGPT {

    public static void main(String[] args) {

        // Creamos el executor service con un único hilo
        ExecutorService executor = Executors.newFixedThreadPool(1);

        // Creamos la persona del mostrador
        Mostrador mostrador = new Mostrador();

        // Ejecutamos al mostrador en el executor service
        executor.execute(mostrador);

        // Creamos un número aleatorio de clientes entre 3 y 6
        int numClientes = new Random().nextInt(4) + 3;

        // Creamos los clientes y los ejecutamos en el executor service
        for (int i = 1; i <= numClientes; i++) {
            Cliente cliente = new Cliente(i, mostrador);
            executor.execute(cliente);
        }

        // Cerramos el executor service
        executor.shutdown();
    }
}

// Clase para representar al mostrador
class Mostrador implements Runnable {

    private String[] productos = {"pan", "leche"};
    private Random random = new Random();

    @Override
    public void run() {
        System.out.println("La persona del mostrador está esperando a que llegue un cliente...");

        // Esperamos a que llegue un cliente
        while (true) {
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Cerrando la tienda...");
                return;
            }
        }
    }

    public synchronized String atenderCliente(int numero) {
        System.out.println("La persona del mostrador está atendiendo al cliente " + numero + "...");
        int productoIndex = random.nextInt(10);
        String producto = productoIndex < 5 ? productos[0] : productos[1];
        System.out.println("El cliente " + numero + " ha pedido " + producto + ".");
        int precio = random.nextInt(10) + 1;
        System.out.println("El precio es " + precio + " euros.");
        return producto;
    }
}

// Clase para representar a un cliente
class Cliente implements Runnable {

    private int numero;
    private Mostrador mostrador;

    public Cliente(int numero, Mostrador mostrador) {
        this.numero = numero;
        this.mostrador = mostrador;
    }

    @Override
    public void run() {
        try {
            // Simulamos el tiempo que tarda el cliente en llegar a la tienda
            Thread.sleep(new Random().nextInt(5000) + 1000);
            System.out.println("El cliente " + numero + " ha llegado a la tienda.");
            String producto = mostrador.atenderCliente(numero);
            // Simulamos el tiempo que tarda el cliente en pagar
            Thread.sleep(new Random().nextInt(5000) + 1000);
            System.out.println("El cliente " + numero + " ha pagado " + producto + ".");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
