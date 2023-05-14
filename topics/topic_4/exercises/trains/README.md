# Trains

Ejercicio para emular el funcionamiento de un sistema de trenes aplicando principios de la programación concurrente.
 
# Objetivo

Completar cada uno de los ejercicios propuestos en los `pfd` de la carpeta `docs` en este repositorio.

### Ejercicios

- [ ] **Ejercicio 1**: El monitor debe controlar que en el tramo de vía compartido nunca hay más de 1 tren en cada momento. Es decir, o está vacío o está ocupado  con 1 tren.

- [ ] **Ejercicio 2**: Al igual que en el ejercicio 1, no puede haber más de un tren dentro. Pero además, cuando un tren sale, tiene prefrencia para entrar un tren que esté esperando en dirección opouesta.

- [ ] **Ejercicio 3**: Se permite que haya varios trenes circulando en la misma dirección por el tramo compartido. Se trata de optimizar el uso del recurso compartido.

- [ ] **Ejercicio 4**: Al igual que el ejercicio 3, puede haber varios trenes circulando en el mismo sentido. Pero además, cuando un tren sale, tiene preferencia para entrar un tren que esté esperandoen dirección opuesta. Es decir, que un nuevo tren sólo entra si no hay nadie esperando en sentido contrario y si no hay nadie circulando en sentido contrario. Se trata de hacer que el recurso se comparta de forma equitativa (fair).
