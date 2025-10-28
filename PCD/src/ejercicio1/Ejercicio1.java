package ejercicio1;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Clase principal que ejecuta dos hilos en paralelo para realizar operaciones con matrices:
 * uno para suma y otro para multiplicación.
 * 
 * Usa concurrencia segura mediante ReentrantLock para evitar solapamiento en la consola.
 */
public class Ejercicio1 {
	 /**
     * Punto de entrada del programa. Inicia los hilos y espera su finalización.
     *
     * @param args Argumentos de línea de comandos (no utilizados).
     */
   public static void main(String[] args) {
       //Lock para la sincronización entre hilos
       ReentrantLock lock = new ReentrantLock(); 
       
       //Creación de hilos, uno para sumar y otro para multiplicar
       Thread hilo1 = new HiloMultiplicador(lock);
       Thread hilo2 = new HiloSumador(lock);
       
       // Iniciar los hilos
       hilo1.start();
       hilo2.start();
   }
}
