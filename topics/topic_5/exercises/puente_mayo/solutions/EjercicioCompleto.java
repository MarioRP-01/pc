package PuenteMayo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EjercicioCompleto {
    // Vamos a trabajar con streams para todo, y de esta forma poder componer métodos más complicados

    public record GPU(
            String name,
            String brand,
            String model,
            float memory,
            String memory_interface,
            float length,
            String interface_data, // Interface no se puede utilizar
            String chipset,
            float base_clock,
            float clock_speed,
            String frame_sync,
            float price,
            String item_url,
            boolean used) {
    }

    public static List<GPU> leerFichero(String fileName) {
        try ( // try-with-resources algo interesante de lo que puede aprender
              var fr = new FileReader(fileName);
              var br = new BufferedReader(fr)) {
            return br.lines()
                    .parallel() // Puede hacer todo el paralelo, nos da igual el orden
                    .skip(1) // Quitamos la cabecera
                    .map(str -> str.split(",(?=([^\"]|\"[^\"]*\")*$)"))// Transformamos la línea an partes, en este caso usamos una expresión regular compleja
                    // Nota: se puede poner un simple split por "," y con el siguiente filtro eliminaremos los campos incorrectos
                    .filter(lista -> lista.length <= 14)
                    // Ahora vamos a limpiar los datos, debido a la "suciedad" con la que vienen.
                    .map(lista ->
                            Arrays.stream(lista)
                                    .map(String::strip)
                                    .map(str -> str.replaceAll("^\"|\"$", ""))
                                    .toArray(String[]::new))
                    // Lo transformamos al objeto requerido
                    .map(lista -> {
                        try { // Si hay problemas, omitimos la línea
                            return new GPU(
                                    lista[0],
                                    lista[1],
                                    lista[2],
                                    Float.parseFloat(lista[3].replace("GB", "").strip()),
                                    lista[4],
                                    Float.parseFloat(lista[5].replace("mm", "").strip()),
                                    lista[6],
                                    lista[7],
                                    Float.parseFloat(lista[8].replace("MHz", "").strip()),
                                    Float.parseFloat(lista[9].replace("MHz", "").strip()),
                                    lista[10],
                                    Float.parseFloat(lista[11].replace("$", "").strip().replace(",", "")),
                                    lista[12],
                                    lista[13].equals("1")
                            );
                        } catch (NumberFormatException e) {
                            // Por desgracia hay datos que todavía necesitarían más trabajo.
                            System.out.println("PROBLEMA: " + e.getMessage());
                        }
                        return null;
                    })
                    .filter(Objects::nonNull) // Filtramos los elementos con problemas
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static List<String> ejercicio2(Stream<GPU> entrada) {
        return entrada
                .map(GPU::brand) // Obtenemos las marcas
                .distinct() // Filtramos, hablaremos de ello
                .toList();
    }

    static public List<String> ejercicio3(Stream<GPU> entrada) {
        return entrada
                .map(GPU::chipset)
                .distinct()
                .toList();
    }

    static public Map<String, Set<String>> ejercicio4(Stream<GPU> entrada) {
        return entrada.collect(
                // Agrupamos por marca y después creamos un conjunto con los chipsets para no tener repetidos
                Collectors.groupingBy(
                        GPU::brand,
                        Collectors.mapping(GPU::chipset, Collectors.toSet())
                ));
    }

    static public List<String> ejercicio5(Stream<GPU> entrada) {
        return entrada
                // Creamos un filtro con los tres valores
                .sorted(
                        Comparator.comparing(GPU::memory, Float::compareTo)
                                .thenComparing(GPU::clock_speed, Float::compare)
                                .thenComparing(GPU::base_clock, Float::compare).reversed())
                // Nos quedamos con los 10 primeros
                .limit(10)
                // Mapeamos el valor
                .map(GPU::toString)
                .toList();

    }

    static public Map<String, Integer> ejercicio6(Stream<GPU> entrada) {
        return entrada
                .collect(
                        // Agrupamos por marca
                        Collectors.groupingBy(
                                GPU::brand,
                                Collectors.mapping( // Mapeamos para quedarnos con el chipset
                                        GPU::chipset,
                                        // Creamos un conjunto para no tener repetidos, y tomamos el valor de este.
                                        Collectors.collectingAndThen(Collectors.toSet(), Set::size))
                        ));
    }


    static public Map<String, Float> ejercicio7(Stream<GPU> entrada) {
        return entrada
                .collect(
                        // Agrupamos por chipset
                        Collectors.groupingBy(
                                GPU::chipset,
                                Collectors.collectingAndThen(
                                        // Agrupamos con base_clock y vemos el número de repeticiones
                                        Collectors.groupingBy(GPU::base_clock, Collectors.counting()),
                                        // Nos quedamos con el base_clock que más se repita
                                        (a) -> a.entrySet()
                                                .stream()
                                                .min((x, y) -> Long.compare(y.getValue(), x.getValue()))
                                                .map(Map.Entry::getKey)
                                                .orElseGet(() -> (float) 0)
                                )
                        ));
    }

    record Differencias(float velocidadNormal, float velocidad, String chipset, String brand) {
    }

    static public Map<String, List<Differencias>> ejercicio8(Map<String, Float> mapaEjercicio7, Stream<GPU> entrada) {
        return entrada
                .collect(
                        // Agrupamos por brand
                        Collectors.groupingBy(
                                GPU::brand,
                                // Mapeamos los valores al nuevo tipo, o devolvemos null
                                Collectors.mapping(
                                        (a) -> {
                                            var velocidadNormal = mapaEjercicio7.get(a.chipset());
                                            if (a.base_clock() != velocidadNormal)
                                                return new Differencias(velocidadNormal, a.base_clock(), a.chipset(), a.brand());
                                            else
                                                return null;
                                        },
                                        // Filtramos los Nulls y creamos una lista
                                        Collectors.filtering(
                                                Objects::nonNull,
                                                Collectors.toList()
                                        )
                                )
                        ));
    }

    static public List<String> ejercicio9(Stream<GPU> entrada) {
        return entrada.collect(
                Collectors.collectingAndThen(
                        // Agrupamos por chipsets contando el número de apariciones
                        Collectors.groupingBy(GPU::chipset, Collectors.counting()),
                        // Nos quedamos con los 5 más vistos
                        (a) -> a.entrySet()
                                .stream()
                                .sorted(Comparator.comparing(Map.Entry<String, Long>::getValue).reversed())
                                .limit(5)
                                .map(Map.Entry::getKey)
                                .collect(Collectors.toList())
                ));
    }

    static public Map<String, String> ejercicio10(Stream<GPU> entrada) {
        return entrada.collect(
                // Agrupamos por chipset
                Collectors.groupingBy(
                        GPU::chipset,
                        Collectors.collectingAndThen(
                                // Creamos una lista
                                Collectors.toList(),
                                // Nos quedamos con la GPU más cara
                                (a) ->
                                        a.stream()
                                                .max(Comparator.comparing(GPU::price))
                                                .map(GPU::model)
                                                .orElseGet(() -> "Sin Datos")
                        )
                ));
    }

    static public String transformarString(String entrada) {
        return (entrada.contains(",")) ? "\"" + entrada + "\"" : entrada;
    }

    static public String transformarPrice(Float entrada) {
        return "$" + entrada.toString();
    }

    static public String ejercicio11(Stream<GPU> entrada) {
        // Ejemplo con solo 4 parámetros
        var header = "name,brand,price,memory\n";
        return entrada.limit(4).map(gpu ->
                String.format("%s,%s,%s,%s",
                        transformarString(gpu.name()),
                        gpu.brand(),
                        transformarPrice(gpu.price),
                        gpu.memory + " GB")).collect(Collectors.joining("\n", header, "\n"));
    }

    static public DoubleSummaryStatistics ejercicio12(Stream<GPU> entrada) {
        return entrada
                .mapToDouble(GPU::price)
                .summaryStatistics();
    }

    static public Map<String, DoubleSummaryStatistics> ejercicio13(Stream<GPU> entrada) {
        return entrada
                .collect(
                        Collectors.groupingBy(
                                GPU::brand,
                                Collectors.summarizingDouble(GPU::price)
                        )
                );
    }

    public static void main(String[] args) {
        var data = leerFichero("src/PuenteMayo/gpu_specs_prices.csv");
        System.out.println(data.get(0));
        Supplier<Stream<GPU>> generador_datos = () -> data.stream().parallel();

        generador_datos.get()
                .limit(5)
                .forEach(System.out::println);

        var dato2 = ejercicio2(generador_datos.get());
        System.out.println("Ejercicio2");
        System.out.println(dato2);
        var dato3 = ejercicio3(generador_datos.get());
        System.out.println("Ejercicio3");
        System.out.println(dato3);
        var dato4 = ejercicio4(generador_datos.get());
        System.out.println("Ejercicio4");
        System.out.println(dato4);
        var dato5 = ejercicio5(generador_datos.get());
        System.out.println("Ejercicio5");
        System.out.println(dato5);
        var dato6 = ejercicio6(generador_datos.get());
        System.out.println("Ejercicio6");
        System.out.println(dato6);
        var dato7 = ejercicio7(generador_datos.get());
        System.out.println("Ejercicio7");
        System.out.println(dato7);
        var dato8 = ejercicio8(dato7, generador_datos.get());
        System.out.println("Ejercicio8");
        System.out.println(dato8);
        var dato9 = ejercicio9(generador_datos.get());
        System.out.println("Ejercicio9");
        System.out.println(dato9);
        var dato10 = ejercicio10(generador_datos.get());
        System.out.println("Ejercicio10");
        System.out.println(dato10);
        var dato11 = ejercicio11(generador_datos.get());
        System.out.println("Ejercicio11");
        System.out.println(dato11);
        var dato12 = ejercicio12(generador_datos.get());
        System.out.println("Ejercicio12");
        System.out.println(dato12);
        var dato13 = ejercicio13(generador_datos.get());
        System.out.println("Ejercicio13");
        System.out.println(dato13);
    }
}

