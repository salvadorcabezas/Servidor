package Modelo;

import CristoPop.servidor.Conexion;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.sql.Statement;
import java.sql.ResultSet;

public class Usuario extends Conexion {

    String login;
    String nombre;
    String password;
    ArrayList<Usuario> array_usuarios;

    public Usuario() {
        login = "";
        nombre = "";
        password = "";
        array_usuarios = new ArrayList<>();
    }

    public Usuario(String login, String nombre, String password, ArrayList<Usuario> array_usuarios) {
        this.login = login;
        this.nombre = nombre;
        this.password = password;
        this.array_usuarios = array_usuarios;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Usuario> getArray_usuarios() {
        return array_usuarios;
    }

    public void setArray_usuarios(ArrayList<Usuario> array_usuarios) {
        this.array_usuarios = array_usuarios;
    }

    /*
        metodo comprueba login 
        parametros: String del tipo PROTOCOLCRISTOPOP1.0#LOGIN#<user_login>#<pass>
        devolverá  PROTOCOLCRISTOPOP1.0#ERROR#BAD_LOGIN si es erroneo
        devolverá PROTOCOLCRISTOPOP1.0#WELLCOME#<user_login>#WITH_TOKEN#<palabra_secreta> si es correcto
     */
    public String verificaUsuario(String login) {
        obtenerConexion();
        
        String[] peticion = login.split("#");
        String theOutput = "PROTOCOLCRISTOPOP1.0#ERROR#BAD_LOGIN";
        boolean usuarioLogueado = false;

        try {
            Statement statement = this.conexion.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM USUARIO");
            while (resultSet.next()) {
                if ((resultSet.getString("login").equals(peticion[2])) && (resultSet.getString("password").equals(peticion[3]))) { // si usuario y contraseña encontrados
                    usuarioLogueado = true;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        if (usuarioLogueado == true) {
            try {
                theOutput = "PROTOCOLCRISTOPOP1.0#WELLCOME#" + peticion[2] + "#WITH_TOKEN#" + token(peticion[2]);
            } catch (NoSuchAlgorithmException ex) {
            }
        }

        cerrarConexion();
        
        return theOutput;
    }

    /*
        metodo que devuelve hash
        parametros String login que es el login del usuario autenticado
     */
    public String token(String login) throws NoSuchAlgorithmException {

        String sha = null;
        MessageDigest mensaje = MessageDigest.getInstance("SHA-256");

        mensaje.update(login.getBytes());

        byte[] digest = mensaje.digest();
        StringBuffer sb = new StringBuffer();

        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        sha = sb.toString();

        return sha;
    }

}
