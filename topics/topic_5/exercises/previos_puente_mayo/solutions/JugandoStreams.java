import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class JugandoStreams {
    public static void main(String[] args) {
        Supplier<IntStream> datos = () -> IntStream.range(25, 50);
        Supplier<Stream<Integer>> datosEnteros = () -> datos.get().boxed();

        // Suma
        var suma = datos.get().sum();
        var sumaEnteros = datosEnteros.get().reduce(0, Integer::sum);
        System.out.format("%s-%s\n", suma, sumaEnteros);

        // Total
        var total = datos.get().count();
        var totalEnteros = datosEnteros.get().count();
        System.out.format("%s-%s\n", total, totalEnteros);

        // Media, se puede dividir directamente, pero voy a realizar con streams
        var media = datos.get().mapToDouble((num) -> num / (double) total).sum();
        var mediaEnteros = datosEnteros.get().map((num) -> num / (double) totalEnteros).reduce(0d, Double::sum);
        System.out.format("%s-%s\n", media, mediaEnteros);

        // Desviación típica
        var desviación = Math.sqrt(datos.get().mapToDouble((num) -> {
            return Math.pow(num - media, 2) / total;
        }).sum());
        var desviaciónEnteros = Math.sqrt(datosEnteros.get().map((num) -> {
            return Math.pow(num - mediaEnteros, 2) / totalEnteros;
        }).reduce(0d, Double::sum));
        System.out.format("%s-%s\n", desviación, desviaciónEnteros);

        // Valores desviados
        var desviados = datos.get().filter((num) -> Math.abs(media - num) > desviación).toArray();
        var desviadosEnteros = datosEnteros.get().filter((num) -> Math.abs(media - num) > desviaciónEnteros).toArray();
        System.out.println(Arrays.toString(desviados));
        System.out.println(Arrays.toString(desviadosEnteros));


        // Resumen de https://www.donquijote.org/es/lengua-espanola/literatura-quijote-resumen/
        Supplier<Stream<String>> lineas = () -> Stream.of(
                "A la pregunta de cuál es el libro más importante escrito en su lengua, cualquier hablante de español responderá sin duda que Don Quijote de la Mancha, de Miguel de Cervantes. Aunque, si la pregunta es si se ha leído entero, entonces no todo el mundo dirá que sí. A continuación, te presentamos un resumen de este magnífico libro para que te animes a leerlo.",
                "Alonso Quijano es un hidalgo -es decir, un noble sin bienes y de escala social baja-, de unos cincuenta años, que vive en algún lugar de La Mancha a comienzos del siglo XVII. Su afición es leer libros de caballería donde se narran aventuras fantásticas de caballeros, princesas, magos y castillos encantados. Se entrega a estos libros con tanta pasión que acaba perdiendo el contacto con la realidad y creyendo que él también puede emular a sus héroes de ficción.",
                "Con este fin, recupera una armadura de sus antepasados y saca del establo a su viejo y desgarbado caballo, al que da el nombre de Rocinante. Como todo caballero, también necesita una dama, por lo que transforma el recuerdo de una campesina de la que estuvo enamorado y le da el nombre de Dulcinea del Toboso. Por último, se cambia el nombre por el de Don Quijote, que rima con el del famoso caballero Lanzarote (Lancelot).",
                "Don quijote sale en busca de aventura. Tiene un aspecto ridículo, pero está decidido a llevar a cabo hazañas heroicas. Sin embargo, aquí comienzan a surgir las primeras diferencias con la realidad: ve una posada y cree que es un castillo; exige al dueño que lo arme caballero en una escena cómica; intenta rescatar a un joven pastor que está siendo azotado por su amo; y ataca también a unos mercaderes que se burlan de él, pero es derribado y herido.");
        var palabras = lineas.get().parallel().flatMap((data) -> Stream.of(data.split("[ ,.\\-]+"))).collect(Collectors.toList());
        // Guardado intermedio para utilizarlo después
        var numeroPalabras = palabras.size();

        Function<String, Long> contarVocales = (palabra) -> {
            return palabra.chars().filter((caracter) -> caracter == 'a' || caracter == 'e' || caracter == 'i' || caracter == 'o' || caracter == 'u').count();
        };
        var vocales = palabras.stream().parallel().map(contarVocales).reduce(0L, Long::sum);

        System.out.format("%s-%s\n", numeroPalabras, vocales);

        // Cambiado a 4 para reducir el espacio
        var palabrasCon4Vocales = palabras.stream().parallel().filter((palabra) -> contarVocales.apply(palabra) > 4).toArray();
        System.out.println(Arrays.toString(palabrasCon4Vocales));
    }
}
