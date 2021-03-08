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
    String nombre; // tengo que añadirlo al constructor, tengo que crear metodo que devuelva nombre, de esta manera identificaré a los hilos, puede que tenga que crear ese metoddo como public static

    public HebraServer(Socket socket, Ventana ventana) throws IOException {
        this.socket = socket;
        this.ventana = ventana;
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        String userAddress = socket.getRemoteSocketAddress().toString();
        ventana.areaTexto.append("Usuario conectado: " + userAddress + "\n");
        
        try {
            String inputLine, outputLine;
            Protocolo protocolo = new Protocolo(ventana);

            while (((inputLine = in.readLine()) != null) && apagar == false) { // BUCLE INFINITO, leo lo que me manda el cliente
                
                do { //DO WHILE, mientras esté enviando una imagen
                    outputLine = protocolo.processInput(inputLine);
                    out.println(outputLine);
                } while (protocolo.controladorProducto.producto.enviaImagen == true);
                compruebaMensaje = inputLine.split("#");
                if (compruebaMensaje[1].equals("BYE")) {
                    apagar = true;
                }
            }
            
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

    // método que envia mensaje especifico al cliente de que un usuario a comprado
    public void itemASidoComprado(String codigoProducto, String userLogin) {
        out.println("PROTOCOLCRISTOPOP1.0#BUY_ACCEPTED#" + codigoProducto + "#" + userLogin);
    }
}
