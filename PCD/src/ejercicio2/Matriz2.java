package ejercicio2;

import java.util.Random;

/**
 * Clase que representa una matriz cuadrada de enteros y proporciona métodos para
 * generar valores aleatorios, sumar matrices, imprimirlas y obtener líneas como texto.
 */
class Matriz2 {

    /** Tamaño de la matriz (número de filas y columnas). */
    public static final int TAMANO_MATRIZ = 10;

    /** Límite para los valores aleatorios generados. */
    public static final int NUM_RANDOM = 10;

    /** Matriz de enteros representada como arreglo bidimensional. */
    private int[][] matriz = new int[TAMANO_MATRIZ][TAMANO_MATRIZ];

    /** Semilla de números aleatorios. */
    private Random random = new Random();

    /**
     * Constructor por defecto. Inicializa la matriz sin valor.
     */
    public Matriz2() {
        
    }

    /**
     * Constructor que recibe una matriz preexistente para asignarla internamente.
     *
     * @param a Matriz de enteros que se utilizará como base.
     */
    public Matriz2(int[][] a) {
        this.matriz = a;
    }

    /**
     * Genera números aleatorios para llenar la matriz.
     */
    public void generar() {
        for (int i = 0; i < TAMANO_MATRIZ; i++) {
            for (int j = 0; j < TAMANO_MATRIZ; j++) {
                matriz[i][j] = random.nextInt(NUM_RANDOM);
            }
        }
    }

    /**
     * Retorna la matriz actual.
     *
     * @return Matriz de enteros.
     */
    public int[][] getMatriz() {
        return matriz;
    }

    /**
     * Realiza la suma de dos matrices A y B.
     *
     * @param A Primera matriz de enteros.
     * @param B Segunda matriz de enteros.
     * @return Nueva matriz con la suma de A y B.
     */
    public static int[][] sumar(int[][] A, int[][] B) {
        int[][] resultado = new int[TAMANO_MATRIZ][TAMANO_MATRIZ];
        for (int i = 0; i < TAMANO_MATRIZ; i++) {
            for (int j = 0; j < TAMANO_MATRIZ; j++) {
                resultado[i][j] = A[i][j] + B[i][j];
            }
        }
        return resultado;
    }

    /**
     * Devuelve una línea de la matriz como una cadena de texto con los valores separados por espacios.
     *
     * @param l Índice de la fila deseada (0 ≤ l < TAMANO_MATRIZ).
     * @return Cadena de texto con los valores de la fila separados por espacios.
     */
    public String getLinea(int l) {
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < TAMANO_MATRIZ; i++) {
            str.append(matriz[l][i] + " ");
        }
        return str.toString();
    }
}
