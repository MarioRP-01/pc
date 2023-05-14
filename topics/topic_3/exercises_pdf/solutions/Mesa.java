package topics.topic_3.exercises_pdf.solutions;


import java.util.concurrent.Semaphore;

public class Mesa {
    // Mesa preparada para uno o múltiples camareros, ya que toda la información se gestiona desde dentro
    Semaphore cliente = new Semaphore(0);
    Semaphore camarero = new Semaphore(0);
    int comunicacion;
    Semaphore libre = new Semaphore(1);

    volatile boolean comandaTomada = true;

    public boolean reservarMesa() {
        return libre.tryAcquire();
    }

    public void sentado() {
        comandaTomada = false;
    }

    public void liberarMesa() {
        libre.release();
    }
}
