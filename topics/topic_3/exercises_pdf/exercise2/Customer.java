package topics.topic_3.exercises_pdf.exercise2;

import java.util.concurrent.Semaphore;

public class Customer implements Runnable {

    private Restaurant restaurant;

    private Waiter waiter;
    private Semaphore waitWaiter = new Semaphore(0);

    public void setWaiter(Restaurant restaurant, Waiter waiter) {
        this.waiter = waiter;
    }

    public void waitForWaiter()
    {
        waitWaiter.acquireUninterruptibly();
    }

    @Override
    public void run()
    {
        // Wait to sit
        restaurant.customerArrive();
        waiter.setCustomer(this);

        waitForWaiter();
    }
}
