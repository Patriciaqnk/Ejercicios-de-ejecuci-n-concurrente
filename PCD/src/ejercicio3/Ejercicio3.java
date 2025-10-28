package ejercicio3;

/**
 * Clase principal que simula la llegada de múltiples clientes a un lavadero de coches.
 */
public class Ejercicio3 {

    /** Número total de clientes que serán creados y ejecutados como hilos. */
    public static final int NUM_CLIENTES = 50;

    /**
     * Método principal que inicializa los monitores y lanza los hilos clientes.
     *
     * @param args Argumentos de la línea de comandos (no utilizados en esta aplicación).
     */
    public static void main(String[] args) {

        // Inicialización de monitores
        Monitor monitorMaquinas = new Monitor();
        MutexPantalla monitorPantalla = new MutexPantalla();
        Lavadero monitorLavadero = new Lavadero();

        // Creación y ejecución de hilos clientes
        HiloCliente[] clientes = new HiloCliente[NUM_CLIENTES];

        for (int i = 0; i < NUM_CLIENTES; i++) {
            clientes[i] = new HiloCliente(i, monitorMaquinas, monitorPantalla, monitorLavadero);
            clientes[i].start();
        }
    }
}

