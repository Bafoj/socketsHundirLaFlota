package servidor.flota.sockets;

import java.io.IOException;
import java.net.ServerSocket;

import comun.flota.sockets.MyStreamSocket;

/**
 * Este modulo contiene la logica de aplicacion del servidor del juego Hundir la flota
 * Utiliza sockets en modo stream para llevar a cabo la comunicacion entre procesos.
 * Puede servir a varios clientes de modo concurrente lanzando una hebra para atender a cada uno de ellos.
 * Se le puede indicar el puerto del servidor en linea de ordenes.
 */


public class ServidorFlotaSockets {
   
   public static void main(String[] args) {
	   
	  // Acepta conexiones vÃ­a socket de distintos clientes.
	  // Por cada conexiÃ³n establecida lanza una hebra de la clase HiloServidorFlota.
	   int puerto=13;
	   try {
			ServerSocket servidor=new ServerSocket(puerto);
			System.out.println("Servidor flota listo");
			while(true) {
				System.out.println("Esperando conexión");
				MyStreamSocket socket = new MyStreamSocket(servidor.accept());
				System.out.println("Conexión aceptada");
				Thread hilo=new Thread(new HiloServidorFlota(socket));
				hilo.start();
			}
	   } catch (IOException e) {
		   e.printStackTrace();
	   }
	   

	  // Revisad el apartado 5.5 del libro de Liu
 
   } //fin main
} // fin class
