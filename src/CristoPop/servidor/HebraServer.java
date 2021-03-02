package CristoPop.servidor;

import java.net.*;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HebraServer implements Runnable {

    private Socket socket = null;
    Ventana ventana;

    public HebraServer(Socket socket, Ventana ventana) {
        this.socket = socket;
        this.ventana = ventana;
    }

    @Override
    public void run() {
        String userAddress = socket.getRemoteSocketAddress().toString(); // METODO PARA OBTENER HOST Y PUERTO
        //System.out.println("Usuario conectado: " + userAddress); // imprimo por pantalla el host y puerto
        ventana.areaTexto.append("Usuario conectado: " + userAddress + "\n");
        try (
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
            String inputLine, outputLine;
            Protocolo protocolo = new Protocolo(ventana);

            while ((inputLine = in.readLine()) != null) { // BUCLE INFINITO, leo lo que escribe el cliente
                //DO WHILE ENVIAImagen==TRUE
                do {
                    outputLine = protocolo.processInput(inputLine); // le paso al protocolo el mensaje del cliente y almaceno la respuesta en el outputline
                    out.println(outputLine); // envio la respuesta al cliente
                } while (protocolo.controladorProducto.producto.enviaImagen == true);
            }
            // BOOLEANO QUE ITERE TANTAS VECES HASTA QUE EL PROTOCOLO INDIQUE QUE YA SE HA ENVIADO LA IMAGEN, EL PROTOCOLO CAMBIARÁ EL BOLEANO

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(HebraServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
