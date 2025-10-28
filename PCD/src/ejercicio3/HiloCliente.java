package ejercicio3;
import java.util.Random;
/**
* Clase que representa un cliente que solicita un servicio de máquina y lavado en un lavadero.
* Cada cliente se ejecuta como un hilo independiente.
*/
public class HiloCliente extends Thread {
   /** Tiempo que el cliente usará la máquina de autoservicio (en milisegundos). */
   private int tiempoServicio;
   /** Tiempo que el coche del cliente estará en el lavadero (en milisegundos). */
   private int tiempoLavado;
   /** Identificador único del cliente. */
   private int id;
   /** Monitor compartido que gestiona las máquinas disponibles. */
   private Monitor monitor;
   /** Monitor para solicitar escribir en pantalla. */
   private MutexPantalla mutex;
   /** Lavadero compartido donde los coches serán lavados en colas independientes. */
   private Lavadero lavadero;
   /** Número total de colas del lavadero. */
   private final static int NUMERO_COLAS = 4;
   /**
    * Constructor que inicializa los datos del cliente y sus tiempos de espera/lavado.
    *
    * @param id Identificador del cliente.
    * @param monitor Referencia al monitor que controla las máquinas.
    * @param mutex Referencia al monitor de acceso a la pantalla.
    * @param lav Lavadero donde se procesarán los lavados.
    */
   public HiloCliente(int id, Monitor monitor, MutexPantalla mutex, Lavadero lav) {
       this.id = id;
       this.monitor = monitor;
       this.tiempoLavado = tiempoLavado();
       this.tiempoServicio = tiempoMaquina();
       this.mutex = mutex;
       this.lavadero = lav;
   }
   /**
    * Genera un tiempo aleatorio para el lavado del coche.
    *
    * @return Tiempo de lavado en milisegundos (entre 1000 y 2000).
    */
   public int tiempoLavado() {
       int min = 1000;
       int max = 2000;
       Random random = new Random();
       return random.nextInt(max - min + 1) + min;
   }
   /**
    * Genera un tiempo aleatorio para el uso de la máquina de autoservicio.
    *
    * @return Tiempo de servicio en milisegundos (entre 500 y 1000).
    */
   public int tiempoMaquina() {
       int min = 500;
       int max = 1000;
       Random random = new Random();
       return random.nextInt(max - min + 1) + min;
   }
   /**
    * Método que define el comportamiento del hilo cliente:
    * pide una máquina, simula el uso, selecciona una cola de lavado, espera su turno
    * y finalmente libera el lavadero.
    */
   @Override
   public void run() {
       try {
           // Solicitar una máquina del monitor
           int maquina = this.monitor.pedirMaquina();
           // Simular uso de la máquina
           Thread.sleep(this.tiempoServicio);
           // Liberar la máquina
           this.monitor.liberarMaquina(maquina);
           // Mostrar información en pantalla (protegido por mutex)
           mutex.pedirAcceso();
           System.out.println("Cliente " + id + " ha solicitado su servicio en la máquina: " + (maquina + 1));
           System.out.println("Tiempo en solicitar el servicio: " + this.tiempoServicio);
           // Seleccionar el lavadero más conveniente
           int idLavadero = lavadero.seleccionarCola();
           System.out.println("El coche será lavado en el lavadero nº: " + idLavadero);
           System.out.println("Tiempo en lavar el coche: " + this.tiempoLavado);
           mutex.liberarAcceso();
           // Mostrar tiempos de espera en todas las colas
           mutex.pedirAcceso();
           System.out.print("Tiempo de espera en la ");
           for (int i = 0; i < NUMERO_COLAS; i++) {
               System.out.print("lavadero" + i + " = " + lavadero.tiempoCola(i) + " ");
           }
           System.out.println("\n");
           mutex.liberarAcceso();
           // Añadir tiempo de lavado a la cola seleccionada
           lavadero.anadirTiempoLavadero(idLavadero, this.tiempoLavado);
           // Simular el tiempo de lavado
           Thread.sleep(tiempoLavado);
           // Liberar el lavadero
           lavadero.liberarLavadero(idLavadero);
       } catch (InterruptedException e) {
           e.printStackTrace();
       }
   }
}
