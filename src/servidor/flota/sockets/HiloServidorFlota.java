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
	   this.myDataSocket=myDataSocket;
   }
 
   /**
	* Gestiona una sesion con un cliente	
   */
   public void run( ) {
      int operacion = 0;
      boolean done = false;
      String[] mensage = null;
      try {
         while (!done) {
        	 // Recibe una peticion del cliente
        	 // Extrae la operación y los argumentos
             mensage = (myDataSocket.receiveMessage()).split("#");
             operacion=Integer.parseInt(mensage[0]);
             switch (operacion) {
             case 0:  // fin de conexión con el cliente
            	 done=true;
            	 myDataSocket.close();
            	 break;

             case 1: { // Crea nueva partida
            	 int nf=Integer.parseInt(mensage[1]);
            	 int nc=Integer.parseInt(mensage[2]);
            	 int nb=Integer.parseInt(mensage[3]);
            	 partida=new Partida(nf,nc,nb);
            	 break;
             }             
             case 2: { // Prueba una casilla y devuelve el resultado al cliente
            	 int fila=Integer.parseInt(mensage[1]);
            	 int columna=Integer.parseInt(mensage[2]);
            	 int res=partida.pruebaCasilla(fila, columna);
            	 myDataSocket.sendMessage(Integer.toString(res));
                 break;
             }
             case 3: { // Obtiene los datos de un barco y se los devuelve al cliente
            	 int id=Integer.parseInt(mensage[1]);
            	 String datos=partida.getBarco(id); //Obtiene la cadena de datos del barco
            	 myDataSocket.sendMessage(datos);
                 break;
             }
             case 4: { // Devuelve al cliente la solucion en forma de vector de cadenas
            	 String[] sol=partida.getSolucion();
        	   // Primero envia el numero de barcos
            	 int nbarcos=sol.length;
            	 myDataSocket.sendMessage(Integer.toString(nbarcos));
               // Despues envia una cadena por cada barco
            	 for(int i=0;i < nbarcos;i++) {
            		 myDataSocket.sendMessage(sol[i]);
            	 }
            	 
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
