package ejercicio2;

import java.util.concurrent.Semaphore;

/**
 * Clase principal que inicia la ejecución del programa.
 * Se crean 3 paneles y semáforos, y se inician 10 hilos que operan sobre ellos.
 */
public class Ejercicio2 {

    /** Número de paneles utilizados en la aplicación. */
    public static int NUM_PANELES = 3;

    /** Número total de hilos que se ejecutarán. */
    public static int NUM_HILOS = 10;

    /**
     * Método principal que configura y lanza los hilos con sus respectivos paneles y semáforos.
     *
     * @param args Argumentos de línea de comandos (no utilizados en esta aplicación).
     */
    public static void main(String[] args) {
        
        // Inicialización de Paneles
        Panel pan1 = new Panel("panel1", 0, 0);
        Panel pan2 = new Panel("panel2", 500, 0);
        Panel pan3 = new Panel("panel3", 1000, 0);
        Panel[] paneles = new Panel[NUM_PANELES];
        paneles[0] = pan1;
        paneles[1] = pan2;
        paneles[2] = pan3;
        
        // Inicialización de Semáforos (uno por panel)
        Semaphore s1 = new Semaphore(1);
        Semaphore s2 = new Semaphore(1);
        Semaphore s3 = new Semaphore(1);
        Semaphore[] semaforos = new Semaphore[NUM_PANELES];
        semaforos[0] = s1;
        semaforos[1] = s2;
        semaforos[2] = s3;
        
        // Inicialización y ejecución de hilos
        HiloSuma[] hilos = new HiloSuma[NUM_HILOS];
        
        for (int i = 0; i < NUM_HILOS; i++) {
            hilos[i] = new HiloSuma(i, semaforos, paneles);
            hilos[i].start();
        }
    }
}
