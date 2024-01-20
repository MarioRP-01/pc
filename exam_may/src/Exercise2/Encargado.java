package Exercise2;

public class Encargado implements Runnable {

    @Override
    public void run() {

        if (Thread.interrupted())
            try {
                Main.exitQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
        }

        System.out.println("message");
    
    }
}
