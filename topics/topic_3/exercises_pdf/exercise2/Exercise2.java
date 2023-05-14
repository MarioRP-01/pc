package topics.topic_3.exercises_pdf.exercise2;

import java.util.ArrayList;
import java.util.Random;

public class Exercise2 
{
    public static void main(String[] args)
    {
        final var customerAmount = new Random().nextInt(29) + 1;
        final var numTables = 10;

        var restaurant = new Restaurant(numTables);
        var waiter = new Waiter(restaurant);
        var cook = new Cook(restaurant);
        cook.setWaiter(waiter);

        var customers = new ArrayList<Thread>(customerAmount);

        for (int i = 0; i < customerAmount; i++)
        {
            var customer = new Customer();
            customer.setWaiter(restaurant, waiter);
            customers.add(new Thread(customer, "customer_" + i));
        }

        cook.start();
        waiter.start();
        customers.forEach(Thread::start);

        try 
        {
            for (Thread customer : customers) {
                customer.join();
            }
        }
        catch (InterruptedException ignore)
        {}
        finally {
            try 
            {
                cook.join();
                waiter.join();
            }
            catch (InterruptedException e)
            {
                System.exit(1);
            }
        }

    }

}

