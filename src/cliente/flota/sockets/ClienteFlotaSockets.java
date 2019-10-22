package cliente.flota.sockets;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.*;


public class ClienteFlotaSockets {
	/**
	 * Implementa el juego 'Hundir la flota' mediante una interfaz gr谩fica (GUI)
	 */

	/** Parametros por defecto de una partida */
	public static final int NUMFILAS=8, NUMCOLUMNAS=8, NUMBARCOS=6;

	private GuiTablero guiTablero = null;			// El juego se encarga de crear y modificar la interfaz gr谩fica
	private AuxiliarClienteFlota partida = null;                 // Objeto con los datos de la partida en juego
	
	/** Atributos de la partida guardados en el juego para simplificar su implementaci贸n */
	private int quedan = NUMBARCOS, disparos = 0;

	/**
	 * Programa principal. Crea y lanza un nuevo juego
	 * @param args
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws SocketException 
	 */
	public static void main(String[] args) throws SocketException, UnknownHostException, IOException {
		ClienteFlotaSockets juego = new ClienteFlotaSockets();
		juego.ejecuta();
	} // end main

	/**
	 * Lanza una nueva hebra que crea la primera partida y dibuja la interfaz grafica: tablero
	 */
	private void ejecuta() {
		// Instancia la primera partida
		try {
			partida = new AuxiliarClienteFlota("localhost","13");
			partida.nuevaPartida(NUMFILAS, NUMCOLUMNAS, NUMBARCOS);
		}catch(Exception e) {
			e.printStackTrace();
		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				guiTablero = new GuiTablero(NUMFILAS, NUMCOLUMNAS);
				guiTablero.dibujaTablero();
			}
		});
	} // end ejecuta

	/******************************************************************************************/
	/*********************  CLASE INTERNA GuiTablero   ****************************************/
	/******************************************************************************************/
	private class GuiTablero {

		private int numFilas, numColumnas;

		private JFrame frame = null;        // Tablero de juego
		private JLabel estado = null;       // Texto en el panel de estado
		private JButton buttons[][] = null; // Botones asociados a las casillas de la partida

		/**
         * Constructor de una tablero dadas sus dimensiones
         */
		GuiTablero(int numFilas, int numColumnas) {
			this.numFilas = numFilas;
			this.numColumnas = numColumnas;
			frame = new JFrame();
			frame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					try {
						partida.fin();
					}catch(IOException ex){
						ex.printStackTrace();
					}
					liberaRecursos();
					System.exit(0);
				}
			});	
		}

		/**
		 * Dibuja el tablero de juego y crea la partida inicial
		 */
		public void dibujaTablero() {
			anyadeMenu();
			anyadeGrid(numFilas, numColumnas);		
			anyadePanelEstado("Intentos: " + disparos + "    Barcos restantes: " + quedan);		
			frame.setSize(300, 300);
			frame.setVisible(true);	
		} // end dibujaTablero

		/**
		 * Anyade el menu de opciones del juego y le asocia un escuchador
		 */
		private void anyadeMenu() {
            // POR IMPLEMENTAR
			MenuListener menuListener=new MenuListener();
			JPanel panelMenu = new JPanel();
			panelMenu.setLayout(new GridLayout());
			JMenuBar menu=new JMenuBar();
			JMenu titulo =new JMenu("Opciones");
			JMenuItem nuevo=new JMenuItem("Nueva partida");
			nuevo.addActionListener(menuListener);
			JMenuItem solucion=new JMenuItem("Muestra solucion");
			solucion.addActionListener(menuListener);
			JMenuItem salir=new JMenuItem("Salir");
			salir.addActionListener(menuListener);
			menu.add(titulo);
			titulo.add(nuevo);
			titulo.add(solucion);
			titulo.add(salir);
			titulo.add(menu);
			panelMenu.add(menu);
			frame.getContentPane().add(panelMenu, BorderLayout.NORTH);
			} // end anyadeMenu

		/**
		 * Anyade el panel con las casillas del mar y sus etiquetas.
		 * Cada casilla sera un boton con su correspondiente escuchador
		 * @param nf	numero de filas
		 * @param nc	numero de columnas
		 */
		private void anyadeGrid(int nf, int nc) {
			int filas = nf+1;
			int col=nc+2;
			buttons=new JButton[nf][nc];
			char letra= 'A';
			ButtonListener escuchadorBoton=new ButtonListener();
			JPanel tablero = new JPanel();
			tablero.setLayout(new GridLayout(filas,col));
			tablero.add(new JLabel());
			for(int i=1;i<col-1;i++) {
				JLabel num=new JLabel(i+"");
				num.setHorizontalAlignment(JLabel.CENTER);
				tablero.add(num);
			}
			tablero.add(new JLabel());
			for(int i=1;i<filas;i++) {
				JLabel left=new JLabel(""+letra);
				left.setHorizontalAlignment(JLabel.CENTER);
				tablero.add(left);
				for(int j=1;j<col-1;j++) {
					JButton boton= new JButton();
					boton.addActionListener(escuchadorBoton);
					boton.putClientProperty(boton,(i-1)+"#"+(j-1));
					buttons[i-1][j-1]=boton;
					tablero.add(boton);
				}
				JLabel right=new JLabel(""+letra);
				right.setHorizontalAlignment(JLabel.CENTER);
				tablero.add(right);
				letra++;
			}
			frame.add(tablero,BorderLayout.CENTER);
			
		} // end anyadeGrid


		/**
		 * Anyade el panel de estado al tablero
		 * @param cadena	cadena inicial del panel de estado
		 */
		private void anyadePanelEstado(String cadena) {	
			JPanel panelEstado = new JPanel();
			estado = new JLabel(cadena);
			panelEstado.add(estado);
			// El panel de estado queda en la posici贸n SOUTH del frame
			frame.getContentPane().add(panelEstado, BorderLayout.SOUTH);
		} // end anyadePanel Estado

		/**
		 * Cambia la cadena mostrada en el panel de estado
		 * @param cadenaEstado	nuevo estado
		 */
		public void cambiaEstado(String cadenaEstado) {
			estado.setText(cadenaEstado);
		} // end cambiaEstado
		/**
		 * Actualiza el texto del jlabel dependiendo de la fase de la partida
		 */
		private void actualizaEstado() {
			if(quedan>0) 
			cambiaEstado("Intentos: " + disparos + "    Barcos restantes: " + quedan);
			else
				cambiaEstado("Intentos: " + disparos + "    　GAME OVER!!" );
		}
		/**
		 * Muestra la solucion de la partida y marca la partida como finalizada
		 */
		public void muestraSolucion() {
            // POR IMPLEMENTAR
			for(JButton fila[]:buttons) {
				for(JButton boton:fila) {
					pintaBoton(boton, Color.BLUE);
				}
				
			}
			try {
			for(String barco:partida.getSolucion()) {
				 pintaBarco(barco, Color.GREEN);
			}
			}catch (IOException e) {
				e.printStackTrace();
			}
			quedan=0;
			actualizaEstado();
			
		} // end muestraSolucion
		/**
		 * Pinta todas las casillas que ocupa un barco de un color pasado como par醡etro
		 * @param cadenaBarco
		 * @param color
		 */
		private void pintaBarco(String cadenaBarco,Color color) {
			String[] vectorBarco= cadenaBarco.split("#");
            int cont=Integer.parseInt(vectorBarco[3]);
            int filaInit=Integer.parseInt(vectorBarco[0]);
            int colInit=Integer.parseInt(vectorBarco[1]);
            do {
            	pintaBoton(buttons[filaInit][colInit], color);
            	switch(vectorBarco[2]) {
            	case "H": 
            		colInit++;
            		break;
            	case "V":
            		filaInit++;
            		break;
            	}
            	cont--;
            } while(cont!=0);
		}
		/**
		 * Pinta un barco como hundido en el tablero
		 * @param cadenaBarco	cadena con los datos del barco codifificados como
		 *                    "filaInicial#columnaInicial#orientacion#tamanyo"
		 */
		public void pintaBarcoHundido(String cadenaBarco) {
			pintaBarco(cadenaBarco, Color.RED);	
            
		} // end pintaBarcoHundido

		/**
		 * Pinta un bot贸n de un color dado
		 * @param b			boton a pintar
		 * @param color		color a usar
		 */
		public void pintaBoton(JButton b, Color color) {
			b.setBackground(color);
			// El siguiente c贸digo solo es necesario en Mac OS X
			b.setOpaque(true);
			b.setBorderPainted(false);
		} // end pintaBoton

		/**
		 * Limpia las casillas del tablero pint谩ndolas del gris por defecto
		 */
		public void limpiaTablero() {
			for (int i = 0; i < numFilas; i++) {
				for (int j = 0; j < numColumnas; j++) {
					buttons[i][j].setBackground(null);
					buttons[i][j].setOpaque(true);
					buttons[i][j].setBorderPainted(true);
				}
			}
		} // end limpiaTablero

		/**
		 * 	Destruye y libera la memoria de todos los componentes del frame
		 */
		public void liberaRecursos() {
			frame.dispose();
		} // end liberaRecursos


	} // end class GuiTablero

	/******************************************************************************************/
	/*********************  CLASE INTERNA MenuListener ****************************************/
	/******************************************************************************************/

	/**
	 * Clase interna que escucha el menu de Opciones del tablero
	 * 
	 */
	private class MenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
            // POR IMPLEMENTAR
			JMenuItem boton=(JMenuItem)e.getSource();
			String texto=boton.getText();
			switch (texto) {
			case "Nueva partida":
				guiTablero.limpiaTablero();
				try {
					partida.nuevaPartida(NUMFILAS, NUMCOLUMNAS, NUMBARCOS);
				}catch(IOException ex) {
					ex.printStackTrace();
				}
				quedan=NUMBARCOS;
				disparos=0;
				guiTablero.actualizaEstado();
				break;
			case "Muestra solucion":
				guiTablero.muestraSolucion();
				break;
			case "Salir":
				try {
					partida.fin();
				}catch(IOException ex){
					ex.printStackTrace();
				}
				guiTablero.liberaRecursos();
				System.exit(0);
				break;
			
			}
		} // end actionPerformed

	} // end class MenuListener



	/******************************************************************************************/
	/*********************  CLASE INTERNA ButtonListener **************************************/
	/******************************************************************************************/
	/**
	 * Clase interna que escucha cada uno de los botones del tablero
	 * Para poder identificar el boton que ha generado el evento se pueden usar las propiedades
	 * de los componentes, apoyandose en los metodos putClientProperty y getClientProperty
	 */
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
            // POR IMPLEMENTAR
			try {
				if(quedan>0) {
				JButton boton = (JButton)e.getSource();
				String[] cadena=((String)boton.getClientProperty(boton)).split("#");
				int res=partida.pruebaCasilla(Integer.parseInt(cadena[0]), Integer.parseInt(cadena[1]));
				switch (res) {
				case -1: //AGUA
					guiTablero.pintaBoton(boton, Color.BLUE);
					break;
				case -2: //TOCADO
					guiTablero.pintaBoton(boton, Color.ORANGE);
					break;
				case -3: //HUNDIDO
					break;
				default:
					guiTablero.pintaBarcoHundido(partida.getBarco(res));
					quedan--;
					break;
				}
				disparos++;
				guiTablero.actualizaEstado();
				}
			}catch (IOException ex) {
				ex.printStackTrace();
			}
        } // end actionPerformed

	} // end class ButtonListener



} // end class Juego

