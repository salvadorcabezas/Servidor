package Controlador;

import java.sql.*;

public class ControladorUsuario {

    Connection conexion = null;
    
    public ControladorUsuario(Connection conexion) {
        this.conexion = conexion;
    }    
    
    public String verificaUsuario(String login){
        
        String[] peticion = login.split("#");
        String theOutput = "PROTOCOLCRISTOPOP1.0#ERROR#BAD_LOGIN";
        String token="";
        boolean usuarioLogueado = false;

        try {
            // creamos objeto statement
            Statement statement = conexion.createStatement();

            // ejecutamos sql
            ResultSet resultSet = statement.executeQuery("SELECT * FROM USUARIO");

            // buscar en el resultset
            while (resultSet.next()) {
                if ((resultSet.getString("login").equals(peticion[2])) && (resultSet.getString("password").equals(peticion[3]))) { // si usuario y contraseña encontrados
                    usuarioLogueado = true;
                }
            }
            //System.out.println(peticion[2]+" "+peticion[3]);
        } catch (Exception e) {
            System.out.println(e);
        }

        if (usuarioLogueado == true) {
            theOutput = "PROTOCOLCRISTOPOP1.0#WELLCOME#"+peticion[2]+"#WITH_TOKEN#";
            //theOutput = "PROTOCOLCRISTOPOP1.0#WELLCOME#"+peticion[2]+"#WITH_TOKEN#"+token;
        }

        return theOutput;
        
    }
    
}
