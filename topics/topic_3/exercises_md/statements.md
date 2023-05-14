# Ejercicios Tema 3

## Ejercicio 1:

Vamos a simular una tienda con un único dependiente. Trabajaremos con dos 
tipos de personas. Tendremos a la persona del mostrador, y a diversos clientes.
La persona del mostrador estará esperando a que una persona llegue. Cuando la 
persona llegue se preguntará que quiere, la otra persona responderá con un 
número hasta el 10.
En función del número devolverá diferentes productos:
Si el valor es menor que 5 devolverá "pan"
Si el valor es mayor o igual que 5 devolverá "leche"

Y entonces la persona preguntará cuánto cuesta, la persona del mostrador 
devolverá un número aleatorio, y se pagará el dinero necesario.
Después de esto la siguiente persona seguirá el mismo proceso.

Todo elemento de este código tendrá que funcionar en un hilo independiente al 
resto.

---

## Ejercicio 2:

Vamos a simular una gasolinera. Esta gasolinera tendrá una persona encargada 
que repostará los vehículos y cobrará a los clientes. Si hay clientes 
esperando a ser cobrados estos tendrán prioridad. Todo vehículo que llegue a 
una de las 4 estaciones de llenado tendrá que esperar 
hasta que exista un hueco en una de ellas.

Una vez llegue el coche avisará a la persona encargada, esta persona irá lo 
antes posible. Una vez rellenado el vehículo, el dueño se irá a la cola para 
realizar el pago. Cuando la persona encargada gestione el pago, el dueño del 
coche se irá dejando el hueco libre.

Todo elemento de este código tendrá que funcionar en un hilo independiente al 
resto.

---

## Ejercicio 3:

Implementar una clase nueva para tener nuestro propio "array" de datos con una
escritura con prioridad. Los escritores tendrán prioridad, mientras que un 
escritor hace su trabajo nadie más estará accediendo o modificando los datos.
Los lectores podrán leer de forma simultanea sin problemas.

Este ejercicio es la implementación del proceso de lectura escritura definido 
en las transparencia de clase.

Todo elemento de este código tendrá que funcionar en un hilo independiente al 
resto.

---

## Ejercicio 4:

Vamos a simular una memoria cache. Queremos que una serie de procesos luchen 
por un espacio en memoria, pero sin producir ningún error indeseado. Los 
escritores se dedicaran a escribir su nombre, pero antes de hacerlo escribirán
por pantalla por quién se han cambiado.

Por otra parte tendremos un hilo por cada posición de memoria que nos irá 
mostrando el valor actualizado.

Debido a la gran concurrencia de la misma queremos que el lector tenga 
prioridad sobre los escritores. Ya que vamos a tener más escritores que
lectores, si no tuviesen prioridad nunca leerían ningún dato.

Cuando un lector lee, todo el mundo podrá leer el valor, pero si hay un 
escritor escribiendo éste tendrá prioridad.

Todo elemento de este código tendrá que funcionar en un hilo independiente al 
resto.

---

## Ejercicio 5:

Vamos a simular la fila única del supermercado. Vamos a tener N cajas 
disponibles para los clientes. Los clientes se pondrán a hacer fila hasta que
exista una cola disponible. Una vez allí realizará un intercambio dando los 
productos a la persona de la caja, y ésta responderá con el precio de los 
artículos. Después el cliente se irá.

Todo elemento de este código tendrá que funcionar en un hilo independiente al 
resto.