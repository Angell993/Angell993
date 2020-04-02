package Fregonas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author bruno
 */
public class Conexion {
    private String user = "root";
    private String pass = "saladino";
    private String db = "fregonasplym";
    private String server = "jdbc:mysql://localhost:3306/" + db + "?serverTimezone=UTC";
    private Connection conexion;

    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }
    
    public void establecerConexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(server, user, pass);
            System.out.println("Conexion OK!!!");
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR, no se puede conectar a la Data Base.\n" + e.toString());
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public void desconectarConexion(){
        try {
            conexion.close();
            System.out.println("Desconectado DB, OK!!");
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
