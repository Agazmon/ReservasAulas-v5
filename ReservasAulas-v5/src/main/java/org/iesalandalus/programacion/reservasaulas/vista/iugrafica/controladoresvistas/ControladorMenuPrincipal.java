package org.iesalandalus.programacion.reservasaulas.vista.iugrafica.controladoresvistas;

import java.io.IOException;

import javax.swing.plaf.ActionMapUIResource;

import org.iesalandalus.programacion.reservasaulas.controlador.IControladorReservasAulas;
import org.iesalandalus.programacion.reservasaulas.vista.iugrafica.utilidades.Dialogos;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ControladorMenuPrincipal {
	
	private IControladorReservasAulas controladorMVC;
	private ControladorVentanaAulas cAulas;
	private ControladorVentanaProfesores cProfesores;
	private ControladorVentanaReservas cReservas;
	
	private Stage ventanaAulas;
	private Stage ventanaProfesores;
	private Stage ventanaReservas;

	@FXML private Button btProfesores;
	@FXML private Button btAulas;
	@FXML private Button btReservas;

	public void setControlador(IControladorReservasAulas controladorMVC) {
		this.controladorMVC = controladorMVC;
	}
	
	@FXML
	private void mostrarVentanaAulas() throws IOException {
		crearVentanaAulas();
		ventanaAulas.show();
	}
	
	@FXML
	private void mostrarVentanaProfesores() throws IOException {
		crearVentanaProfesores();
		ventanaProfesores.showAndWait();
	}
	
	@FXML
	private void mostrarVentanaReservas() throws IOException{
		crearVentanaReservas();
		ventanaReservas.showAndWait();
	}
	
	@FXML 
	void salir() {
		if (Dialogos.mostrarDialogoConfirmacion("Salir", "¿Estás seguro de que quieres salir de la aplicación?", null)) {
			controladorMVC.salir();
			System.exit(0);
		}
	}
	
 	@FXML
	private void acercaDe() {
		Alert dialogo = new Alert(AlertType.INFORMATION);
		dialogo.setTitle("Acerca de ...");
		DialogPane panelDialogo = dialogo.getDialogPane();
		panelDialogo.getStylesheets().add(getClass().getResource("../iugventanas.css").toExternalForm());
		panelDialogo.lookupButton(ButtonType.OK).setId("btAceptar");
		VBox contenido = new VBox();
		contenido.setAlignment(Pos.CENTER);
		contenido.setPadding(new Insets(20, 20, 0, 20));
		contenido.setSpacing(20);
		Label lbTextoTarea = new Label("Tarea 09 Programación");
		Label lbTextoCreador = new Label("Alejandro Jesús Gázquez Monedero");
		lbTextoCreador.setStyle("-fx-font: 20 Calibri");
		lbTextoTarea.setStyle("-fx-font: 15 Arial");
		contenido.getChildren().addAll(lbTextoCreador, lbTextoTarea);
		panelDialogo.setHeader(contenido);
		dialogo.showAndWait();
	}

	@FXML
	private void crearVentanaAulas() throws IOException{
		if (ventanaAulas == null) {
			ventanaAulas = new Stage();
			FXMLLoader cargadorVentanaAulas = new FXMLLoader(getClass().getResource("../vistas/VentanaAulas.fxml"));
			VBox raizVentanaAulas = cargadorVentanaAulas.load();
			ControladorVentanaAulas conAulas = cargadorVentanaAulas.getController();
			conAulas.setControlador(controladorMVC);
			conAulas.actualizarDatos();
			Scene escenaVentanaAulas = new Scene(raizVentanaAulas);
			ventanaAulas.setTitle("Administración de Aulas");
			ventanaAulas.initModality(Modality.NONE); 
			ventanaAulas.setScene(escenaVentanaAulas);
		}
	}

	@FXML
	private void crearVentanaProfesores() throws IOException{
		if (ventanaProfesores == null) {
			ventanaProfesores = new Stage();
			FXMLLoader cargadorVentanaProfesores = new FXMLLoader(getClass().getResource("../vistas/VentanaProfesores.fxml"));
			VBox raizVentanaProfesores = cargadorVentanaProfesores.load();
			ControladorVentanaProfesores conProfesores = cargadorVentanaProfesores.getController();
			conProfesores.setControlador(controladorMVC);
			conProfesores.actualizarDatos();
			Scene escenaVentanaProfesores = new Scene(raizVentanaProfesores);
			ventanaProfesores.setTitle("Administración de Profesores");
			ventanaProfesores.initModality(Modality.APPLICATION_MODAL); 
			ventanaProfesores.setScene(escenaVentanaProfesores);
		}
		
	}

	@FXML
	void crearVentanaReservas() throws IOException{
		if (ventanaReservas == null) {
			ventanaReservas = new Stage();
			FXMLLoader cargadorVentanaReservas = new FXMLLoader(getClass().getResource("../vistas/VentanaReservas.fxml"));
			VBox raizVentanaReservas = cargadorVentanaReservas.load();
			ControladorVentanaReservas conReservas = cargadorVentanaReservas.getController();
			conReservas.setControlador(controladorMVC);
			conReservas.actualizarDatos();
			Scene escenaVentanaReservas = new Scene(raizVentanaReservas);
			ventanaReservas.setTitle("Administración de Reservas");
			ventanaReservas.initModality(Modality.APPLICATION_MODAL); 
			ventanaReservas.setScene(escenaVentanaReservas);
		}
	}
}