package Exercises;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Exercise3 {
    
    public static void main(String[] args) throws IOException {

        var fr = new FileReader("src/gpu_specs_prices.csv"); 
        var br = new BufferedReader(fr);

        var elements = Exercise1.getGpu(br.lines());
        var chipsets = getChipsets(elements);

        System.out.println(chipsets.toString());

        br.close();
    }

    public static List<String> getChipsets(List<Gpu> elements) {

        return elements.stream()
            .parallel()
            .map(Gpu::chipset)
            .distinct()
            .toList();
    }
}
