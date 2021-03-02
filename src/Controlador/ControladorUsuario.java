package Controlador;

import Modelo.Usuario;

public class ControladorUsuario {

    Usuario usuario = new Usuario();
    
    public ControladorUsuario() {
    }    
     
    public String verificaUsuario(String login){
        String theOutput = usuario.verificaUsuario(login);
        return theOutput;
    }
    
}
