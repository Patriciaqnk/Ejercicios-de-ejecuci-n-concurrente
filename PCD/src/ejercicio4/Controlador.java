package ejercicio4;
import messagepassing.*;
/**
 * Clase que representa al controlador central del sistema.
 * Se encarga de asignar colas a las personas, controlar el acceso concurrente y gestionar la liberación.
 */
public class Controlador extends Thread {
	private MailBox enviar, colaR, colaL, liberar, pantalla;
	   private boolean colaRLibre = true, colaLLibre = true; //Estados de las colas
	   private Selector s;	//Para manejar múltiples buzones
	   private volatile boolean activo = true;	//Controlar el flujo de ejecución del hilo
	   private MailBox[] buzonesPersonas; // Buzones individuales por persona
	   
	   /**
	     * Constructor del controlador.
	     * 
	     * @param enviar Buzón de solicitudes de personas
	     * @param colaR Cola derecha
	     * @param colaL Cola izquierda
	     * @param liberar Buzón de liberación de cola
	     * @param pantalla Buzón para imprimir mensajes
	     * @param buzonesPersonas Arreglo de buzones para responder a cada persona
	     */
	   
	   public Controlador(MailBox enviar, MailBox colaR, MailBox colaL, MailBox liberar, MailBox pantalla, MailBox[] buzonesPersonas) {
	       this.s = new Selector();
	       this.enviar = enviar;
	       this.liberar = liberar;
	       this.colaR = colaR;
	       this.colaL = colaL;
	       this.pantalla = pantalla;
	       this.buzonesPersonas = buzonesPersonas;
	      
	       //Añadir buzones al selector
	       s.addSelectable(enviar, false);
	       s.addSelectable(colaR, false);
	       s.addSelectable(colaL, false);
	       s.addSelectable(liberar, false);
	       s.addSelectable(pantalla, false);
	   }
	   /**
	     * Permite detener el bucle principal del hilo controlador.
	     */
	   public void terminar() {
	       activo = false;
	   }

	   /**
	     * Bucle principal del controlador. Atiende mensajes según disponibilidad de colas y realiza asignaciones.
	     * Utiliza selector para evitar bloqueo activo.
	     */
	   public void run() {
	       while (activo) {
	       	//Actualiza las condiciones
	           colaR.setGuardValue(colaRLibre);
	           colaL.setGuardValue(colaLLibre);
	           switch (s.selectOrBlock()) {
	               case 1: // solicitud cola
	                   Object id = enviar.receive();
	                   String cajaAsignada;
	                   int tiempoPago = (int) (Math.random() * 10) + 1;
	                   cajaAsignada = (tiempoPago >= 5) ? "R" : "L";
	                   String respuesta = tiempoPago + "," + cajaAsignada;
	                   buzonesPersonas[(int) id].send(respuesta);
	                   break;
	               case 2: // Entrada a cola R
	                   id = colaR.receive();
	                   colaRLibre = false;
	                   buzonesPersonas[(int) id].send("ok");
	                   break;
	               case 3: // Entrada a cola L
	                   id = colaL.receive();
	                   colaLLibre = false;
	                   buzonesPersonas[(int) id].send("ok");
	                   break;
	               case 4: // liberar
	                   Object cola = liberar.receive();
	                   if (cola.equals("R")) colaRLibre = true;
	                   else colaLLibre = true;
	                   break;
	               case 5: // imprimir
	                   Object mensaje = pantalla.receive();
	                   System.out.println(mensaje);
	                 
	                   break;
	           }
	       }
	   }
}
