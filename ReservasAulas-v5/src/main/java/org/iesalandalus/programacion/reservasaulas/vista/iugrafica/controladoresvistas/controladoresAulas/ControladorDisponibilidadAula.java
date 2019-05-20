package org.iesalandalus.programacion.reservasaulas.vista.iugrafica.controladoresvistas.controladoresAulas;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import org.iesalandalus.programacion.reservasaulas.controlador.IControladorReservasAulas;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.Permanencia;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.PermanenciaPorHora;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.PermanenciaPorTramo;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.Tramo;
import org.iesalandalus.programacion.reservasaulas.vista.iugrafica.utilidades.Dialogos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ControladorDisponibilidadAula {
	    @FXML private Button btAceptar;
	    @FXML private ChoiceBox<String> cbTipoPermanencia;
	    @FXML private Label lbEsperaSeleccionTIpo;
	    @FXML private TextField tfNombre;
	    @FXML private ComboBox<String> cbPermanencia;
	    @FXML private DatePicker dpSelectorFecha;
	    @FXML private Button btCancelar;
	    
	    private IControladorReservasAulas controladorMVC;
	    private final ObservableList<String> horas = FXCollections.observableArrayList("Selecciona","08:00","09:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00","22:00");
	    private final ObservableList<String> tramos = FXCollections.observableArrayList("Selecciona","Mañana", "Tarde");
	    private final Tooltip fechaInvalida = new Tooltip("La fecha no puede ser en este mes, debe ser posterior");
	    
	public void setControlador(IControladorReservasAulas controladorMVC) {
			this.controladorMVC = controladorMVC;
	}
	@FXML
	private void initialize() {
		cbTipoPermanencia.setItems(FXCollections.observableArrayList("Selecciona una", "Por hora","Por tramo"));
		cbTipoPermanencia.valueProperty().addListener((ob, ov, nv) -> actualizar(cbTipoPermanencia.getValue()));
		dpSelectorFecha.valueProperty().addListener((ob, ov, nv) -> comprobarFecha(dpSelectorFecha.getValue()));
		
	}
		

	private void comprobarFecha(LocalDate fecha) {
			if (fecha.getDayOfYear() < LocalDate.now().plus(1, ChronoUnit.MONTHS).getDayOfYear() || fecha.getYear()<LocalDate.now().getYear()) {
				btAceptar.setTooltip(fechaInvalida);
			} else {
				btAceptar.setTooltip(null);
				btAceptar.setDisable(false);
			}
	}
	private void actualizar(String cadena) {
		if (cadena.equals("Por hora")) {
			lbEsperaSeleccionTIpo.setText("Selecciona hora: ");
			cbPermanencia.setItems(horas);
			cbPermanencia.setValue("Selecciona");
			btAceptar.setDisable(false);
		} else if (cadena.equals("Por tramo")) {
			lbEsperaSeleccionTIpo.setText("Selecciona tramo: ");
			cbPermanencia.setItems(tramos);
			cbPermanencia.setValue("Selecciona");
			btAceptar.setDisable(false);
		} else {
			lbEsperaSeleccionTIpo.setText("Seleccione");
			cbPermanencia.setItems(FXCollections.observableArrayList("Seleccione el tipo"));
			btAceptar.setDisable(true);
		}
		
	}
	
    @FXML
    void cancelar() {
    	Stage ventana = (Stage) btCancelar.getScene().getWindow();
    	ventana.close();
    }
    
    @FXML
    void aceptar() {
    	try {
    		String nombre = tfNombre.getText();
    		Permanencia permanencia = null;
	    	if(nombre == null || nombre.trim().equals("")) {
				tfNombre.setStyle("-fx-border-color: red");
			} else {
				tfNombre.setStyle("fx-border-color: green");
			}
	    	if(dpSelectorFecha.getValue().getDayOfYear() >= LocalDate.now().plus(1, ChronoUnit.MONTHS).getDayOfYear() && dpSelectorFecha.getValue().getYear()>=LocalDate.now().getYear()) {
	    		if(cbTipoPermanencia.getValue()!="Selecciona" && cbPermanencia.getValue()!="Selecciona") {
	    			if (cbTipoPermanencia.getValue()=="Por hora") {
	    				permanencia = new PermanenciaPorHora(dpSelectorFecha.getValue(),cbPermanencia.getValue());
	    			} else {
	    				if (cbPermanencia.getValue().equals("Mañana")) {
	    					permanencia = new PermanenciaPorTramo(dpSelectorFecha.getValue(),Tramo.MANANA);
	    				} else {
	    					permanencia = new PermanenciaPorTramo(dpSelectorFecha.getValue(),Tramo.TARDE);
	    				}
	    			}
	    		}
	    	}
	    	if (nombre != null || !nombre.trim().equals("")) {
	    		Aula aulaDisponibilidad = new Aula(nombre, 15);
	    		if(controladorMVC.consultarDisponibilidad(controladorMVC.buscarAula(aulaDisponibilidad),permanencia)) {
	    			Dialogos.mostrarDialogoInformacion("Consulta realizada", "El aula esta disponible");
	    		} else {
	    			Dialogos.mostrarDialogoAdvertencia("Consulta realizada", "El aula no esta disponible");
	    		}
	    		Stage stage = (Stage) btAceptar.getScene().getWindow();
	    		stage.close();
	    	}
		} catch (Exception e) {
			Dialogos.mostrarDialogoError("Error", e.getMessage(), null);
		}
    	
    }
	

}