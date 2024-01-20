package Exercises;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Exercise4 {
    
    public static void main(String[] args) throws IOException {

        var fr = new FileReader("src/gpu_specs_prices.csv"); 
        var br = new BufferedReader(fr);

        var elements = Exercise1.getGpu(br.lines());
        var data = getChipsetByManufacturer(elements);

        System.out.println(data);

        br.close();
    }

    public static Map<String, Set<String>> getChipsetByManufacturer(List<Gpu> elements) {
        return elements.stream().parallel()
            .collect(
                Collectors.groupingBy(
                    Gpu::brand,
                    Collectors.mapping(Gpu::chipset, Collectors.toSet())
                )
            );
    }
}
