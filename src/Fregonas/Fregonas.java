package Fregonas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 * @author bruno
 */
public class Fregonas {

    private StringProperty modelo;
    private DoubleProperty alto;
    private StringProperty color;
    private StringProperty marca;
    private DoubleProperty precio;
    private IntegerProperty ID_Fregona;

    public Fregonas(String modelo, Double alto, String color, String marca, Double precio, int ID_Fregona) {
        this.modelo = new SimpleStringProperty(modelo);
        this.alto = new SimpleDoubleProperty(alto);
        this.color = new SimpleStringProperty(color);
        this.marca = new SimpleStringProperty(marca);
        this.precio = new SimpleDoubleProperty(precio);
        this.ID_Fregona = new SimpleIntegerProperty(ID_Fregona);
    }

    public String getModelo() {
        return modelo.get();
    }

    public double getAlto() {
        return alto.get();
    }

    public String getColor() {
        return color.get();
    }

    public String getMarca() {
        return marca.get();
    }

    public double getPrecio() {
        return precio.get();
    }

    public int getID_Fregona() {
        return ID_Fregona.get();
    }

    /**
     *
     * @param connection
     * @param lista obtenemos una lista de Fregonas
     */
    public static void llenarInformacion(Connection connection, ObservableList<Fregonas> lista) {
        try {
            String sql = "SELECT Modelo, Alto, Color, Marca, Precio, ID_Fregona from fregonas;";
            Statement sc = connection.createStatement();
            ResultSet datos = sc.executeQuery(sql);
            while (datos.next()) {
                lista.add(new Fregonas(datos.getString("Modelo"),
                        datos.getDouble("Alto"),
                        datos.getString("Color"),
                        datos.getString("Marca"),
                        datos.getDouble("Precio"),
                        datos.getInt("ID_Fregona")
                ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Fregonas.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int ingresarRegistro(Connection connection) {
        try {
            String sql = "INSERT INTO fregonas (Modelo, Alto, Color, Marca, Precio, ID_Fregona ) VALUES ( ? , ? , ? , ? , ? , ? );";
            PreparedStatement datos = connection.prepareStatement(sql);
            datos.setString(1, modelo.get());
            datos.setDouble(2, alto.get());
            datos.setString(3, color.get());
            datos.setString(4, marca.get());
            datos.setDouble(5, precio.get());
            datos.setInt(6, ID_Fregona.get());

            return datos.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Fregonas.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public int modificarRegistro(Connection connection) {
        try {
            String sql = "UPDATE  fregonas SET Modelo = ? , Alto = ? , Color = ? , Marca= ? , Precio = ? WHERE ID_Fregona = ? ;";
            PreparedStatement datos = connection.prepareStatement(sql);
            datos.setString(1, modelo.get());
            datos.setDouble(2, alto.get());
            datos.setString(3, color.get());
            datos.setString(4, marca.get());
            datos.setDouble(5, precio.get());
            datos.setInt(6, ID_Fregona.get());
            return datos.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            Logger.getLogger(Fregonas.class.getName()).log(Level.SEVERE, null, ex);

            return 0;
        }
    }

    public int eliminarRegistro(Connection connection) {
        try {
            String sql = "DELETE FROM fregonas WHERE ID_Fregona = ?";
            PreparedStatement instruccion = connection.prepareStatement(sql);
            instruccion.setInt(1, ID_Fregona.get());
            return instruccion.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Fregonas.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
}
