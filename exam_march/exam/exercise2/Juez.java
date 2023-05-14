package exam.exercise2;

import java.util.concurrent.Semaphore;

public class Juez extends Thread {

    Resultados resultados;
    Semaphore esperaResultados = new Semaphore(0);

    /** Impide que a un debate se unan más de dos equipos */
    Semaphore limiteEquipos = new Semaphore(2, true);
    /** Comunica a juez que los dos equipos están listos */
    Semaphore equiposListos = new Semaphore(0);
    /** Elección equipo */
    Semaphore seleccionEquipos = new Semaphore(1);

    Equipo equipo1 = null;

    Equipo equipo2 = null;

    volatile int turnoActual = 1;

    final int rondasAntesDescando = 2;
    final int totalRondas = 4;
    final int milisDescanso = 100;

    Juez()
    {
        super("juez");
    }

    public void setResultados(Resultados resultados) {
        this.resultados = resultados;
    }

    void calculoPregunta1()
    {
        
    }

    void calculoPregunta2()
    {
        // coger más pequeño equipo 1
        // coger más pequeño equipo 2
    }

    void ejecucionRonda(int numRonda) 
    {
        System.out.println("Comienza la ronda: " + numRonda);
        System.out.println("Generen sus numeros");
        equipo1.esperaJuez.release();
        equipo2.esperaJuez.release();
    }

    @Override
    public void run() {
        System.out.println("Esperando a los equipos");

        equiposListos.acquireUninterruptibly(2);
        equipo1.setTurno(1);
        equipo2.setTurno(2);

        System.out.println("Ya se han elegido los equipos, comienzan las" + 
            "rondas");

        
        for (int i = 1; i <= rondasAntesDescando; i++) {
            ejecucionRonda(i);
        }
        sleep(milisDescanso);
        for (int i = rondasAntesDescando + 1; i <= totalRondas; i++) {
            ejecucionRonda(i);
        }
        limiteEquipos.release(2);
        seleccionEquipos.release();
    }

    void sleep(int milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
