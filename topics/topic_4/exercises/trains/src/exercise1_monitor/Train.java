package exercise1_monitor;

import exercise1.Direction;

public class Train extends Thread
{
    TrackMonitor trackMonitor;
    Direction direction;

    public Train(String name, TrackMonitor trackMonitor, Direction direction)
    {
        super(name);
        this.trackMonitor = trackMonitor;
        this.direction = direction;
    }

    @Override
    public void run()
    {
        trackMonitor.enter(this);
        System.out.format(
            "Train %s entered the track section\n", 
            this.getName()
        );
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        trackMonitor.leave(this);
        System.out.format(
            "Train %s left the track section\n", 
            this.getName()
        );
    }

    public Direction getDirection() {
        return direction;
    }
}
