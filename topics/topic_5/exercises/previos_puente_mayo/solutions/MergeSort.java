import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MergeSort {
    public static List<Integer> CombinarListas(List<Integer> lista1, List<Integer> lista2) {
        int lista1Index = 0;
        int lista2Index = 0;
        List<Integer> merged = new ArrayList<Integer>();

        while (lista1Index < lista1.size() && lista2Index < lista2.size()) {
            if (lista1.get(lista1Index) < lista2.get(lista2Index)) {
                merged.add(lista1.get(lista1Index++));
            } else {
                merged.add(lista2.get(lista2Index++));
            }
        }
        merged.addAll(lista1.subList(lista1Index, lista1.size()));
        merged.addAll(lista2.subList(lista2Index, lista2.size()));
        return merged;
    }

    public static void OrdenarLista(List<Integer> lista) {
        lista.sort(Integer::compare);
    }

    static class MergeSortForkJoin extends RecursiveTask<List<Integer>> {
        protected List<Integer> lista;

        public MergeSortForkJoin(List<Integer> lista) {
            this.lista = lista;
        }

        @Override
        protected List<Integer> compute() {
            if (this.lista.size() < 10) {
                OrdenarLista(this.lista);
                return this.lista;
            }

            // Dividimos la lista en dos
            var medio = lista.size() / 2;
            var primeraParte = lista.subList(0, medio);
            var segundaParte = lista.subList(medio, lista.size());
            var primeraTarea = new MergeSortForkJoin(primeraParte);
            var segundaTarea = new MergeSortForkJoin(segundaParte);

            // Seguimos el orden correcto
            primeraTarea.fork();
            var segundoResultado = segundaTarea.compute();
            var primerResultado = primeraTarea.join();

            // Devolvemos la solución
            return CombinarListas(primerResultado, segundoResultado);
        }
    }

    public static void ForkJoin(Supplier<List<Integer>> lista) {
        var ejecutor = new ForkJoinPool();
        var solucion = ejecutor.invoke(new MergeSortForkJoin(lista.get()));
        ejecutor.shutdown();
    }


    public static void StreamReduce(Supplier<List<Integer>> listaInicial) {
        List<Integer> identidad = new ArrayList<>();
        var solucion = listaInicial.get().stream().parallel().reduce(identidad,
                (lista, numero) -> {
                    List<Integer> aux = new ArrayList<>(lista);
                    if (aux.isEmpty()) {
                        aux.add(numero);
                    } else {
                        // Buscamos el hueco e introducimos el valor
                        var indice = -1;
                        while (++indice < lista.size() && aux.get(indice) < numero) ;
                        aux.add(indice, numero);
                    }
                    return aux;
                }, MergeSort::CombinarListas);
    }

    public static void StreamCollect(Supplier<List<Integer>> listaInicial) {
        var solucion = listaInicial.get().stream().parallel().collect(ArrayList<Integer>::new,
                (lista, numero) -> {
                    if (lista.isEmpty()) {
                        lista.add(numero);
                    } else {
                        // Buscamos el hueco e introducimos el valor
                        var indice = -1;
                        while (++indice < lista.size() && lista.get(indice) < numero) ;
                        lista.add(indice, numero);
                    }
                }, (lista1, lista2) -> {
                    // Aquí tenemos un problema, una solución sería una inserción ordenada
                    //  pero queremos tener el algoritmo más parecido a los otros.
                    var merge = CombinarListas(lista1, lista2);
                    lista1.clear();
                    lista1.addAll(merge);

                });
    }


    public static void main(String[] args) {
        var datos = IntStream.range(1, 5000).boxed().collect(Collectors.toList());
        Collections.shuffle(datos);
        Supplier<List<Integer>> supplierDatos = () -> new ArrayList<Integer>(datos);

        var inicio = System.currentTimeMillis();
        ForkJoin(supplierDatos);
        var parada1 = System.currentTimeMillis();
        StreamReduce(supplierDatos);
        var parada2 = System.currentTimeMillis();
        StreamCollect(supplierDatos);
        var parada3 = System.currentTimeMillis();

        var tiempo1 = (parada1 - inicio) / 1000d;
        var tiempo2 = (parada2 - parada1) / 1000d;
        var tiempo3 = (parada3 - parada2) / 1000d;
        // En este ejercicio no les voy a mostrar mis tiempos, ya que sucede algo interesante
        // Debido a la carga computacional del problema, y las diferentes configuraciones es injusto.
        //
        // Conclusiones globales
        //  - El reduce es más lento que el collect. Principalmente por la gran carga de la copia de datos.
        //  - A mayor cantidad de datos, el ForkJoin funcionará cada vez mejor, gracias al menor número de tareas.
        System.out.format("Tiempos\n%s\n%s\n%s\n", tiempo1, tiempo2, tiempo3);

    }
}
