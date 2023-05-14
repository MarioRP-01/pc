package topics.topic_3.exercises_pdf.solutions;

import java.util.ArrayList;

public class Ejercicio2 {
    static int NUM_CLIENTES = 20;

    public static void main(String[] args) {
        // En esta implementación hay algunas carencias, tienen que tenerlo en cuenta.

        var camarero = new Camarero();
        var cocinero = new Cocinero();
        camarero.setCocinero(cocinero);
        var clientes = new ArrayList<Thread>(NUM_CLIENTES);
        for (int i = 0; i < NUM_CLIENTES; i++) {
            var cliente = new Cliente();
            cliente.camarero = camarero;
            clientes.add(new Thread(cliente, "Cliente-" + i));
        }

        // Iniciamos todo
        cocinero.start();
        camarero.start();
        clientes.forEach(Thread::start);

        // Esperamos a los clientes
        try {
            for (Thread cliente : clientes) {
                cliente.join();
            }
        } catch (InterruptedException ignore) {
        } finally {
            // Informamos del fin de las comidas
            camarero.fin = true;
            cocinero.fin = true;
            try {
                camarero.join();
                cocinero.join();
            } catch (InterruptedException e) {
                System.exit(1);
            }
        }
    }
}
