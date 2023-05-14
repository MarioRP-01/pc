package topics.topic_3.exercises_pdf.solutions;


import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Camarero extends Thread {
    ArrayList<Mesa> mesas = new ArrayList<>();
    Cocinero cocinero;
    // Comunicación
    Semaphore clientesEsperando = new Semaphore(0, true);
    Semaphore mesasDisponibles = new Semaphore(0, true);
    Semaphore sCliente = new Semaphore(0, true);
    Cliente cliente;
    boolean fin;

    public void configurarMesas(int numeroMesas) {
        for (int i = 0; i < numeroMesas; i++) {
            mesas.add(new Mesa());
            mesasDisponibles.release();
        }
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        this.sCliente.release();
    }

    public void setCocinero(Cocinero cocinero) {
        this.cocinero = cocinero;
    }

    protected void llevarComida() {
        var pedido = cocinero.recogerPedido();
        if (pedido != null) {
            var mesa = mesas.get(pedido.mesa());
            System.out.printf("Mesa %s aquí tiene el plato %s\n", pedido.mesa(), pedido.plato());
            mesa.comunicacion = pedido.plato();
            mesa.cliente.release();
            mesa.camarero.acquireUninterruptibly();
            System.out.printf("Pedido de la mesa %s entregado\n", pedido.mesa());
        }
    }

    protected Mesa buscarMesa() {
        for (Mesa mesa : mesas) {
            if (mesa.reservarMesa()) return mesa;
        }
        throw new RuntimeException("No había mesas libres");

    }

    protected void asignarMesa() {
        if (clientesEsperando.hasQueuedThreads()) {
            if (mesasDisponibles.tryAcquire()) {
                // Si hay mesas se asigna al cliente
                System.out.println("Tenemos mesa, un segundo");
                clientesEsperando.release(); // Avisamos al cliente
                this.sCliente.acquireUninterruptibly();
                cliente.setMesa(buscarMesa());
                cliente.semaforo.release();
            } else {
                System.out.println("No hay mesa disponible, espere por favor");
            }
        }
    }

    public void atenderMesas() {
        configurarMesas(10);
        for (int i = 0; i < mesas.size(); i++) {
            atenderMesa(mesas.get(i), i);
        }
    }

    public void atenderMesa(Mesa mesa, int indice) {
        if (!mesa.comandaTomada) {
            mesa.comandaTomada = true;
            System.out.printf("Qué quiere usted? (mesa %s)\n", indice);

            // Esperemos pedido
            mesa.cliente.release();
            mesa.camarero.acquireUninterruptibly();

            // Pedido confirmado
            System.out.printf("Perfecto :D (mesa %s)\n", indice);
            this.cocinero.nuevoPedido(indice, mesa.comunicacion);
        }
    }

    @Override
    public void run() {
        while (!fin) {
            asignarMesa();
            atenderMesas();
            llevarComida();
        }
    }

    public Camarero() {
        super("camarero");
    }
}
