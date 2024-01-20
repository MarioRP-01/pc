package Exercise3;

import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Main {

    public record Data (
        int id,
        int size
    ) { };

    public static void main(String[] args) {

        List<Data> data = new ArrayList<>();
    
        Random random = new Random();
        
        // int valueId;
        // int valueSize;

        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 50; j++) {
                data.add(new Data(i, random.nextInt(900) + 100));
            }
        }

        IntSummaryStatistics statics = data.stream()
            .mapToInt(Data::size)
            .summaryStatistics();
        
        System.out.println(statics);

        data.stream()
            .collect(Collectors.groupingBy(
                Data::id, Collectors.mapping(Data::size, Collectors.maxBy(
                    (x, y) -> Integer.compare(0, 0))
                ))
            )
            .forEach((x, y) -> 
                System.out.format(
                    "id: %d, max_size: %d\n",
                    x, y.get()
                ));

        data.stream()
            .collect(Collectors.groupingBy(Data::id, 
                Collectors.mapping(
                    Data::size, 
                    Collectors.toList()
                ))
            )
            .forEach((x, y) ->
                System.out.format(
                "id: %d, max_size: %s\n",
                x, y.toString()
            ));

        data.stream()
            .collect(Collectors.groupingBy(Data::id, Collectors.summingInt(Data::size)));

        
    }
}
