package org.iesalandalus.programacion.reservasaulas.vista.iugrafica.controladoresvistas.controladoresAulas;

import org.iesalandalus.programacion.reservasaulas.controlador.IControladorReservasAulas;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.vista.iugrafica.utilidades.Dialogos;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControladorBorrarAula {

		@FXML private Button btAceptar;
	    @FXML private TextField tfNombre;
	    @FXML private Button btCancelar;
	    private String ER_OBLIGATORIO  = ".+";
	    private IControladorReservasAulas controladorMVC;
	    
	    public void setControlador(IControladorReservasAulas modelo) {
	    	this.controladorMVC=modelo;
	    }
	    @FXML 
	    private void initialize(){
	    	tfNombre.textProperty().addListener((ob, ov, nv)-> comprobarTexto(ER_OBLIGATORIO, tfNombre));
	    }
	    
	    private void comprobarTexto(String er, TextField campoTexto) {
			if(campoTexto.getText().matches(er)) {
				campoTexto.setStyle("fx-border-color: green");
			} else {
				campoTexto.setStyle("fx-border-color: red");
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
	    		
		    	if (tfNombre.getStyle().equals("fx-border-color: green")) {
		    		Aula aulaBorrar = new Aula(tfNombre.getText(), 15);
		    		controladorMVC.borrarAula(controladorMVC.buscarAula(aulaBorrar));
		    		Dialogos.mostrarDialogoInformacion("Accion realizada", "Usted ha borrado el aula: " + tfNombre.getText());
		    		Stage stage = (Stage) btAceptar.getScene().getWindow();
		    		stage.close();
		    	}
			} catch (Exception e) {
				Dialogos.mostrarDialogoError("Error", e.getMessage(), null);
			}
	    	
	    }

}
