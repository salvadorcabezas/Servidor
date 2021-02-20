package Controlador;

import java.sql.*;

public class ControladorProducto {

    Connection conexion = null;

    public ControladorProducto(Connection conexion) {
        this.conexion = conexion;
    }

    public String dameProductos() {
        String productos = "";
        String mensaje = "";
        int rowCount = 0;

        try {
            // creamos objeto statement
            Statement statement = conexion.createStatement();

            // ejecutamos sql
            ResultSet resultSet = statement.executeQuery("SELECT * FROM PRODUCTO");

            // rellenamos el string con el resultset
            while (resultSet.next()) {
                productos = productos + "#" + resultSet.getString("codigo_producto") + "@" + resultSet.getString("nombre") + "@" + resultSet.getString("precio") + "@" + resultSet.getString("visualizaciones");
                rowCount++;
            }
            mensaje = "PROTOCOLCRISTOPOP1.0#AVAILABLE_PRODUCTS#" + rowCount + productos;
        } catch (Exception e) {
            System.out.println(e);
            mensaje = "PROTOCOLCRISTOPOP1.0#ERROR#CANT_GET_FILES";
        }

        return mensaje;
    }

}
