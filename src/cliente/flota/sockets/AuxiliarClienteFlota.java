
package cliente.flota.sockets;
import java.net.*;
import java.io.*;

import comun.flota.sockets.*;

/**
 * Esta clase implementa el intercambio de mensajes
 * asociado a cada una de las operaciones basicas que comunican cliente y servidor
 */

public class AuxiliarClienteFlota {

   private MyStreamSocket mySocket;
   private InetAddress serverHost;
   private int serverPort;

	/**
	 * Constructor del objeto auxiliar del cliente
	 * Crea un socket de tipo 'MyStreamSocket' y establece una conexión con el servidor
	 * 'hostName' en el puerto 'portNum'
	 * @param	hostName	nombre de la máquina que ejecuta el servidor
	 * @param	portNum		numero de puerto asociado al servicio en el servidor
	 */
   AuxiliarClienteFlota(String hostName,
                     String portNum) throws SocketException,
                     UnknownHostException, IOException {
	   this.serverHost=InetAddress.getByName(hostName);
	   this.serverPort=Integer.parseInt(portNum);
	   mySocket=new MyStreamSocket(serverHost,serverPort);
	   
   } // end constructor
   
   /**
	 * Usa el socket para enviar al servidor una petición de fin de conexión
	 * con el formato: "0"
	 * @throws	IOException
	 */
   public void fin( )throws	IOException {
		mySocket.sendMessage("0");
		mySocket.close();
	   
   } // end fin 
  
   /**
    * Usa el socket para enviar al servidor una petición de creación de nueva partida 
    * con el formato: "1#nf#nc#nb"
    * @param nf	número de filas de la partida
    * @param nc	número de columnas de la partida
    * @param nb	número de barcos de la partida
    * @throws IOException
    */
   public void nuevaPartida(int nf, int nc, int nb)  throws IOException {
	 
	   String parametros = 1+"#"+nf+"#"+nc+"#"+nb;
	   mySocket.sendMessage(parametros);
	   
	   
   } // end nuevaPartida

   /**
    * Usa el socket para enviar al servidor una petición de disparo sobre una casilla 
    * con el formato: "2#f#c"
    * @param f	fila de la casilla
    * @param c	columna de la casilla
    * @return	resultado del disparo devuelto por la operación correspondiente del objeto Partida
    * 			en el servidor.
    * @throws IOException
    */
   public int pruebaCasilla(int f, int c) throws IOException {
	
	   String parametros = 2+"#"+f+"#"+c;
	   mySocket.sendMessage(parametros);
	   String mensage=mySocket.receiveMessage();
	   return Integer.parseInt(mensage); 
	   
    } // end pruebaCasilla
   
   /**
    * Usa el socket para enviar al servidor una petición de los datos de un barco
    * con el formato: "3#idBarco"
    * @param idBarco	identidad del Barco
    * @return			resultado devuelto por la operación correspondiente del objeto Partida
    * 					en el servidor.
    * @throws IOException
    */
   public String getBarco(int idBarco) throws IOException {
	   
	   // Por implementar
	   String parametros = 3+"#"+idBarco;
	   mySocket.sendMessage(parametros);
	   String mensage=mySocket.receiveMessage();
	   return mensage;
	   
    } // end getBarco
   
   /**
    * Usa el socket para enviar al servidor una petición de los datos de todos los barcos
    * con el formato: "4"
    * @return	resultado devuelto por la operación correspondiente del objeto Partida
    * 			en el servidor
    * @throws IOException
    */
   public String[] getSolucion() throws IOException {
	   
	   // Por implementar
	   String parametros = Integer.toString(4);
	   mySocket.sendMessage(parametros);
	   int nbarcos=Integer.parseInt(mySocket.receiveMessage());
	   String[] barcos=new String[nbarcos];
	   for(int i=0;i<nbarcos;i++) {
		   barcos[i]=mySocket.receiveMessage();
	   }
	   
	   return barcos;
	   
    } // end getSolucion
   


} //end class
