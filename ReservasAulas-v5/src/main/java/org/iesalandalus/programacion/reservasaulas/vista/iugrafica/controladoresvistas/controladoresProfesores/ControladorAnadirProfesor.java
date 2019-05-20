package org.iesalandalus.programacion.reservasaulas.vista.iugrafica.controladoresvistas.controladoresProfesores;

import org.iesalandalus.programacion.reservasaulas.controlador.IControladorReservasAulas;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.vista.iugrafica.utilidades.Dialogos;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControladorAnadirProfesor {

	@FXML
	private TextField tfNombre;
	@FXML
	private TextField tfCorreo;
	@FXML
	private TextField tfTelefono;

	@FXML
	private Button btAceptar;
	@FXML
	private Button btCancelar;

	private static String ER_TELEFONO = "[6|9]\\d{8}";
	private static String ER_CORREO = "\\w+(?:\\.\\w+)*@\\w+\\.\\w{2,5}";
	private static String ER_OBLIGATORIO = ".+";

	private IControladorReservasAulas controladorMVC;

	public void setControlador(IControladorReservasAulas controladorMVC) {
		this.controladorMVC = controladorMVC;
	}
	@FXML
	private void initialize() {
		tfNombre.textProperty().addListener((ob, ov, nv) -> comprobarCampo(ER_OBLIGATORIO, tfNombre));
		tfCorreo.textProperty().addListener((ob, ov, nv) -> comprobarCampo(ER_CORREO, tfCorreo));
		tfTelefono.textProperty().addListener((ob, ov , nv) -> comprobarCampo(ER_TELEFONO, tfTelefono));
	}
	private void comprobarCampo(String er , TextField campoTexto) {
		String texto = campoTexto.getText();
		if(er.equals(ER_TELEFONO)) {
			if(texto.trim().equals("") || texto == null) {
				campoTexto.setStyle("-fx-border-color: red");
			} else {
				if (texto.matches(er) || texto.equals("")) {
					campoTexto.setStyle("-fx-border-color: green");
				}
			}
		} else {
			if(texto.matches(er)) {
				campoTexto.setStyle("-fx-border-color: green");
			} else {
				campoTexto.setStyle("-fx-border-color: red");
			}
		}
	}
	@FXML
	void cancelar() {
		Stage stage = (Stage) btCancelar.getScene().getWindow();
		stage.close();
	}

	@FXML
	void aceptar() {
		Profesor profesorAnadir;
		try {
			if (tfTelefono.getText().equals("")) {
				profesorAnadir = new Profesor(tfNombre.getText(), tfCorreo.getText());
			} else {
				profesorAnadir = new Profesor(tfNombre.getText(), tfCorreo.getText(), tfTelefono.getText());
			}
			controladorMVC.insertarProfesor(profesorAnadir);
			Stage escena = (Stage) btAceptar.getScene().getWindow();  
			Dialogos.mostrarDialogoInformacion("Profesor añadido", "El profesor ha sido añadido con éxito");
			escena.close();

		} catch (Exception e) {
			Dialogos.mostrarDialogoError("Error", e.getMessage());
		}

	}

}