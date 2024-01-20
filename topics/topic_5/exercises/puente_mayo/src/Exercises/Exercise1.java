package Exercises;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Realizar la lectura del fichero utilizando Streams. Empezaremos 
 * separándolo por líneas, y después “mapeando” los datos a un 
 * objeto. Les recomendamos utilizar Records
 */    
public class Exercise1 {

    public static void main(String[] args) throws IOException {
        var fr = new FileReader("src/gpu_specs_prices.csv"); 
        var br = new BufferedReader(fr);

        var elements = getGpu(br.lines());
        
        System.out.println(elements.toString());

        br.close();
    }

    public static List<Gpu> getGpu(Stream<String> stream)
    {
        return stream
            .parallel()
            .skip(1)
            .map((element) -> element.split(",(?=([^\"]|\"[^\"]*\")*$)"))
            .filter((element) -> { return element.length <= 14; })
            .map((element) ->
                Arrays.stream(element)
                    .map(String::strip)
                    .map((str) -> 
                        str.replaceAll("^\"|\"$", "")
                    )
                    .toArray(String[]::new)
            )
            .map((element) -> {
                try {
                    return new Gpu(
                        element[0],
                        element[1],
                        element[2],
                        Float.parseFloat(element[3].replace("GB", "").strip()),
                        element[4],
                        Float.parseFloat(element[5].replace("mm", "").strip()),
                        element[6],
                        element[7],
                        Float.parseFloat(element[8].replace("MHz", "").strip()),
                        Float.parseFloat(element[9].replace("MHz", "").strip()),
                        element[10],
                        Float.parseFloat(element[11].replace("$", "").strip().replace(",", "")),
                        element[12],
                        element[13].equals("1")
                    );
                }
                catch(NumberFormatException e) {
                    System.out.println("ERROR: " + e.getMessage());
                }
                return null;
            })
            .filter(Objects::nonNull)
            .toList();
    }
}
