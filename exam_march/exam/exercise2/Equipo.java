package exam.exercise2;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Equipo implements Runnable {

    Juez juez;
    Semaphore esperaJuez = new Semaphore(0);

    ArrayList<Integer> numerosGenerados;
    volatile int turno;

    public void setTurno(int turno) {
        this.turno = turno;
    }

    public void setJuez(Juez juez) {
        this.juez = juez;
    }

    ArrayList<Integer> generarNumeros()
    {
        var random = new Random();
        var nums = new ArrayList<Integer>();
        for (int i = 0; i < 5; i++) {
            nums.add(random.nextInt(99) + 1);
        }
        return nums;
    }

    @Override
    public void run() {
        juez.limiteEquipos.acquireUninterruptibly();
        if (juez.seleccionEquipos.tryAcquire() || juez.equipo1 != null)
            juez.equipo1 = this;
        else 
            juez.equipo2 = this;
        juez.equiposListos.release();

        esperaJuez.acquireUninterruptibly();
        numerosGenerados = generarNumeros();
        esperaJuez.acquireUninterruptibly();
    }
    
}
