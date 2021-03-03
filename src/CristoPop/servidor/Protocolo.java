package CristoPop.servidor;

import Controlador.ControladorProducto;
import Controlador.ControladorUsuario;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Protocolo {

    ControladorUsuario controladorUsuario;
    ControladorProducto controladorProducto;
    Ventana ventana;

    boolean enviaImagen = false;
    boolean cargarArray = true;
    int numero_paquete = 0;

    public Protocolo(Ventana ventana) {
        controladorUsuario = new ControladorUsuario();
        controladorProducto = new ControladorProducto();
        this.ventana = ventana;
    }

    // metodo al que le pasas por parametro un mensaje y te devuelve otro
    public String processInput(String mensaje) throws NoSuchAlgorithmException, IOException {
        String[] peticion = mensaje.split("#");
        String theOutput = "";
        String[] respuesta;

        switch (peticion[1]) {
            case "LOGIN":
                theOutput = controladorUsuario.verificaUsuario(mensaje);
                respuesta = theOutput.split("#");
                if (respuesta[1].equals("WELLCOME")) {
                    ventana.areaTexto.append("Usuario logueado: " + respuesta[2] + "\n");
                } else {
                    ventana.areaTexto.append("Error de logueo: " + peticion[2] + "\n");
                }
                break;
            case "GET_PRODUCTS":
                if (token(peticion[2]).equals(peticion[3])) {
                    theOutput = controladorProducto.dameProductos();
                    respuesta = theOutput.split("#");
                    if (respuesta[1].equals("AVAILABLE_PRODUCTS")) {
                        ventana.areaTexto.append("Usuario: " + peticion[2] + " está visualizando los productos" + "\n");
                    }
                } else {
                    theOutput = "PROTOCOLCRISTOPOP1.0#ERROR#CANT_GET_FILES";
                }
                break;
            case "GET_ITEM":
                if (token(peticion[2]).equals(peticion[3])) {
                    theOutput = controladorProducto.dameItem(mensaje);
                    respuesta = theOutput.split("#");
                    if (respuesta[1].equals("GET_ITEM")) {
                        ventana.areaTexto.append("Usuario: " + peticion[2] + " está visualizando el producto " + respuesta[2] + "\n");
                    }
                } else {
                    theOutput = "PROTOCOLCRISTOPOP1.0#ERROR#CANT_GET_ITEM";
                }
                break;
            case "PREPARED_TO_RECEIVE":
                if (token(peticion[2]).equals(peticion[3])) {
                    theOutput = controladorProducto.dameImagen(mensaje);
                }
                break;
            case "BUY_PRODUCT":
                if (token(peticion[2]).equals(peticion[3])) {
                    theOutput = controladorProducto.compraProducto(mensaje);
                    respuesta = theOutput.split("#");
                    ventana.areaTexto.append("El usuario " + peticion[2] + " ha intendado comprar un ITEM " + respuesta[1] + "\n");
                }
                break;
            case "BYE":
                if (token(peticion[2]).equals(peticion[3])) {
                    theOutput = "PROTOCOLCRISTOPOP1.0#ADIOSXULO#"+peticion[2]+"#"+peticion[3];
                    ventana.areaTexto.append("El usuario " + peticion[2] + " Se ha desconectado\n");
                }
                break;
        }

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

// primero llamo al protocolo desde la HebraServer pasandole el mensaje del cliente, y la conexion para que los controladores puedan acceder a la base de datos
/* dentro del protocolo tendré los objetos controladores*/
// divido ese mensaje en el protocolo para saber que tipo de peticion se ha solicitado
// desde un menu swift llamaré a un metodo u otro de la clase controlador cliente o controlador producto
// no podré acceder al resto de metodos si no se ha logueado, esto lo controlare con un booleano
// cuando llame a un método, le pasare el String entero, el metodo se encargará de dividirlo y operar sobre la base de datos
// los metodos devolverán un string al protocolo
// el protocolo devolverá un string a la hebraServer
// la hebra server devolverá la información al cliente
