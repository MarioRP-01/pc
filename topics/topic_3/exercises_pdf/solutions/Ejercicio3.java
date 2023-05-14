package topics.topic_3.exercises_pdf.solutions;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Ejercicio3 {

    static int NUM_COMPETIDORES = 9;

    volatile static int variableBatalla = -1;

    static boolean[] fin;
    static Semaphore[] semCompetidores;
    static Semaphore semJuez = new Semaphore(0);

    static void printCompetidor(String mensaje, int numero) {
        System.out.printf("[Competidor-%s] %s\n", numero, mensaje);
    }

    static void sleep(int tiempo) {
        try {
            Thread.sleep(tiempo);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static Random random = new Random();

    static void competidor(int numero) {
        while (!fin[numero]) {
            semCompetidores[numero].acquireUninterruptibly();
            if (fin[numero]) {
                printCompetidor("Me voy", numero);
                break;
            }
            printCompetidor("Preparado para competir", numero);
            for (int i = 0; i < 5; i++) {
                variableBatalla = numero;
                sleep(random.nextInt(100));
            }
            semJuez.release();
        }
    }


    record Duelo(int A, int B) {
    }

    static void gestionarCarrera() {
        while (true) {
            var duelos = new ArrayList<Duelo>();
            var ant = -1;
            for (int i = 0; i < fin.length; i++) {
                if (!fin[i]) {
                    if (ant == -1) {
                        ant = i;
                    } else {
                        duelos.add(new Duelo(ant, i));
                        ant = -1;
                    }
                }
            }

            if (duelos.isEmpty()) {
                if (ant == -1) {
                    System.out.println("No hay gente que quiere competir");
                } else {
                    // Informamos al último que se acabó
                    fin[ant] = true;
                    semCompetidores[ant].release();

                    System.out.println("El ganador es " + ant);
                }
                return;
            }

            System.out.println("------ Nueva ronda ------");
            for (Duelo duelo : duelos) {
                System.out.println("Compiten " + duelo.A + " contra " + duelo.B);
                var semA = semCompetidores[duelo.A];
                var semB = semCompetidores[duelo.B];

                // Para darle emoción
                if (random.nextBoolean()) {
                    semA.release();
                    semB.release();
                } else {
                    semB.release();
                    semA.release();
                }
                semJuez.acquireUninterruptibly(2);

                System.out.println("Ganador de la ronda " + variableBatalla);
                if (variableBatalla == duelo.A) {
                    fin[duelo.B] = true;
                    semCompetidores[duelo.B].release();
                } else if (variableBatalla == duelo.B) {
                    fin[duelo.A] = true;
                    semCompetidores[duelo.A].release();
                } else {
                    System.out.println("Tenemos un problema logístico... Volverán a competir después");
                }
            }
        }
    }

    public static void main(String[] args) {
        var competidores = new ArrayList<Thread>();
        fin = new boolean[NUM_COMPETIDORES];
        semCompetidores = new Semaphore[NUM_COMPETIDORES];
        for (int i = 0; i < NUM_COMPETIDORES; i++) {
            fin[i] = false;
            semCompetidores[i] = new Semaphore(0);
            int finalI = i;
            competidores.add(new Thread(
                    () -> competidor(finalI),
                    "competidor" + i)
            );
        }
        competidores.forEach(Thread::start);

        gestionarCarrera();

        try {
            for (Thread competidor : competidores) {
                competidor.join();
            }
        } catch (InterruptedException ignore) {
            System.exit(1);
        }


    }
}
