package exercise1_monitor;

import java.util.ArrayList;
import java.util.List;

public class Track 
{
    private List<Train> trains = new ArrayList<>();

    public synchronized void enter(Train train)
    {
        trains.add(train);
    }

    public synchronized void leave(Train train)
    {
        trains.remove(train);
    }
}
