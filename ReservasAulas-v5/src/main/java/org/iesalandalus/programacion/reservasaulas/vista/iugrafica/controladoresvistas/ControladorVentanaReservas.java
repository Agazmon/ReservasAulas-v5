package org.iesalandalus.programacion.reservasaulas.vista.iugrafica.controladoresvistas;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import org.iesalandalus.programacion.reservasaulas.controlador.IControladorReservasAulas;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.vista.iugrafica.controladoresvistas.controladoresReservas.ControladorRealizarReserva;
import org.iesalandalus.programacion.reservasaulas.vista.iugrafica.utilidades.Dialogos;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ControladorVentanaReservas {

	@FXML private TextField tfReservasAula;
	@FXML private TextField tfReservasProfesor;
	
	@FXML private Accordion acOpcion;
	@FXML private TitledPane tpEdicion;
	
	@FXML private Accordion acReservas;
	@FXML private TitledPane tpReservas;
	@FXML private TableView<Reserva> tvReservas;
	@FXML private TableColumn<Reserva, String> tcNombreAulaReserva;
	@FXML private TableColumn<Reserva, Integer> tcCapacidadAulaReserva;
	@FXML private TableColumn<Reserva, String> tcNombreProfesorReserva;
	@FXML private TableColumn<Reserva, String> tcCorreoProfesorReserva;
	@FXML private TableColumn<Reserva, String> tcTelefonoProfesorReserva;
	@FXML private TableColumn<Reserva, String> tcPermanenciaDiaReserva;
	@FXML private TableColumn<Reserva, String> tcPermanenciaReserva;
	@FXML private TableColumn<Reserva, Float> tcPuntosReserva;

	@FXML private Button btRealizar;
	@FXML private Button btCancelar;
	@FXML private Button btVolver;
	
	private ObservableList<Reserva> reservas = FXCollections.observableArrayList();
	private FilteredList<Reserva> reservasFiltradas = new FilteredList<>(reservas, p -> true);
	
	private Stage ventanaRealizarReserva;
	private Stage ventanaCancelarReserva;
	
	private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	private IControladorReservasAulas controladorMVC;
	
	@FXML
	private void initialize() {
		acOpcion.setExpandedPane(tpEdicion);
		acReservas.setExpandedPane(tpReservas);
		tcNombreAulaReserva.setCellValueFactory(reserva -> new SimpleStringProperty(reserva.getValue().getAula().getNombre()));
    	tcCapacidadAulaReserva.setCellValueFactory(reserva -> new SimpleIntegerProperty(reserva.getValue().getAula().getPuestos()).asObject());
    	tcNombreProfesorReserva.setCellValueFactory(reserva -> new SimpleStringProperty(reserva.getValue().getProfesor().getNombre()));
    	tcCorreoProfesorReserva.setCellValueFactory(reserva -> new SimpleStringProperty(reserva.getValue().getProfesor().getCorreo()));
    	tcTelefonoProfesorReserva.setCellValueFactory(reserva -> new SimpleStringProperty(reserva.getValue().getProfesor().getTelefono()));
    	tcPermanenciaDiaReserva.setCellValueFactory(reserva -> new SimpleStringProperty(FORMATO_FECHA.format(reserva.getValue().getPermanencia().getDia())));
    	tcPermanenciaReserva.setCellValueFactory(reserva -> new SimpleStringProperty(reserva.getValue().getPermanencia().toString()));
    	tcPuntosReserva.setCellValueFactory(reserva -> new SimpleFloatProperty(reserva.getValue().getPuntos()).asObject());
    	
    	SortedList<Reserva> reservasOrdenadas = new SortedList<>(reservasFiltradas);
    	reservasOrdenadas.comparatorProperty().bind(tvReservas.comparatorProperty());
    	tvReservas.setItems(reservasOrdenadas);
    	tfReservasAula.textProperty().addListener((ob, ov, nv) -> reservasFiltradas.setPredicate(aula -> {
    		if(nv == null || nv.isEmpty()) {
    			return true;
    		}
    		String nombre = aula.getAula().getNombre().toLowerCase();
    		return nombre.contains(nv.toLowerCase());
    		})
    	);
    	tfReservasAula.focusedProperty().addListener((ob, ov, nv) -> {
    		tfReservasProfesor.setText("");
    	});
    	
    	tfReservasProfesor.textProperty().addListener((ob, ov, nv) -> reservasFiltradas.setPredicate(profesor -> {
    		if(nv == null || nv.isEmpty()) {
    			return true;
    		}
    		String nombre = profesor.getProfesor().getNombre().toLowerCase();
    		return nombre.contains(nv.toLowerCase());
    		})	
    	);
    	tfReservasProfesor.focusedProperty().addListener((ob, ov, nv) -> {
    		tfReservasAula.setText("");
    	});
	}
	
	@FXML
	void salir() {
		if (Dialogos.mostrarDialogoConfirmacion("Salir", "¿Estás seguro de que quieres salir de la aplicación?", null)) {
			controladorMVC.salir();
			System.exit(0);
		}
	}

	@FXML
	void acercaDe() {
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
	void ventanaRealizar() throws IOException {
		ventanaRealizarReserva = new Stage();
		FXMLLoader cargadorVentanaRealizar = new FXMLLoader(getClass().getResource("../vistas/RealizarReserva.fxml"));
		VBox raizVentanaRealizar = cargadorVentanaRealizar.load();
		ControladorRealizarReserva conReservas = cargadorVentanaRealizar.getController();
		conReservas.setControlador(controladorMVC);
		conReservas.inicializarDatos();
		Scene escenaVentanaRealizar = new Scene(raizVentanaRealizar);
		ventanaRealizarReserva.setTitle("Realizar reserva");
		ventanaRealizarReserva.initModality(Modality.WINDOW_MODAL); 
		ventanaRealizarReserva.setScene(escenaVentanaRealizar);
		ventanaRealizarReserva.setResizable(false);
   		ventanaRealizarReserva.showAndWait();
   		if((boolean)btRealizar.getScene().getWindow().isFocused()){
   			actualizarDatos(); 
   		}
	}

	@FXML
	void ventanaCancelar() throws IOException {
		try {
			Reserva reservaSeleccionada = tvReservas.getSelectionModel().getSelectedItem();
			if (reservaSeleccionada == null) {
				Dialogos.mostrarDialogoAdvertencia("Seleccione una reserva", "Seleccione una reserva en la tabla para borrarla");
			} else {
				controladorMVC.anularReserva(reservaSeleccionada);
				Dialogos.mostrarDialogoInformacion("Reserva eleminada", "La reserva ha sido borrada del sistema");
				if((boolean)btRealizar.getScene().getWindow().isFocused()){
					actualizarDatos();
				}
			}
		} catch (Exception e) {
			Dialogos.mostrarDialogoError("Error", e.getMessage());
		}
	}

	@FXML
	void volver() {
		Stage ventana = (Stage) btVolver.getScene().getWindow();
    	ventana.close();
	}

	public void actualizarDatos() {
		reservas.setAll(controladorMVC.getReservas());
	}

	public void setControlador(IControladorReservasAulas controladorMVC) {
		this.controladorMVC = controladorMVC;
	}

}
