package ejercicio3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Clase que simula un sistema de lavado de coches con 4 lavaderos.
 * Cada lavadero tiene una cola controlada mediante condiciones y un control de tiempos acumulados.
 * Se usa programación concurrente con ReentrantLock y Condition.
 */
public class Lavadero {

    /** Candado para proteger el acceso concurrente a los lavaderos. */
    private ReentrantLock l1 = new ReentrantLock();

    /** Condiciones asociadas a cada cola del lavadero. */
    private Condition cola1 = l1.newCondition();
    private Condition cola2 = l1.newCondition();
    private Condition cola3 = l1.newCondition();
    private Condition cola4 = l1.newCondition();

    /** Arreglo que almacena el número de clientes esperando en cada cola. */
    private int[] colas;

    /** Estado de disponibilidad de cada lavadero. */
    private boolean[] libre;

    /** Lista que contiene las condiciones para cada cola. */
    private ArrayList<Condition> arrayColas;

    /**
     * Mapa que almacena el tiempo total de espera acumulado por cola.
     * La clave es el ID del lavadero y el valor es el tiempo.
     */
    private TreeMap<Integer, Integer> tiempoColas;

    /** Número total de lavaderos disponibles. */
    private final static int NUM_LAVADEROS = 4;

    /**
     * Devuelve el tiempo de espera acumulado en la cola de un lavadero dado.
     *
     * @param id Identificador del lavadero.
     * @return Tiempo total de espera acumulado en milisegundos.
     */
    public int tiempoCola(int id) {
        return this.tiempoColas.getOrDefault(id, 0);
    }

    /**
     * Constructor de la clase Lavadero.
     * Inicializa las colas, condiciones, estados de disponibilidad y tiempos.
     */
    public Lavadero() {
        colas = new int[NUM_LAVADEROS];
        arrayColas = new ArrayList<>();
        Collections.addAll(arrayColas, cola1, cola2, cola3, cola4);

        // Inicializar colas y lavaderos como libres
        for (int i = 0; i < NUM_LAVADEROS; i++) {
            colas[i] = 0;
        }
        libre = new boolean[NUM_LAVADEROS];
        for (int i = 0; i < NUM_LAVADEROS; i++) {
            libre[i] = true;
        }

        tiempoColas = new TreeMap<>();
    }

    /**
     * Determina cuál es la cola con menor tiempo de espera acumulado.
     *
     * @return El índice de la cola más vacía.
     */
    private int colaConMenosTiempo() {
        l1.lock();
        int masPequeña = Integer.MAX_VALUE;
        int idColaMasPequeña = 0;
        for (int idcola : this.tiempoColas.keySet()) {
            int actual = tiempoColas.getOrDefault(idcola, 0);
            if (actual < masPequeña) {
                idColaMasPequeña = idcola;
                masPequeña = actual;
            }
        }
        l1.unlock();
        return idColaMasPequeña;
    }

    /**
     * Selecciona el lavadero más adecuado para el siguiente cliente.
     * Si hay algún lavadero libre, se elige ese.
     * En caso contrario, se elige la cola con menor tiempo de espera.
     *
     * @return El ID del lavadero seleccionado.
     */
    public int seleccionarCola() {
        l1.lock();
        int id = -1;
        for (int i = 0; i < this.libre.length; i++) {
            if (libre[i]) {
                id = i; // se mete en el último libre encontrado
            }
        }
        if (id == -1) {
            id = colaConMenosTiempo();
        }
        l1.unlock();
        return id;
    }

    /**
     * Añade el tiempo de lavado a la cola del lavadero y espera su turno si no está libre.
     *
     * @param id ID del lavadero.
     * @param tiempoLavado Tiempo de lavado en milisegundos.
     */
    public void anadirTiempoLavadero(int id, int tiempoLavado) {
        l1.lock();

        // Si el lavadero está ocupado, se suma el tiempo a la cola
        if (!libre[id]) {
            colas[id]++;
            int actual = this.tiempoColas.getOrDefault(id, 0);
            actual += tiempoLavado;
            this.tiempoColas.put(id, actual);
        }

        // Esperar a que el lavadero esté libre
        while (!libre[id]) {
            try {
                arrayColas.get(id).await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Una vez liberado, continuar
        if (colas[id] != 0) {
            colas[id]--; // se retira de la cola
        }

        libre[id] = false;

        // Actualizar tiempo restante de espera
        int actual = this.tiempoColas.getOrDefault(id, 0);
        if (actual != 0) {
            actual -= tiempoLavado;
        }
        this.tiempoColas.put(id, actual);

        l1.unlock();
    }

    /**
     * Libera un lavadero y notifica a los hilos que están esperando en su cola.
     *
     * @param id ID del lavadero que se libera.
     */
    public void liberarLavadero(int id) {
        l1.lock();
        libre[id] = true;
        arrayColas.get(id).signalAll(); // notificar a todos los clientes en espera
        l1.unlock();
    }
}
