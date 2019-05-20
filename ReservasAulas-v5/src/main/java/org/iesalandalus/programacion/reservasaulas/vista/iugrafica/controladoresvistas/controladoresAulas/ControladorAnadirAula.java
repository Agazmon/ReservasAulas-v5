package org.iesalandalus.programacion.reservasaulas.vista.iugrafica.controladoresvistas.controladoresAulas;

import org.iesalandalus.programacion.reservasaulas.controlador.IControladorReservasAulas;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.vista.iugrafica.utilidades.Dialogos;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControladorAnadirAula {

    private static final String ER_OBLIGATORIO = ".+";
    private static final String ER_CAPACIDAD = "[0-9]{1,3}";
    
	@FXML private TextField tfCapacidad;
    @FXML private Button btAceptar;
    @FXML private TextField tfNombre;
    @FXML private Button btCancelar;

    private IControladorReservasAulas controladorMVC;
    
    public void setControlador(IControladorReservasAulas modelo) {
    	this.controladorMVC=modelo;
    }
    @FXML 
    private void initialize(){
    	tfNombre.textProperty().addListener((ob, ov, nv) -> comprobarTexto(ER_OBLIGATORIO, tfNombre));
    	tfCapacidad.textProperty().addListener((ob, ov, nv) -> comprobarTexto(ER_CAPACIDAD, tfCapacidad));
    }
    
    private void comprobarTexto(String er, TextField campoTexto) {
		if (!campoTexto.getText().matches(er)) {
			campoTexto.setStyle("-fx-border-color: red");
		} else {
			campoTexto.setStyle("-fx-border-color: green");
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
    		if(tfNombre.getStyle().equals("-fx-border-color: green")&& tfCapacidad.getStyle().equals("-fx-border-color: green")) {
				controladorMVC.insertarAula(new Aula(tfNombre.getText(), Integer.parseInt(tfCapacidad.getText())));
				Dialogos.mostrarDialogoInformacion("Aula añadida", "El aula ha sido añadida satisfactoriamente.");
				Stage stage = (Stage) btAceptar.getScene().getWindow();
				stage.close();
			}
		} catch (Exception e) {
			Dialogos.mostrarDialogoError("Error", e.getMessage(), null);
		}
    }

}
