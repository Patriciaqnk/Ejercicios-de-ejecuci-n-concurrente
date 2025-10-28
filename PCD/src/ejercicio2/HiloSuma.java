package ejercicio2;

import java.util.concurrent.Semaphore;

/**
 * Clase que representa un hilo que realiza la suma de dos matrices y muestra el resultado
 * en uno de los paneles disponibles, utilizando semáforos para sincronizar el acceso.
 */
public class HiloSuma extends Thread {
    
    /** Número de iteraciones que realiza cada hilo. */
    public static final int NUM_ITERACIONES = 3;

    /** Número de filas que contienen las matrices. */
    public static final int NUM_FILAS = 10;
    
    /** Arreglo de semáforos, uno por cada panel, para controlar el acceso concurrente. */
    private Semaphore[] semaforos;

    /** Arreglo de paneles donde se mostrarán los resultados. */
    private Panel[] paneles;

    /** Identificador del hilo. */
    private final int id;

    /**
     * Constructor de la clase HiloSuma.
     *
     * @param i Identificador del hilo.
     * @param s Arreglo de semáforos correspondientes a los paneles.
     * @param p Arreglo de paneles disponibles para mostrar resultados.
     */
    public HiloSuma(int i, Semaphore[] s, Panel[] p) {
        this.semaforos = s;
        this.paneles = p;
        this.id = i;
    }

    /**
     * Método que ejecuta el hilo. Cada hilo realiza varias iteraciones en las que genera dos matrices,
     * las suma, y muestra el resultado en uno de los paneles disponibles, asegurando exclusión mutua
     * mediante semáforos.
     */
    @Override
    public void run() {
        for (int iter = 0; iter < NUM_ITERACIONES; iter++) {

            // Generamos las matrices A y B con valores aleatorios
            Matriz2 a = new Matriz2();
            Matriz2 b = new Matriz2();
            a.generar();
            b.generar();
            int[][] A = a.getMatriz();
            int[][] B = b.getMatriz();

            // Realizamos la suma de matrices A y B, almacenando el resultado en C
            Matriz2 resultado = new Matriz2(Matriz2.sumar(A, B));

            boolean mostrado = false;

            // Intentamos mostrar el resultado en uno de los paneles disponibles
            while (!mostrado) {
                for (int i = 0; i < paneles.length; i++) {
                    if (semaforos[i].tryAcquire()) { // Intenta adquirir el semáforo sin bloquear
                        try {
                            // Escribimos en el panel la información generada por el hilo
                            paneles[i].escribir_mensaje("Hilo: " + id);
                            paneles[i].escribir_mensaje("Matriz A:");
                            for (int j = 0; j < NUM_FILAS; j++) {
                                paneles[i].escribir_mensaje(a.getLinea(j));
                            }
                            paneles[i].escribir_mensaje("Matriz B:");
                            for (int j = 0; j < NUM_FILAS; j++) {
                                paneles[i].escribir_mensaje(b.getLinea(j));
                            }
                            paneles[i].escribir_mensaje("Matriz C (A+B):");
                            for (int j = 0; j < NUM_FILAS; j++) {
                                paneles[i].escribir_mensaje(resultado.getLinea(j));
                            }
                            mostrado = true;
                            break;
                        } finally {
                            // Liberamos el semáforo tras usar el panel
                            semaforos[i].release();
                        }
                    }
                }
            }
        }
    }
}
