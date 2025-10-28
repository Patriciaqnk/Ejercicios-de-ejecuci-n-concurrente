package ejercicio4;

import messagepassing.*;

/**
 * Clase principal que inicia la simulación de personas comprando y pagando en colas.
 * Gestiona hilos y controla la concurrencia mediante el uso de buzones.
 */
public class Ejercicio4 {
	 public static final int NUM_PERSONAS = 50; //Número personas en la simulación
	    public static Thread[] Personas = new Thread[NUM_PERSONAS]; //Arreglo de hilos
	    /**
	     * Método principal que lanza la ejecución del programa.
	     * Crea los buzones, inicia al controlador y lanza todos los hilos Persona.
	     *
	     * @param args No se utilizan argumentos de entrada.
	     */
	    public static void main(String[] args) {
	        //Buzones para la comunicación
	        MailBox enviar = new MailBox();
	        MailBox colaR = new MailBox();
	        MailBox colaL = new MailBox();
	        MailBox liberar = new MailBox();
	        MailBox pantalla = new MailBox();
	        MailBox[] buzonesPersonas = new MailBox[NUM_PERSONAS];
	       
	        //Se crea el controlador
	        Controlador controlador = new Controlador(enviar, colaR, colaL, liberar, pantalla, buzonesPersonas);
	        controlador.start(); //Lo lanzamos
	        //Creamos los clientes
	        for (int i = 0; i < NUM_PERSONAS; i++) {
	            buzonesPersonas[i] = new MailBox();
	            Personas[i] = new Thread(new Persona(i, enviar, buzonesPersonas[i], colaR, colaL, liberar, pantalla));
	        }
	       
	        //Inicializamos los hilos
	        for (Thread persona : Personas) {
	            persona.start();
	        }
	       
	        //Esperamos a que todas las personas terminen
	        for (Thread persona : Personas) {
	            try {
	                persona.join();
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    }

}
