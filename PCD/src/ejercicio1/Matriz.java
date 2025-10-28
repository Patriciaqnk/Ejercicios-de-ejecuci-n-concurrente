package ejercicio1;
import java.util.Random;
/**
 * Clase para generar una matriz cuadrada de tamaño fijo y realizar operaciones con ella.
 */
class Matriz {
	public static final int TAMANO_MATRIZ = 3;	//Tamaño matriz
	public static final int NUM_RANDOM = 10;
   private int[][] matriz = new int[TAMANO_MATRIZ][TAMANO_MATRIZ]; //Matriz principal
   private Random random = new Random();	//Generar valores aleatorios
  
   /**
    * Rellena la matriz con valores aleatorios entre 0 y 9.
    */
   public void generar() {
       for (int i = 0; i < TAMANO_MATRIZ; i++)
           for (int j = 0; j < TAMANO_MATRIZ; j++)
               matriz[i][j] = random.nextInt(NUM_RANDOM); // números entre 0 y 9
   }
  
   /**
    * Devuelve la matriz generada.
    *
    * @return Matriz generada.
    */
   public int[][] getMatriz() {
       return matriz;
   }
  
   /**
    * Suma una matriz consigo misma.
    *
    * @param A Matriz a sumar.
    * @return Resultado de la suma A + A.
    */
   public static int[][] sumar(int[][] A) {
       int[][] resultado = new int[TAMANO_MATRIZ][TAMANO_MATRIZ];
       for (int i = 0; i < TAMANO_MATRIZ; i++)
           for (int j = 0; j < TAMANO_MATRIZ; j++)
               resultado[i][j] = A[i][j] + A[i][j];
       return resultado;
   }
   
   /**
    * Multiplica una matriz consigo misma (A × A).
    *
    * @param A Matriz a multiplicar.
    * @return Resultado de la multiplicación A × A.
    */
   public static int[][] multiplicar(int[][] A) {
       int[][] resultado = new int[TAMANO_MATRIZ][TAMANO_MATRIZ];
       for (int i = 0; i < TAMANO_MATRIZ; i++)
           for (int j = 0; j < TAMANO_MATRIZ; j++)
               for (int k = 0; k < TAMANO_MATRIZ; k++)
                   resultado[i][j] += A[i][k] * A[k][j];
       return resultado;
   } 
   
   /**
    * Imprime una matriz con título.
    *
    * @param titulo Título a mostrar antes de la matriz.
    * @param A Matriz a imprimir.
    */
   public static void imprimir(String titulo, int[][] A) {
       System.out.println(titulo);
       for (int[] fila : A) {
           for (int valor : fila) {
               System.out.printf("%4d", valor);
           }
           System.out.println();
       }
       System.out.println();
   }
}
