package Exercises;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Exercise5 {
    public static void main(String[] args) throws IOException {

        var fr = new FileReader("src/gpu_specs_prices.csv"); 
        var br = new BufferedReader(fr);

        var elements = Exercise1.getGpu(br.lines());
        var data = getTierList(elements);

        System.out.println(data);

        br.close();
    }

    public static List<Gpu> getTierList(List<Gpu> elements) {
        return elements.stream().parallel()
            .sorted(
                Comparator
                    .comparing(Gpu::memory, Float::compareTo)
                    .thenComparing(Gpu::clock_speed, Float::compareTo)
                    .thenComparing(Gpu::base_clock, Float::compareTo)
                )
            .limit(10)
            .toList();
            
    }
}
