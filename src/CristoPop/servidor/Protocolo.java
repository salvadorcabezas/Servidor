package CristoPop.servidor;

// el protocolo se encarga de recibir datos y devolver otros
import java.sql.*;

public class Protocolo {

    Connection conexion = null;

    public Protocolo(Connection conexion) {
        this.conexion = conexion;
    }

    // metodo al que le pasas por parametro un mensaje y te devuelve otro
    public String processInput(String theInput) {
        String theOutput = "Usuario incorrecto";
        boolean usuarioLogueado = false;

        try {
            // creamos objeto statement
            Statement statement = conexion.createStatement();

            // ejecutamos sql
            ResultSet resultSet = statement.executeQuery("SELECT * FROM USUARIO");

            // buscar en el resultset
            while (resultSet.next()) {
                if ((resultSet.getString("login").equals("neutron")) && (resultSet.getString("password").equals("cabezon"))) {
                    usuarioLogueado = true;
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        if (usuarioLogueado == true) {
            theOutput = "Usuario correcto0";
        }

        return theOutput;
    }
}
