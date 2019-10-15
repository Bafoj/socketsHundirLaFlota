package servidor.flota.sockets;


import java.io.IOException;
import java.net.SocketException;

import partida.flota.sockets.*;
import comun.flota.sockets.MyStreamSocket;

/**
 * Clase ejecutada por cada hebra encargada de servir a un cliente del juego Hundir la flota.
 * El metodo run contiene la logica para gestionar una sesion con un cliente.
 */

 // Revisar el apartado 5.5. del libro de Liu

class HiloServidorFlota implements Runnable {
   MyStreamSocket myDataSocket;
   private Partida partida = null;

	/**
	 * Construye el objeto a ejecutar por la hebra para servir a un cliente
	 * @param	myDataSocket	socket stream para comunicarse con el cliente
	 */
   HiloServidorFlota(MyStreamSocket myDataSocket) {
	   
      // Por implementar
	   
   }
 
   /**
	* Gestiona una sesion con un cliente	
   */
   public void run( ) {
      int operacion = 0;
      boolean done = false;
      // ...
      try {
         while (!done) {
        	 // Recibe una peticion del cliente
        	 // Extrae la operación y los argumentos
                      
             switch (operacion) {
             case 0:  // fin de conexión con el cliente
            	 // ...
            	 break;

             case 1: { // Crea nueva partida
            	 // ...
            	 break;
             }             
             case 2: { // Prueba una casilla y devuelve el resultado al cliente
            	 // ... 
                 break;
             }
             case 3: { // Obtiene los datos de un barco y se los devuelve al cliente
            	 // ... 
                 break;
             }
             case 4: { // Devuelve al cliente la solucion en forma de vector de cadenas
        	   // Primero envia el numero de barcos 
               // Despues envia una cadena por cada barco
               break;
             }
         } // fin switch
       } // fin while   
     } // fin try
     catch (Exception ex) {
        System.out.println("Exception caught in thread: " + ex);
     } // fin catch
   } //fin run
   
} //fin class 
