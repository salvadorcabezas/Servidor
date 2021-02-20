package CristoPop.servidor;

import Controlador.ControladorProducto;
import Controlador.ControladorUsuario;
import java.sql.*;

public class Protocolo {

    Connection conexion = null;
    ControladorUsuario controladorUsuario;
    ControladorProducto controladorProducto;

    public Protocolo(Connection conexion) {
        this.conexion = conexion;
        controladorUsuario = new ControladorUsuario(conexion);
        controladorProducto = new ControladorProducto(conexion);
    }

    // metodo al que le pasas por parametro un mensaje y te devuelve otro
    public String processInput(String mensaje) {
        String[] peticion = mensaje.split("#");
        String theOutput = "";

        switch (peticion[1]) {
            case "LOGIN":
                theOutput = controladorUsuario.verificaUsuario(mensaje);
                break;
            case "GET_PRODUCTS":
                theOutput = controladorProducto.dameProductos();
                break;
            case "GET_ITEM":
                System.out.println("");
                break;
            case "PREPARED_TO_RECEIVE":
                System.out.println("");
                break;
        }

        return theOutput;
    }
}

// primero llamo al protocolo desde la HebraServer pasandole el mensaje del cliente, y la conexion para que los controladores puedan acceder a la base de datos
/* dentro del protocolo tendré los objetos controladores*/
// divido ese mensaje en el protocolo para saber que tipo de peticion se ha solicitado
// desde un menu swift llamaré a un metodo u otro de la clase controlador cliente o controlador producto
// no podré acceder al resto de metodos si no se ha logueado, esto lo controlare con un booleano
// cuando llame a un método, le pasare el String entero, el metodo se encargará de dividirlo y operar sobre la base de datos
// los metodos devolverán un string al protocolo
// el protocolo devolverá un string a la hebraServer
// la hebra server devolverá la información al cliente
