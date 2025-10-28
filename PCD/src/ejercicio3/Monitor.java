package ejercicio3;

import java.util.concurrent.locks.*;

/**
 * Monitor que gestiona el acceso concurrente a un conjunto de máquinas de servicio.
 * Solo hay 3 máquinas, y los hilos deben esperar si todas están ocupadas.
 * Utiliza ReentrantLock y Condition.
 */
public class Monitor {

    /** Número de máquinas actualmente ocupadas. */
    private int ocupadas;

    /** Arreglo que indica si cada máquina está libre (true) u ocupada (false). */
    private boolean[] libre;

    /** Lock para proteger el acceso concurrente al monitor. */
    private ReentrantLock l = new ReentrantLock();

    /** Condición utilizada para esperar si no hay máquinas libres. */
    private Condition nolibre = l.newCondition();

    /**
     * Constructor del monitor. Inicializa las 3 máquinas como libres.
     */
    public Monitor() {
        libre = new boolean[3];
        libre[0] = true;
        libre[1] = true;
        libre[2] = true;
        ocupadas = 0;
    }

    /**
     * Método que permite a un hilo solicitar una máquina.
     * Si todas están ocupadas, el hilo espera hasta que una esté disponible.
     *
     * @return El ID de la máquina asignada (0, 1 o 2).
     * @throws InterruptedException si el hilo es interrumpido mientras espera.
     */
    public int pedirMaquina() throws InterruptedException {
        l.lock();
        try {
            // Esperar si todas las máquinas están ocupadas
            while (ocupadas == 3) {
                nolibre.await();
            }

            // Buscar una máquina libre
            int id = 0;
            while (!libre[id]) {
                id++;
            }

            libre[id] = false; // Marcar como ocupada
            ocupadas++;
            return id;
        } finally {
            l.unlock();
        }
    }

    /**
     * Libera la máquina con el ID especificado y notifica a los hilos en espera.
     *
     * @param id ID de la máquina a liberar (0, 1 o 2).
     */
    public void liberarMaquina(int id) {
        l.lock();
        try {
            libre[id] = true;
            ocupadas--;
            nolibre.signalAll(); // Notificar a los hilos en espera
        } finally {
            l.unlock();
        }
    }
}