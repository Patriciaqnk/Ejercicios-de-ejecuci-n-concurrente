package ejercicio1;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Hilo que genera una matriz aleatoria y la suma consigo misma en varias iteraciones.
 * Utiliza un ReentrantLock para imprimir sin interferencia de otros hilos.
 */
class HiloSumador extends Thread {
   private final ReentrantLock lock;		//Sincronizar acceso a pantalla
	public static final int NUM_ITERACIONES = 10;
	
	/**
     * Constructor.
     *
     * @param lock ReentrantLock compartido para sincronización de pantalla.
     */

   public HiloSumador(ReentrantLock lock) {
       this.lock = lock;
   }
   @Override
   public void run() {
       for (int iter = 0; iter < NUM_ITERACIONES; iter++) {
       	//Generamos matriz
           Matriz m = new Matriz();
           m.generar();
          
           int[][] A = m.getMatriz();	//Obtenemos la matriz generada
           int[][] resultado = Matriz.sumar(A);
           lock.lock();	//Bloquear acceso a pantalla
           try {
               System.out.println("Iteración " + (iter + 1) + " - Hilo Sumador (A + A):");
               Matriz.imprimir("Matriz A:", A);
               Matriz.imprimir("Resultado A + A:", resultado);
           } finally {
               lock.unlock();	//Liberar bloqueo
           }
       }
   }
}
