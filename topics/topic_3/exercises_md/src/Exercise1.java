import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Vamos a simular una tienda con un único dependiente. Trabajaremos con 
 * dos tipos de personas. Tendremos a la persona del mostrador, y a 
 * diversos clientes. La persona del mostrador estará esperando a que una 
 * persona llegue. Cuando la persona llegue se preguntará que quiere, 
 * la otra persona responderá con un número hasta el 10. En función del 
 * número devolverá diferentes productos: Si el valor es menor que 5 
 * devolverá "pan" Si el valor es mayor o igual que 5 devolverá "leche".
 *
 * 
 * Y entonces la persona preguntará cuánto cuesta, la persona del 
 * mostrador devolverá un número aleatorio, y se pagará el dinero 
 * necesario. Después de esto la siguiente persona seguirá el mismo 
 * proceso.
 * 
 * Todo elemento de este código tendrá que funcionar en un hilo 
 * independiente al resto.
 */
public class Exercise1 
{
    protected static volatile int turno = -1;
    protected static volatile boolean free = false;
    protected static volatile String msgShopAssistante;
    protected static volatile String msgClient;

    public static void main(String[] args) throws InterruptedException 
    {
        System.out.println("The shop opens");

        final ShopAssistant shopAssistant = new ShopAssistant();
        final var shopAssistantActions = 
            new Thread(shopAssistant, "shopAssistant");
            
        shopAssistantActions.start();

        final List<Thread> clients = new ArrayList<>();
        final int numClients = new Random().nextInt(4) + 3;
        for (int i = 1; i <= numClients; ++i)
        {
            clients.add(new Thread(new Client(i), "client_" + i));
            clients.get(i - 1).start();
        }
        Thread.sleep(1000);
        System.exit(0);
    }
}

class ShopAssistant implements Runnable 
{
    @Override
    public void run() {
        System.out.println("The shop assistant is ready");
        Exercise1.free = true;
        while(Exercise1.free) Thread.onSpinWait();
        
    }
    
}

class Client implements Runnable 
{
    final int turn;

    public Client(int turn) {
        this.turn = turn; 
    }

    @Override
    public void run() {
        while(!Exercise1.free) Thread.onSpinWait();
        Exercise1.free = false;
        System.out.println("I'm the client " + turn);
    }
}