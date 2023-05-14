package topics.topic_3.exercises_pdf.exercise2;

import java.util.concurrent.Semaphore;

public class Table {

    private int id;
    private Semaphore free = new Semaphore(0);

    public Table(int id)
    {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean assign()
    {
        return free.tryAcquire(0);
    }

    public void free()
    {
        free.release();
    }
}
