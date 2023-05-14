package topics.topic_3.exercises_pdf.exercise2;

public class Cook extends Thread {
    
    private Restaurant restaurant;
    private Waiter waiter;

    public Cook(Restaurant restaurant)
    {
        super("cook");
        this.restaurant = restaurant;
    }

    public void setWaiter(Waiter waiter) {
        this.waiter = waiter;
    }

    @Override
    public void run()
    {
        
    }
}
