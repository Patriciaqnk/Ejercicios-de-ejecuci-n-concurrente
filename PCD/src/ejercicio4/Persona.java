package ejercicio4;

import messagepassing.*;

/**
 * Clase que representa a una persona que realiza pagos en colas (R o L).
 * Implementa Runnable para que cada persona sea un hilo concurrente.
 */
public class Persona implements Runnable {

    public static final int NUM_RANDOM = 10; // Máximo tiempo aleatorio
    public static final int PAGOS = 5;       // Cantidad de compras/pagos

    private int id; // Identificador único de la persona
    private int tiempoCompra;	//Tiempo que tarda en seleccionar las entradas
    private int tiempoPago;		//Tiempo que tara en pagar
    private MailBox buzonEnvio;	//Buzon para enviar id al controlador
    
    // Buzones de comunicación entre hilos
    private MailBox buzonRespuesta;	//Buzón para recibir respuesta
    private MailBox colaR;		//Buzón para enviar id a la cola rápida
    private MailBox colaL;		//Buzón para enviar id a la cola lenta
    private MailBox liberar;	// Buzón para liberar la cola
    private MailBox imprimir;	//Buzón para imprimir


    private String asignadas;  //Cola asignada a cada persona, R o L

    /**
     * Constructor de Persona
     * @param id ID de la persona
     * @param buzonEnvio buzón para enviar solicitudes al controlador
     * @param buzonRespuesta buzón para recibir la respuesta del controlador
     * @param colaR buzón para cola R
     * @param colaL buzón para cola L
     * @param liberar buzón para liberar la cola
     * @param buzonImprimir buzón para enviar mensajes a imprimir
     */
    public Persona(int id, MailBox buzonEnvio, MailBox buzonRespuesta, MailBox colaR, MailBox colaL,
                   MailBox liberar, MailBox buzonImprimir) {
        this.id = id;
        this.buzonEnvio = buzonEnvio;
        this.buzonRespuesta = buzonRespuesta;
        this.colaR = colaR;
        this.colaL = colaL;
        this.liberar = liberar;
        this.imprimir = buzonImprimir;
    }

    /**
     * Método que define el comportamiento concurrente de la persona:
     * 1. Simula compras.
     * 2. Solicita cola al controlador.
     * 3. Espera confirmación y paga.
     * 4. Libera la cola y notifica al sistema.
     */
    public void run() {
        for (int i = 0; i < PAGOS; i++) {
            //1.- Simula el tiempo de compra
            tiempoCompra = (int) (Math.random() * NUM_RANDOM) + 1;
            try {
                Thread.sleep(tiempoCompra * 100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            buzonEnvio.send(id); // 2.- Envía solicitud al controlador
            
            //Recibe el tiempo de pago y la cola asignada
            String[] partes = buzonRespuesta.receive().toString().split(",");
            tiempoPago = Integer.parseInt(partes[0]);
            asignadas = partes[1];

            // Se pone en la cola asignada
            if (asignadas.equals("R"))
                colaR.send(id);
            else
                colaL.send(id);

            // Espera confirmación de la cola
            buzonRespuesta.receive();

            //3.- Simula el pago
            try {
                Thread.sleep(tiempoPago * 100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //4.- Libera la cola usada
            liberar.send(asignadas);

            //5.- Envía mensaje para impresión
            Object info = "--------------------------\n" +
                          "Persona " + id + " ha usado la cola " + asignadas +
                          "\nTiempo de pago = " + tiempoPago * 100 +
                          "\nThread.sleep(" + tiempoPago*100 + ")" +
                          "\nPersona " + id + " liberando la cola " + asignadas +
                          "\n--------------------------\n";
            imprimir.send(info);
        }
    }
}
