package org.iesalandalus.programacion.reservasaulas.vista.iugrafica.controladoresvistas;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import org.iesalandalus.programacion.reservasaulas.controlador.IControladorReservasAulas;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.vista.iugrafica.controladoresvistas.controladoresAulas.ControladorAnadirAula;
import org.iesalandalus.programacion.reservasaulas.vista.iugrafica.controladoresvistas.controladoresAulas.ControladorBorrarAula;
import org.iesalandalus.programacion.reservasaulas.vista.iugrafica.controladoresvistas.controladoresProfesores.ControladorAnadirProfesor;
import org.iesalandalus.programacion.reservasaulas.vista.iugrafica.controladoresvistas.controladoresProfesores.ControladorBorrarProfesor;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ControladorVentanaProfesores {

    @FXML private Accordion acProfesoresReservas;
    @FXML private TitledPane tpProfesores;
    @FXML private Accordion acOpcion;
    @FXML private TitledPane tpEdicion;
    
    @FXML private Button btAnadir;
    @FXML private Button btBorrar;
    @FXML private Button btVolver;
    
    @FXML private TextField tfReservasProfesor;
    @FXML private TextField tfProfesor;
    
    @FXML private TableView<Reserva> tvReservasProfesores;
    @FXML private TableColumn<Reserva, String> tcNombreAulaReserva;
    @FXML private TableColumn<Reserva, Integer> tcCapacidadAulaReserva;
    @FXML private TableColumn<Reserva, String> tcNombreProfesorReserva;
    @FXML private TableColumn<Reserva, String> tcCorreoProfesorReserva;
    @FXML private TableColumn<Reserva, String> tcTelefonoProfesorReserva;
    @FXML private TableColumn<Reserva, String> tcPermanenciaDiaReserva;
    @FXML private TableColumn<Reserva, String> tcPermanenciaReserva;
    @FXML private TableColumn<Reserva, Float> tcPuntosReserva;
    
    @FXML private TableView<Profesor> tvProfesores;
    @FXML private TableColumn<Profesor, String> tcNombreProfesor;
    @FXML private TableColumn<Profesor, String> tcCorreoProfesor;
    @FXML private TableColumn<Profesor, String> tcTelefonoProfesor;
    
    private Stage ventanaAnadir;
    private Stage ventanaBorrar;

    private ObservableList<Profesor> profesores = FXCollections.observableArrayList();
    private ObservableList<Reserva> reservasProfesores = FXCollections.observableArrayList();
    private FilteredList<Profesor> profesoresFiltrados = new FilteredList<>(profesores, p -> true);
    private FilteredList<Reserva> reservasProfesoresFiltradas = new FilteredList<>(reservasProfesores, p -> true);
    
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private IControladorReservasAulas controladorMVC;
    
    public void setControlador(IControladorReservasAulas controladorMVC) {
    	this.controladorMVC = controladorMVC;
    }
    
    @FXML
    private void initialize() {
    	tcNombreProfesor.setCellValueFactory(profesor -> new SimpleStringProperty(profesor.getValue().getNombre()));
    	tcCorreoProfesor.setCellValueFactory(profesor -> new SimpleStringProperty(profesor.getValue().getCorreo()));
    	tcTelefonoProfesor.setCellValueFactory(profesor -> new SimpleStringProperty(profesor.getValue().getTelefono()));
    	
    	tcNombreAulaReserva.setCellValueFactory(reserva -> new SimpleStringProperty(reserva.getValue().getAula().getNombre()));
    	tcCapacidadAulaReserva.setCellValueFactory(reserva -> new SimpleIntegerProperty(reserva.getValue().getAula().getPuestos()).asObject());
    	tcNombreProfesorReserva.setCellValueFactory(reserva -> new SimpleStringProperty(reserva.getValue().getProfesor().getNombre()));
    	tcCorreoProfesorReserva.setCellValueFactory(reserva -> new SimpleStringProperty(reserva.getValue().getProfesor().getCorreo()));
    	tcTelefonoProfesorReserva.setCellValueFactory(reserva -> new SimpleStringProperty(reserva.getValue().getProfesor().getTelefono()));
    	tcPermanenciaDiaReserva.setCellValueFactory(reserva -> new SimpleStringProperty(FORMATO_FECHA.format(reserva.getValue().getPermanencia().getDia())));
    	tcPermanenciaReserva.setCellValueFactory(reserva -> new SimpleStringProperty(reserva.getValue().getPermanencia().toString()));
    	tcPuntosReserva.setCellValueFactory(reserva -> new SimpleFloatProperty(reserva.getValue().getPuntos()).asObject());
    	
    	SortedList<Profesor> profesoresOrdenados = new SortedList<>(profesoresFiltrados);
    	profesoresOrdenados.comparatorProperty().bind(tvProfesores.comparatorProperty());
    	tvProfesores.setItems(profesoresOrdenados);
    	SortedList<Reserva> reservasProfesoresOrdenadas = new SortedList<>(reservasProfesoresFiltradas);
    	reservasProfesoresOrdenadas.comparatorProperty().bind(tvReservasProfesores.comparatorProperty());
    	tvReservasProfesores.setItems(reservasProfesoresOrdenadas);
    	acOpcion.setExpandedPane(tpEdicion);
    	acProfesoresReservas.setExpandedPane(tpProfesores);
    	tfProfesor.textProperty().addListener((ob, ov, nv) -> 
		profesoresFiltrados.setPredicate(profesor -> {
			if (nv == null || nv.isEmpty()) {
				return true;
			}
			String nombre = profesor.getNombre().toLowerCase();
			return nombre.contains(nv.toLowerCase());
			})
    	);
    	
    	tfReservasProfesor.textProperty().addListener((ob,ov,nv)->
    	reservasProfesoresFiltradas.setPredicate(reserva -> {
    		if (nv == null || nv.isEmpty()) {
    			return true;
    		}
    		String nombre = reserva.getProfesor().getNombre().toLowerCase();
    		return nombre.contains(nv.toLowerCase());
    		})
    	
    	);
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
    void ventanaAnadir() throws IOException {
    	ventanaAnadir = new Stage();
		FXMLLoader cargadorVentanaInsertar = new FXMLLoader(getClass().getResource("../vistas/AnadirProfesor.fxml"));
		GridPane raizVentanaInsertar = cargadorVentanaInsertar.load();
		ControladorAnadirProfesor conProfesor = cargadorVentanaInsertar.getController();
		conProfesor.setControlador(controladorMVC);
		Scene escenaVentanaInsertar = new Scene(raizVentanaInsertar);
		ventanaAnadir.setTitle("Añadir Profesor/a");
		ventanaAnadir.initModality(Modality.WINDOW_MODAL); 
		ventanaAnadir.setScene(escenaVentanaInsertar);
		ventanaAnadir.setResizable(false);
   		ventanaAnadir.showAndWait();
   		if((boolean)btAnadir.getScene().getWindow().isFocused()){
   			actualizarDatos(); 
   		}
    }

    @FXML
    void ventanaBorrar() throws IOException {
    	ventanaBorrar = new Stage();
		FXMLLoader cargadorVentanaBorrar = new FXMLLoader(getClass().getResource("../vistas/BorrarProfesor.fxml"));
		GridPane raizVentanaBorrar = cargadorVentanaBorrar.load();
		ControladorBorrarProfesor conProfesor = cargadorVentanaBorrar.getController();
		conProfesor.setControlador(controladorMVC);
		Scene escenaVentanaBorrar = new Scene(raizVentanaBorrar);
		ventanaBorrar.setTitle("Borrar profesor/a");
		ventanaBorrar.initModality(Modality.WINDOW_MODAL); 
		ventanaBorrar.setScene(escenaVentanaBorrar);
		ventanaBorrar.setResizable(false);
   		ventanaBorrar.showAndWait();
   		if((boolean)btAnadir.getScene().getWindow().isFocused()){
   			actualizarDatos(); 
   		}
    }

    @FXML
    void volver() {
    	Stage ventana = (Stage) btVolver.getScene().getWindow();
    	ventana.close();
    }

	public void actualizarDatos() {
		profesores.setAll(controladorMVC.getProfesores());
		reservasProfesores.setAll(controladorMVC.getReservas());
	}

}
