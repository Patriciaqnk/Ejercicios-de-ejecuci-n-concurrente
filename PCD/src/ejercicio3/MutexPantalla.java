package ejercicio3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * MutexPantalla es un monitor que implementa exclusión mutua
 * para el acceso a la pantalla con ReentrantLock y Condition.
 */
public class MutexPantalla {

    /** Indica si el recurso está ocupado por algún hilo. */
    private boolean ocupada;

    /** Lock utilizado para garantizar la exclusión mutua. */
    private ReentrantLock l1 = new ReentrantLock();

    /** Condición asociada al lock para esperar cuando el recurso está ocupado. */
    private Condition c = l1.newCondition();

    /**
     * Inicializa el estado del recurso como libre.
     */
    public MutexPantalla() {
        ocupada = false;
    }

    /**
     * Solicita acceso exclusivo al recurso.
     * Si ya está siendo utilizado por otro hilo, espera hasta que esté libre.
     */
    public void pedirAcceso() {
        l1.lock();
        try {
            while (ocupada) {
                try {
                    c.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ocupada = true;
        } finally {
            l1.unlock();
        }
    }

    /**
     * Libera el acceso exclusivo al recurso y notifica a los hilos en espera.
     */
    public void liberarAcceso() {
        l1.lock();
        try {
            ocupada = false;
            c.signal();  // Despierta a un hilo que esté esperando acceso
        } finally {
            l1.unlock();
        }
    }
}
