package exam;

import java.util.ArrayList;

class Exercise1 
{
    static volatile boolean padreDespierta = false;
    static volatile boolean madreDespierta = false; 
    
    static volatile boolean padreHaceCama = false; 
    static volatile boolean madreHaceCama = false;
    
    static volatile boolean padreCocina = false;
    static volatile boolean madreDespiertaResto = false;

    static volatile boolean hijoVestirse = false;
    static volatile boolean hijaVestirse = false;

    static volatile boolean padreDesayunar = false;
    static volatile boolean madreDesayunar = false;
    static volatile boolean hijoDesayunar = false;
    static volatile boolean hijaDesayunar = false;    

    static volatile boolean hijaConducir = false;

    static void padre() 
    {
        System.out.println("Padre despierta");
        padreDespierta = true;
        while(!madreDespierta) Thread.onSpinWait();
        System.out.println("Padre hace la cama");
        padreHaceCama = true;
        while(!madreHaceCama) Thread.onSpinWait();
        System.out.println("Padre cocina");
        padreCocina = true;
        System.out.println("Padre desayuna");
        padreDesayunar = true;
        while(!hijaConducir) Thread.onSpinWait();
        System.out.println("padre trabaja");
    }

    static void madre() 
    {
        System.out.println("Madre despierta");
        madreDespierta = true;
        while(!padreDespierta) Thread.onSpinWait();
        System.out.println("Madre hace la cama");
        madreHaceCama = true;
        while(!padreHaceCama) Thread.onSpinWait();
        System.out.println("Madre despierta al resto");
        madreDespiertaResto = true;
        while(!padreCocina) Thread.onSpinWait();
        System.out.println("Madre desayuna");
        madreDesayunar = true;
        while(!hijaConducir) Thread.onSpinWait();
        System.out.println("madre trabaja");
    }

    static void hijo()
    {
        while(!madreDespiertaResto) Thread.onSpinWait();
        System.out.println("Hijo se viste");
        hijoVestirse = true;
        while(!padreCocina) Thread.onSpinWait();
        System.out.println("Hijo desayuna");
        hijoDesayunar = true;
        while(!hijaConducir) Thread.onSpinWait();
        System.out.println("hijo va a clase");
    }

    static void hija()
    {
        while(!madreDespiertaResto) Thread.onSpinWait();
        System.out.println("Hija se viste");
        hijaVestirse = true;
        while(!padreCocina) Thread.onSpinWait();
        System.out.println("Hija desayuna");
        hijaDesayunar = true;
        while(!hijoDesayunar) Thread.onSpinWait();
        System.out.println("Hija conduce");
        hijaConducir = true;
        System.out.println("hija va a clase");
    }

    public static void main(String[] args) 
    {
        var familia = new ArrayList<Thread>();

        familia.add(new Thread(Exercise1::padre, "padre"));        
        familia.add(new Thread(Exercise1::madre, "madre"));
        familia.add(new Thread(Exercise1::hijo, "hijo"));
        familia.add(new Thread(Exercise1::hija, "hija"));  
        
        familia.forEach(Thread::start);
    }
}