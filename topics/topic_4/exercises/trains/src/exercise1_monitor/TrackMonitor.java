package exercise1_monitor;

import java.util.ArrayList;
import java.util.List;

import exercise1.Direction;

public class TrackMonitor
{    
    private List<Intersection> intersections = new ArrayList<>();

    private boolean occupied = false;

    TrackMonitor (Track track)
    {
        intersections.add(new Intersection(track, Direction.FORWARD));
        intersections.add(new Intersection(track, Direction.BACKWARD));
    }

    public synchronized void enter(Train train)
    {
        intersections.stream()
            .filter(i -> i.getDirection() == train.getDirection())
            .forEach(i -> {
                while (occupied)
                    waiting();
                occupied = true;
                i.enter(train);
            });
    }

    public synchronized void leave(Train train)
    {
        intersections.stream()
            .filter(i -> i.getDirection() == train.getDirection())
            .forEach(i -> {
                i.leave(train);
                occupied = false;
                notifyAll();
            });
    }

    private void waiting() 
    {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
