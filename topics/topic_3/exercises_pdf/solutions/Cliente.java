package topics.topic_3.exercises_pdf.solutions;


import java.util.Random;
import java.util.concurrent.Semaphore;

public class Cliente implements Runnable {
    Mesa mesa;
    // Semáforo para la gestión de la mesa, y confirmar el nuevo valor
    Semaphore semaforo = new Semaphore(0, true);

    Camarero camarero;

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    @Override
    public void run() {
        // Esperamos a sentarnos
        camarero.clientesEsperando.acquireUninterruptibly();
        camarero.setCliente(this);
        // Vamos a la mesa
        semaforo.acquireUninterruptibly();
        mesa.sentado();
        // Pedimos la comida
        mesa.cliente.acquireUninterruptibly(); // Esperamos al camarero
        var plato = new Random().nextInt(10);
        System.out.println("Me gustaría un plato " + plato);
        mesa.comunicacion = plato;
        mesa.camarero.release();

        // Comemos
        mesa.cliente.acquireUninterruptibly();
        if (mesa.comunicacion == plato) {
            System.out.println("El plato es el correcto");
        } else {
            System.out.println("El plato está mal, pero voy a comérmelo");
        }
        mesa.camarero.release();

        // Falta revisar el plato y realizar el pago

        // Me voy
        mesa.liberarMesa();
    }
}
