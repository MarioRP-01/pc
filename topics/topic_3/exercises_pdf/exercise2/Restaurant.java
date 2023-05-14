package topics.topic_3.exercises_pdf.exercise2;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Restaurant {

    private final int numTables; 
    private ArrayList<Table> tableInventory;
    private Semaphore availableTables;
    private Semaphore waitingCustomers = new Semaphore(0);

    public Restaurant(int numTables)
    {
        this.numTables = numTables;
        this.tableInventory = new ArrayList<>(numTables);
        for (int i = 1; i <= numTables; i++) {
            this.tableInventory.add(new Table(i));
        }
        this.availableTables = new Semaphore(numTables);
    }

    public void startServingNextClient()
    {
        waitingCustomers.release();
    }

    public void customerArrive()
    {
        waitingCustomers.acquireUninterruptibly();
    }

    public boolean areCustomersWaiting()
    {
        return waitingCustomers.hasQueuedThreads();
    }

    public boolean areAvailableTables()
    {
        return availableTables.hasQueuedThreads();
    }

    public Table getFreeTable()
    {
        for (Table table : tableInventory) {
            if (!table.assign()) return table;
        }

        throw new RuntimeException("There are not available tables");
    }

    public void assignTable(Waiter waiter)
    {
        
    }

}
