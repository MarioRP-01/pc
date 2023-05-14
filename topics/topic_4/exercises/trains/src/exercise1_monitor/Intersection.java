package exercise1_monitor;

import exercise1.Direction;

public class Intersection 
{
    private Track track;
    private Direction direction;

    Intersection(Track track, Direction direction)
    {
        this.track = track;
        this.direction = direction;
    }

    public void enter(Train train)
    {
        track.enter(train);
    }

    public void leave(Train train)
    {
        track.leave(train);
    }

    public Track getTrack()
    {
        return track;
    }

    public Direction getDirection()
    {
        return direction;
    }
}
