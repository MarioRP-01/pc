package topics.topic_3.exercises_pdf.solutions;


import java.util.ArrayList;
import java.util.concurrent.Semaphore;


public class Cocinero extends Thread {

    public record Pedido(int mesa, int plato) {
    }

    Semaphore exLista = new Semaphore(1, true);
    ArrayList<Pedido> lista = new ArrayList<>();


    Semaphore exPlatosListos = new Semaphore(1, true);
    ArrayList<Pedido> platoListos = new ArrayList<>();

    // Para avisar al camarero
    Semaphore platosDisponibles = new Semaphore(0, true);

    boolean fin = false;

    @Override
    public void run() {
        try {
            while (!fin) {
                cocinar();
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            // No sabemos qué hacer
            throw new RuntimeException(e);
        }
    }

    public void nuevoPedido(int mesa, int plato) {
        var pedido = new Pedido(mesa, plato);
        exLista.acquireUninterruptibly();
        lista.add(pedido);
        exLista.release();
    }

    public Pedido recogerPedido() {
        if (platosDisponibles.tryAcquire()) {
            try {
                exPlatosListos.acquireUninterruptibly();
                return platoListos.remove(0);
            } finally {
                exPlatosListos.release();
            }
        }
        return null;
    }

    protected Pedido sacarComanda() {
        exLista.acquireUninterruptibly();
        try {
            if (lista.size() > 0)
                return lista.remove(0);
            else
                return null;
        } finally {
            exLista.release();
        }
    }

    protected void cocinar() throws InterruptedException {
        var comanda = sacarComanda();
        if (comanda == null) return;
        // Cocinando
        Thread.sleep(100);
        exPlatosListos.acquireUninterruptibly();
        platoListos.add(comanda);
        exPlatosListos.release();
        System.out.printf("Plato %s para mesa %s preparado", comanda.plato, comanda.mesa);
        platosDisponibles.release();
    }

    public Cocinero() {
        super("Camarero");
    }
}
