package org.iesalandalus.programacion.reservasaulas.vista.iugrafica.controladoresvistas;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import org.iesalandalus.programacion.reservasaulas.controlador.IControladorReservasAulas;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.vista.iugrafica.controladoresvistas.controladoresAulas.ControladorAnadirAula;
import org.iesalandalus.programacion.reservasaulas.vista.iugrafica.controladoresvistas.controladoresAulas.ControladorBorrarAula;
import org.iesalandalus.programacion.reservasaulas.vista.iugrafica.controladoresvistas.controladoresAulas.ControladorDisponibilidadAula;
import org.iesalandalus.programacion.reservasaulas.vista.iugrafica.utilidades.Dialogos;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ControladorVentanaAulas {

    @FXML private Button btAnadir;
    @FXML private Button btBorrar;
    @FXML private Button btVolver;
    @FXML private Button btDisponibilidad;
    @FXML private Button btActualizar;
    
    @FXML private TableView<Aula> tvAulas;
    @FXML private TableColumn<Aula, String> tcNombreAula;
    @FXML private TableColumn<Aula, Integer> tcCapacidadAula;
   
    @FXML private TableView<Reserva> tvReservasAulas;
    @FXML private TableColumn<Reserva, String> tcNombreAulaReserva;
    @FXML private TableColumn<Reserva, Integer> tcCapacidadAulaReserva;
    
    @FXML private TableColumn<Reserva, String> tcNombreProfesorReserva;
    @FXML private TableColumn<Reserva, String> tcCorreoProfesorReserva;
    @FXML private TableColumn<Reserva, String> tcTelefonoProfesorReserva;
    
    @FXML private TableColumn<Reserva, String> tcPermanenciaDiaReserva;
    @FXML private TableColumn<Reserva, String> tcPermanenciaReserva;
    @FXML private TableColumn<Reserva, Float> tcPuntosReserva;
    
    @FXML private TextField tfReservasAula;
    @FXML private TextField tfAula;
    @FXML private Accordion acAulasReservas;
    @FXML private TitledPane tpAulas;
    
    private ObservableList<Aula> aulas = FXCollections.observableArrayList();
    private ObservableList<Reserva> reservasAula = FXCollections.observableArrayList();
    private FilteredList<Aula> aulasFiltradas = new FilteredList<>(aulas, p -> true);
    private FilteredList<Reserva> reservasAulaFiltradas = new FilteredList<>(reservasAula, p -> true);
    
    private Stage ventanaAnadir;
    private Stage ventanaBorrar;
    private Stage ventanaDisponibilidad;
    
    private IControladorReservasAulas controladorMVC;
    
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    private TextField tfCapacidad;
    
    public void setControlador(IControladorReservasAulas controladorMVC) {
    	this.controladorMVC = controladorMVC;
    }
    
    @FXML
    private void initialize() {    	
    	tcNombreAula.setCellValueFactory(aula -> new SimpleStringProperty(aula.getValue().getNombre()));
    	tcCapacidadAula.setCellValueFactory(aula -> new SimpleIntegerProperty(aula.getValue().getPuestos()).asObject());
    	
    	tcNombreAulaReserva.setCellValueFactory(reserva -> new SimpleStringProperty(reserva.getValue().getAula().getNombre()));
    	tcCapacidadAulaReserva.setCellValueFactory(reserva -> new SimpleIntegerProperty(reserva.getValue().getAula().getPuestos()).asObject());
    	tcNombreProfesorReserva.setCellValueFactory(reserva -> new SimpleStringProperty(reserva.getValue().getProfesor().getNombre()));
    	tcCorreoProfesorReserva.setCellValueFactory(reserva -> new SimpleStringProperty(reserva.getValue().getProfesor().getCorreo()));
    	tcTelefonoProfesorReserva.setCellValueFactory(reserva -> new SimpleStringProperty(reserva.getValue().getProfesor().getTelefono()));
    	tcPermanenciaDiaReserva.setCellValueFactory(reserva -> new SimpleStringProperty(FORMATO_FECHA.format(reserva.getValue().getPermanencia().getDia())));
    	tcPermanenciaReserva.setCellValueFactory(reserva -> new SimpleStringProperty(reserva.getValue().getPermanencia().toString()));
    	tcPuntosReserva.setCellValueFactory(reserva -> new SimpleFloatProperty(reserva.getValue().getPuntos()).asObject());
    	
    	SortedList<Aula> aulasOrdenadas = new SortedList<>(aulasFiltradas);
    	aulasOrdenadas.comparatorProperty().bind(tvAulas.comparatorProperty());
    	tvAulas.setItems(aulasOrdenadas);
    	SortedList<Reserva> reservasAulaOrdenadas = new SortedList<>(reservasAulaFiltradas);
    	reservasAulaOrdenadas.comparatorProperty().bind(tvReservasAulas.comparatorProperty());
    	tvReservasAulas.setItems(reservasAulaOrdenadas);
    	acAulasReservas.setExpandedPane(tpAulas);
    	tfAula.textProperty().addListener((ob, ov, nv) -> 
		aulasFiltradas.setPredicate(aula -> {
			if (nv == null || nv.isEmpty()) {
				return true;
			}
			String nombre = aula.getNombre().toLowerCase();
			return nombre.contains(nv.toLowerCase());
			})
    	);
    	
    	tfReservasAula.textProperty().addListener((ob,ov,nv)->
    	reservasAulaFiltradas.setPredicate(reserva -> {
    		if (nv == null || nv.isEmpty()) {
    			return true;
    		}
    		String nombre = reserva.getAula().getNombre().toLowerCase();
    		return nombre.contains(nv.toLowerCase());
    		})
    	
    	);
    	
    }
    @FXML
    void salir(ActionEvent event) {
    	if (Dialogos.mostrarDialogoConfirmacion("Salir", "¿Estás seguro de que quieres salir de la aplicación?", null)) {
			controladorMVC.salir();
			System.exit(0);
		}
    }

    @FXML
    void acercaDe(ActionEvent event) {
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
    void ventanaAnadir(ActionEvent event) throws IOException {
    	ventanaAnadir = new Stage();
		FXMLLoader cargadorVentanaInsertar = new FXMLLoader(getClass().getResource("../vistas/AnadirAula.fxml"));
		GridPane raizVentanaInsertar = cargadorVentanaInsertar.load();
		ControladorAnadirAula conAulas = cargadorVentanaInsertar.getController();
		conAulas.setControlador(controladorMVC);
		Scene escenaVentanaInsertar = new Scene(raizVentanaInsertar);
		ventanaAnadir.setTitle("Añadir aula");
		ventanaAnadir.initModality(Modality.WINDOW_MODAL); 
		ventanaAnadir.setScene(escenaVentanaInsertar);
		ventanaAnadir.setResizable(false);
   		ventanaAnadir.showAndWait();
   		if((boolean)btAnadir.getScene().getWindow().isFocused()){
   			actualizarDatos(); 
   		}
    }

    @FXML
    void ventanaBorrar(ActionEvent event) throws IOException {
    	ventanaBorrar = new Stage();
		FXMLLoader cargadorVentanaBorrar = new FXMLLoader(getClass().getResource("../vistas/BorrarAula.fxml"));
		GridPane raizVentanaBorrar = cargadorVentanaBorrar.load();
		ControladorBorrarAula conAulas = cargadorVentanaBorrar.getController();
		conAulas.setControlador(controladorMVC);
		Scene escenaVentanaBorrar = new Scene(raizVentanaBorrar);
		ventanaBorrar.setTitle("Borrar aula");
		ventanaBorrar.initModality(Modality.WINDOW_MODAL); 
		ventanaBorrar.setScene(escenaVentanaBorrar);
		ventanaBorrar.setResizable(false);
   		ventanaBorrar.showAndWait();
   		if((boolean)btBorrar.getScene().getWindow().isFocused()){
   			actualizarDatos(); 
   		}
    }

    @FXML
    void abrirDisponibilidad(ActionEvent event) throws IOException {
    	ventanaDisponibilidad = new Stage();
		FXMLLoader cargadorVentanaDisponibilidad = new FXMLLoader(getClass().getResource("../vistas/DisponibilidadAula.fxml"));
		GridPane raizVentanaDisponibilidad = cargadorVentanaDisponibilidad.load();
		ControladorDisponibilidadAula conAulas = cargadorVentanaDisponibilidad.getController();
		conAulas.setControlador(controladorMVC);
		Scene escenaVentanaDisponibilidad = new Scene(raizVentanaDisponibilidad);
		ventanaDisponibilidad.setTitle("Disponibilidad Aula");
		ventanaDisponibilidad.initModality(Modality.WINDOW_MODAL); 
		ventanaDisponibilidad.setScene(escenaVentanaDisponibilidad);
		ventanaDisponibilidad.setResizable(false);
   		ventanaDisponibilidad.showAndWait();
    }

    @FXML
    void volver(ActionEvent event) {
    	Stage ventana = (Stage) btVolver.getScene().getWindow();
    	ventana.close();
    }

    public void actualizarDatos() {
    	aulas.setAll(controladorMVC.getAulas());
    	reservasAula.setAll(controladorMVC.getReservas());
    }

}
