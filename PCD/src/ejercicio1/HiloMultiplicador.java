package ejercicio1;
import java.util.concurrent.locks.ReentrantLock;
/**
 * Hilo que genera una matriz aleatoria y la multiplica consigo misma en varias iteraciones.
 * Sincroniza la impresión de resultados usando un ReentrantLock.
 */
class HiloMultiplicador extends Thread {
	public static final int NUM_ITERACIONES = 10;	//número de repeticiones
   private final ReentrantLock lock;	//Lock para sincronizar la salida por pantalla
   
   /**
    * Constructor.
    *
    * @param lock Objeto ReentrantLock compartido para sincronizar la salida por pantalla.
    */
   public HiloMultiplicador(ReentrantLock lock) {
       this.lock = lock;
   }
   
   @Override
   public void run() {
       for (int iter = 0; iter < NUM_ITERACIONES ; iter++) { //10 iteraciones
           //Generamos la matriz
       	Matriz m = new Matriz();
           m.generar();
          
           int[][] A = m.getMatriz();	//Obtenemos la matriz generada
           int[][] resultado = Matriz.multiplicar(A);	//Multiplicar matriz por sí misma
           lock.lock();	//Bloquear acceso a pantalla
           try {
               System.out.println("Iteración " + (iter + 1) + " - Hilo Multiplicador (A × A):");
               Matriz.imprimir("Matriz A:", A);
               Matriz.imprimir("Resultado A × A:", resultado);
           } finally {
               lock.unlock();	//Liberar bloqueo
           }
       }
   }
}
