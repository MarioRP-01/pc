package topics.topic_3.exercises_pdf.exercise2;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Waiter extends Thread {
    
    private Restaurant restaurant;
    private Cook cook;

    private Customer customer = null;
    private Semaphore attendCustomer = new Semaphore(0, true);

    ArrayList<Table> tables = new ArrayList<>();
    

    public Waiter(Restaurant restaurant)
    {
        super("waiter");
        this.restaurant = restaurant;
    }

    public void setCook(Cook cook) {
        this.cook = cook;
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
        attendCustomer.release();
    }

    public void waitForCustomer()
    {
        attendCustomer.acquireUninterruptibly();
    }

    public void freeCustomer()
    {
        customer = null;
        attendCustomer.acquireUninterruptibly();
    }

    public void assignTable()
    {
        if (!restaurant.areCustomersWaiting()) return;
        
        if (!restaurant.areAvailableTables())
        {
            System.out.println("There are no tables currently available.");
            return;
        }
        
        System.out.println("We currently have tables available. " +
            "We'll seat you as soon as possible."
        );

        restaurant.startServingNextClient();
        waitForCustomer();

        Table table = restaurant.getFreeTable();
        System.out.println("Please, take a seat in table " + table.getId());
        waitForCustomer();
    }

    @Override
    public void run()
    {
        
    }

}
