package org.iesalandalus.programacion.reservasaulas.vista.iugrafica;

import org.iesalandalus.programacion.reservasaulas.controlador.IControladorReservasAulas;
import org.iesalandalus.programacion.reservasaulas.vista.IVistaReservasAulas;
import org.iesalandalus.programacion.reservasaulas.vista.iugrafica.controladoresvistas.ControladorMenuPrincipal;
import org.iesalandalus.programacion.reservasaulas.vista.iugrafica.utilidades.Dialogos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class VistaReservasAulas extends Application implements IVistaReservasAulas {

	private IControladorReservasAulas controladorMVC = null;
	private static VistaReservasAulas instancia = null;
	
	public VistaReservasAulas() {
		if (instancia != null) {
			controladorMVC = instancia.controladorMVC;
		} else {
			instancia = this;
		}
	}

	@Override
	public void start(Stage menuPrincipal) {
		try {
			FXMLLoader cargadorMenuPrincipal = new FXMLLoader(getClass().getResource("vistas/MenuPrincipal.fxml"));
			VBox raiz = cargadorMenuPrincipal.load();
			ControladorMenuPrincipal cMenuPrincipal = cargadorMenuPrincipal.getController();
			cMenuPrincipal.setControlador(controladorMVC);
			Scene escena = new Scene(raiz);
			menuPrincipal.setOnCloseRequest(e -> confirmarSalida(menuPrincipal, e));
			menuPrincipal.setTitle("Programa Reservas Aulas");
			menuPrincipal.setScene(escena);
			menuPrincipal.setResizable(false);
			menuPrincipal.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	private void confirmarSalida(Stage menuPrincipal, WindowEvent e) {
		if (Dialogos.mostrarDialogoConfirmacion("Salir", "¿Desea salir del programa?", menuPrincipal)) {
			controladorMVC.salir();
			menuPrincipal.close();
		} else {
			e.consume();
		}
	}

	@Override
	public void setControlador(IControladorReservasAulas controlador) {
		this.controladorMVC = controlador;
	}

	@Override
	public void comenzar() {
		launch(this.getClass());
	}

}
