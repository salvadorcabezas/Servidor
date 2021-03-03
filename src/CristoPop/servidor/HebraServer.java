package CristoPop.servidor;

import java.net.*;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HebraServer implements Runnable {

    private Socket socket = null;
    String compruebaMensaje[];
    Ventana ventana;
    PrintWriter out;
    BufferedReader in;
    boolean apagar = false;

    public HebraServer(Socket socket, Ventana ventana) throws IOException {
        this.socket = socket;
        this.ventana = ventana;
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        String userAddress = socket.getRemoteSocketAddress().toString(); // METODO PARA OBTENER HOST Y PUERTO
        //System.out.println("Usuario conectado: " + userAddress); // imprimo por pantalla el host y puerto
        ventana.areaTexto.append("Usuario conectado: " + userAddress + "\n");
        try {
            String inputLine, outputLine;
            Protocolo protocolo = new Protocolo(ventana);

            while (((inputLine = in.readLine()) != null) && apagar == false) { // BUCLE INFINITO, leo lo que escribe el cliente
                //DO WHILE ENVIAImagen==TRUE
                do {
                    outputLine = protocolo.processInput(inputLine); // le paso al protocolo el mensaje del cliente y almaceno la respuesta en el outputline
                    out.println(outputLine); // envio la respuesta al cliente
                } while (protocolo.controladorProducto.producto.enviaImagen == true);
                compruebaMensaje = inputLine.split("#");
                if (compruebaMensaje[1].equals("BYE")) {
                    apagar = true;
                }
            }
            // BOOLEANO QUE ITERE TANTAS VECES HASTA QUE EL PROTOCOLO INDIQUE QUE YA SE HA ENVIADO LA IMAGEN, EL PROTOCOLO CAMBIARÁ EL BOLEANO
            in.close();
            out.close();
            socket.close();
            System.gc();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(HebraServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void itemASidoComprado(String codigoProducto, String userLogin) {
        out.println("PROTOCOLCRISTOPOP1.0#BUY_ACCEPTED#" + codigoProducto + "#" + userLogin);
    }
}
