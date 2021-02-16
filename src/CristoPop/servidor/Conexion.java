package CristoPop.servidor;

import java.sql.*;

public class Conexion {

    private final String driver = "jdbc";
    private final String protocolo = "mysql";
    private final String puerto = "localhost:3306";
    private final String DB_name = "cristopop";
    private final String user = "root";
    private final String passwd = "admin";
    private Connection miConexion = null;

    public Conexion() {
    }
    
    public Connection obtenerConexion() {
        try {
            miConexion = DriverManager.getConnection(driver + ":" + protocolo + "://" + puerto + "/" + DB_name, user, passwd);
            System.out.println("Conexion establecida");

        } catch (SQLException e) {
            System.out.println(e);
        }

        return miConexion;
    }

}
