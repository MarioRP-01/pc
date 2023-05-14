package exam.exercise2;

import java.util.ArrayList;

public class Exercise2 {
    
    // Supondré caso base para solamente dos equipos.
    // Supondré que cada debate constarña de 4 rondas

    static int numParejasEquipos = 1;

    public static void main(String[] args) {
        var juez = new Juez();
        var resultados = new Resultados();

        int numEquipos = numParejasEquipos * 2;
        var equipos = new ArrayList<Thread>();

        for (int i = 1; i <= numEquipos; i++) {
            Equipo equipo = new Equipo();
            equipo.setJuez(juez);
            equipos.add(new Thread(equipo, "equipo_" + 1));
        }

        juez.start();
        resultados.start();
        equipos.forEach(Thread::start);

        try 
        {
            juez.join();
            resultados.join();

            for (Thread thread : equipos) {
                thread.join();
            }
        }
        catch (InterruptedException ignore) { }
        finally
        {
            resultados.show();
        }
    }
}
