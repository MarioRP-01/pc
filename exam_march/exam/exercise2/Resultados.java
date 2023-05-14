package exam.exercise2;

import java.util.concurrent.Semaphore;

public class Resultados extends Thread {

    Juez juez;
    Semaphore esperaJuez = new Semaphore(0);

    Resultados()
    {
        super("resultado");
    }

    public void setJuez(Juez juez) {
        this.juez = juez;
    }

    public void show()
    {

    }
}
