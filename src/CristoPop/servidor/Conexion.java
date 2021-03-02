package CristoPop.servidor;

import java.sql.*;

public class Conexion {

    String driver;
    String protocolo;
    String puerto;
    String DB_name;
    String user;
    String passwd;
    public Connection conexion = null;

    public Conexion() {
        this.driver = "jdbc";
        this.protocolo = "mysql";
        this.puerto = "localhost:3306";
        this.DB_name = "cristopop";
        this.user = "root";
        this.passwd = "admin";
        //this.obtenerConexion();
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public String getPuerto() {
        return puerto;
    }

    public void setPuerto(String puerto) {
        this.puerto = puerto;
    }

    public String getDB_name() {
        return DB_name;
    }

    public void setDB_name(String DB_name) {
        this.DB_name = DB_name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
    
    public void obtenerConexion() {
        try {
            conexion = DriverManager.getConnection(driver + ":" + protocolo + "://" + puerto + "/" + DB_name, user, passwd);
            //System.out.println("Conexion establecida");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
    /*
    public Connection obtenerConexion() {
        try {
            miConexion = DriverManager.getConnection(driver + ":" + protocolo + "://" + puerto + "/" + DB_name, user, passwd);
            System.out.println("Conexion establecida");

        } catch (SQLException e) {
            System.out.println(e);
        }

        return miConexion;
    }
    */
    
    public void cerrarConexion(){
        try{
            this.conexion.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
