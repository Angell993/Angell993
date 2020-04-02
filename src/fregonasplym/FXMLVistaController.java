/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fregonasplym;

import Fregonas.Conexion;
import Fregonas.Fregonas;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author bruno
 */
public class FXMLVistaController implements Initializable {

    // TextField
    @FXML
    private TextField textModelo;
    @FXML
    private TextField textAlto;
    @FXML
    private TextField textColor;
    @FXML
    private TextField textMarca;
    @FXML
    private TextField textPrecio;
    @FXML
    private TextField textIdFregona;

    // Button de consultas
   /* @FXML
    private Button btnIngresar;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnBorrar;*/

    // TableView y TableColumn
    @FXML
    private TableView<Fregonas> tablaFregona;
    @FXML
    private TableColumn<Fregonas, String> tcModelo;
    @FXML
    private TableColumn<Fregonas, Double> tcAlto;
    @FXML
    private TableColumn<Fregonas, String> tcColor;
    @FXML
    private TableColumn<Fregonas, String> tcMarca;
    @FXML
    private TableColumn<Fregonas, String> tcPrecio;

    //Coleccion y Connection 
    private ObservableList<Fregonas> listaFregona;
    private Conexion conexion;
    private int posicionRegistro;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conexion = new Conexion();
        conexion.establecerConexion();

        //Inicializamos las listas ObservableList
        listaFregona = FXCollections.observableArrayList();

        //Rellenamos la lista 
        Fregonas.llenarInformacion(conexion.getConexion(), listaFregona);

        //Enlazar la lista con TableView
        tablaFregona.setItems(listaFregona);

        //Enlazar Columnas con atributos
        tcModelo.setCellValueFactory(new PropertyValueFactory("modelo"));
        tcAlto.setCellValueFactory(new PropertyValueFactory<>("alto"));
        tcColor.setCellValueFactory(new PropertyValueFactory("color"));
        tcMarca.setCellValueFactory(new PropertyValueFactory("marca"));
        tcPrecio.setCellValueFactory(new PropertyValueFactory("precio"));

        rellenarCampos();
        //Se termina la conexion a la data base 
        conexion.desconectarConexion();
    }

    //Rellenar los TextFiled al pinchar en un registro
    public void rellenarCampos() {
        tablaFregona.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Fregonas> arg0, Fregonas valorAnterior, Fregonas valorSeleccionado) -> {
            if (valorSeleccionado != null) {

                textModelo.setText(valorSeleccionado.getModelo());
                textAlto.setText(String.valueOf(valorSeleccionado.getAlto()));
                textColor.setText(valorSeleccionado.getColor());
                textMarca.setText(valorSeleccionado.getMarca());
                textPrecio.setText(String.valueOf(valorSeleccionado.getPrecio()));
                textIdFregona.setText(String.valueOf(valorSeleccionado.getID_Fregona()));
                textIdFregona.setDisable(true);
            }
        });
    }

    //Metodos para los Botones
    @FXML
    public void ingresarRegistro() {
        if (isCamposVacios() == true && isNumero() == true) {
            Fregonas nuevo = new Fregonas(textModelo.getText(), Double.valueOf(textAlto.getText()),
                    textColor.getText(), textMarca.getText(), Double.valueOf(textPrecio.getText()),
                    Integer.valueOf(textIdFregona.getText()));
            conexion.establecerConexion();
            //Si el PreparedStatement realizo correctamente registrar nos devuelve 1 o 0
            int respuestaRecibida = nuevo.ingresarRegistro(conexion.getConexion());
            conexion.desconectarConexion();
            //Si es 1 agregamos a la lista de freogonas
            if (respuestaRecibida == 1) {
                listaFregona.add(nuevo);
                
                Alert mensaje = new Alert(AlertType.INFORMATION);
                mensaje.setTitle("Registro agregado");
                mensaje.setHeaderText("El registro ha sido agregado exitosamente.");
                mensaje.show();
            } else {
                Alert mensaje = new Alert(AlertType.INFORMATION);
                mensaje.setTitle("ERROR al Registrar");
                mensaje.setHeaderText("El registro no se agregado exitosamente.");
                mensaje.show();
            }
        }

    }

    @FXML
    public void eliminarRegistro() {
        posicionRegistro = tablaFregona.getSelectionModel().getSelectedIndex() -1;
        conexion.establecerConexion();
        int resultado = tablaFregona.getSelectionModel().getSelectedItem().eliminarRegistro(conexion.getConexion());
        conexion.desconectarConexion();

        if (resultado == 1) {
            listaFregona.remove(posicionRegistro);

            Alert mensaje = new Alert(AlertType.INFORMATION);
            mensaje.setTitle("Registro eliminado");
            mensaje.setHeaderText("El registro ha sido eliminado exitosamente");
            mensaje.show();
        }
    }

    @FXML
    public void modificarRegistro() {
        posicionRegistro = tablaFregona.getSelectionModel().getSelectedIndex() ;
        if (isCamposVacios() == true && isNumero() == true) {
            
            Fregonas nuevo = new Fregonas(textModelo.getText(), Double.valueOf(textAlto.getText()),
                    textColor.getText(), textMarca.getText(), Double.valueOf(textPrecio.getText()),
                    Integer.valueOf(textIdFregona.getText()));
            conexion.establecerConexion();
            //Si el PreparedStatement realizo correctamente registrar nos devuelve 1 o 0
            int respuestaRecibida = nuevo.modificarRegistro(conexion.getConexion());
            conexion.desconectarConexion();
            //Si es 1 agregamos a la lista de freogonas
            if (respuestaRecibida == 1) {
                listaFregona.set(posicionRegistro, nuevo);
                
                Alert mensaje = new Alert(AlertType.INFORMATION);
                mensaje.setTitle("Registro Modificado");
                mensaje.setHeaderText("El registro ha sido modificado exitosamente.");
                mensaje.show();
            } else {
                Alert mensaje = new Alert(AlertType.ERROR);
                mensaje.setTitle("ERROR al Modificar");
                mensaje.setHeaderText("El registro no se modifico correctamente.");
                mensaje.show();
            }
        }
    }

    @FXML
    public void borrarTextField() {
        String nulo = null;
        textModelo.setText(nulo);
        textAlto.setText(nulo);
        textColor.setText(nulo);
        textMarca.setText(nulo);
        textPrecio.setText(nulo);
        textIdFregona.setText(nulo);
        textIdFregona.setDisable(false);
    }

    public Boolean isCamposVacios() {
        try {
            if (!textModelo.getText().isEmpty() && !textColor.getText().isEmpty() && !textMarca.getText().isEmpty()) {
                return true;
            } else {
                Alert mensaje = new Alert(AlertType.ERROR);
                mensaje.setTitle("ERROR AL Registrar");
                mensaje.setHeaderText("Los campos estan vacíos.");
                mensaje.show();
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public Boolean isNumero() {
        try {
            if (!textAlto.getText().isEmpty() && !textPrecio.getText().isEmpty() && !textIdFregona.getText().isEmpty()) {
                Double.valueOf(textAlto.getText());
                Double.valueOf(textPrecio.getText());
                Integer.valueOf(textIdFregona.getText());
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println(e.toString());
            Alert mensaje = new Alert(AlertType.ERROR);
            mensaje.setTitle("LOS CAMPOS ALTO , PRECIO e ID_Fregona");
            mensaje.setHeaderText("Deben ser de tipo numerico.");
            mensaje.show();
            return false;
        }
    }
}
