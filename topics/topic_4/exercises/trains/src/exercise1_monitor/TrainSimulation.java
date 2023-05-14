package exercise1_monitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import exercise1.Direction;

public class TrainSimulation 
{
    private static final int number_of_trains = 20;
    private static final List<Train> trains = new ArrayList<>();

    public static void main(String[] args) 
    {
        var random = new Random();
        var track = new Track();
        var trackMonitor = new TrackMonitor(track);

        for (int i = 1; i <= number_of_trains; i++) 
        {
            random = new Random();
            trains.add(new Train(
                String.format("%d", i),
                trackMonitor,
                getRandomDirection(random)
            ));
        }

        trains.forEach(train -> train.start());
        trains.forEach(train -> {
            try {
                train.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private static Direction getRandomDirection(Random random)
    {
        return random.nextBoolean() ? Direction.FORWARD : Direction.BACKWARD;
    }
}
