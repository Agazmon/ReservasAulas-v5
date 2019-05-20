package org.iesalandalus.programacion.reservasaulas.vista.iugrafica.controladoresvistas.controladoresReservas;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.iesalandalus.programacion.reservasaulas.controlador.IControladorReservasAulas;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.Permanencia;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.PermanenciaPorHora;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.PermanenciaPorTramo;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.Tramo;
import org.iesalandalus.programacion.reservasaulas.vista.iugrafica.utilidades.Dialogos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

public class ControladorRealizarReserva {

	@FXML
	private ComboBox<String> cbAula;
	@FXML
	private ComboBox<String> cbProfesor;
	@FXML
	private DatePicker dpFecha;
	@FXML
	private ComboBox<String> cbTipoReserva;
	@FXML
	private Label lbTiempoReserva;
	@FXML
	private ComboBox<String> cbTiempoReserva;
	@FXML
	private Button btCancelar;
	@FXML
	private Button btRealizar;
	
	private ObservableList<Aula> aulas = FXCollections.observableArrayList();
	private ObservableList<Profesor> profesores = FXCollections.observableArrayList();
	private Tooltip fechaInvalida = new Tooltip("La fecha no puede ser en este mes, debe ser posterior");
	private IControladorReservasAulas controladorMVC;
	private ObservableList<String> aulasNombres;
	private ObservableList<String> profesoresNombres;

	private final ObservableList<String> horas = FXCollections.observableArrayList("Selecciona", "08:00", "09:00",
			"10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00",
			"22:00");
    private final ObservableList<String> tramos = FXCollections.observableArrayList("Selecciona","Mañana", "Tarde");
    
    public void setControlador(IControladorReservasAulas controladorMVC) {
		this.controladorMVC = controladorMVC;
	}

	@FXML
	private void initialize() {
		dpFecha.valueProperty().addListener((ob, ov, nv)-> comprobarFecha(dpFecha.getValue()));
		cbTipoReserva.setItems(FXCollections.observableArrayList("Selecciona una", "Por hora", "Por tramo"));
		cbTipoReserva.valueProperty().addListener((ob, ov, nv) -> actualizar(cbTipoReserva.getValue()));
	}
	private void comprobarFecha(LocalDate value) {
		if(value.getDayOfYear()<LocalDate.now().plus(1, ChronoUnit.MONTHS).getDayOfYear() || value.getYear() < LocalDate.now().getYear()) {
			btRealizar.setTooltip(fechaInvalida);
		} else {
			btRealizar.setTooltip(null);
			btRealizar.setDisable(false);
		}
	}
	private ObservableList<String> obtenerNombresProfesores(List<Profesor> listaProfesores) {
		profesoresNombres = FXCollections.observableArrayList(); 
		if (!listaProfesores.isEmpty()) {
			for (Profesor profesor : listaProfesores) {
				profesoresNombres.add(profesor.getNombre());
			}
			return profesoresNombres;
		} else {
			profesoresNombres.add("No hay profesores");
			return profesoresNombres;
		}
	
	}
	private ObservableList<String> obtenerNombresAulas(List<Aula> listaAulas){
		aulasNombres = FXCollections.observableArrayList();
		if (!listaAulas.isEmpty()) {
			for (Aula aula : listaAulas) {
				aulasNombres.add(aula.getNombre());
			}
			return aulasNombres;
		} else {
			aulasNombres.add("No hay aulas");
			return aulasNombres;
		}
	}
	
	private void actualizar(String seleccion) {
		if (seleccion.equals("Por hora")) {
			lbTiempoReserva.setText("Seleccione hora: ");
			cbTiempoReserva.setItems(horas);
			cbTiempoReserva.setValue("Selecciona");
			btRealizar.setDisable(false);
		} else if(seleccion.equals("Por tramo")) {
			lbTiempoReserva.setText("Selecciona tramo: ");
			cbTiempoReserva.setItems(tramos);
			cbTiempoReserva.setValue("Selecciona");
			btRealizar.setDisable(false);
		} else {
			lbTiempoReserva.setText("Seleccione");
			cbTiempoReserva.setItems(FXCollections.observableArrayList("Seleccione el tipo"));
			btRealizar.setDisable(true);
		}
	}

	@FXML
	void cancelar() {
		Stage ventana = (Stage) btCancelar.getScene().getWindow();
		ventana.close();
	}

	@FXML
	void realizarReserva() {
		try {
			Aula aulaReserva = controladorMVC.buscarAula(new Aula(cbAula.getValue(), 10));
			Profesor profesorReserva = controladorMVC.buscarProfesor(new Profesor(cbProfesor.getValue(), "correo@inventado.com"));
			Permanencia permanenciaReserva;
			if(cbTipoReserva.getValue()!="Selecciona" && cbTiempoReserva.getValue()!="Selecciona") {
				if(cbTipoReserva.getValue().equals("Por hora")) {
					permanenciaReserva = new PermanenciaPorHora(dpFecha.getValue(), cbTiempoReserva.getValue());
				} else {
					if(cbTiempoReserva.getValue().equals("Mañana")) {
						permanenciaReserva = new PermanenciaPorTramo(dpFecha.getValue(), Tramo.MANANA);
					} else {
						permanenciaReserva = new PermanenciaPorTramo(dpFecha.getValue(), Tramo.TARDE);
					}
				}
				if(controladorMVC.consultarDisponibilidad(aulaReserva, permanenciaReserva)) {
					controladorMVC.realizarReserva(new Reserva(profesorReserva, aulaReserva, permanenciaReserva));
					Dialogos.mostrarDialogoInformacion("Reserva realizada","La reserva ha sido registrada", (Stage) btRealizar.getScene().getWindow());
				} else {
					Dialogos.mostrarDialogoAdvertencia("Aula ocupada", "Lamentamos comunicarle que el aula esta ocupada", (Stage) btRealizar.getScene().getWindow());
				}
				
			}
		} catch (Exception e) {
			Dialogos.mostrarDialogoError("Error", e.getMessage());
		}

	}

	public void inicializarDatos() {
		aulas.setAll(controladorMVC.getAulas());
		profesores.setAll(controladorMVC.getProfesores());
		cbAula.setItems(FXCollections.observableArrayList(obtenerNombresAulas(aulas)));
		cbProfesor.setItems(FXCollections.observableArrayList(obtenerNombresProfesores(profesores)));
		
	}

}