package CristoPop.servidor;

import java.net.*;
import java.io.*;
import java.sql.*;

public class HebraServer implements Runnable {

    private Socket socket = null;
    private Connection conexion = null;

    public HebraServer(Socket socket, Connection conexion) { // constructor, asigno el socket que le he pasado al socket de la hebra
        //super("KKMultiServerThread");
        this.socket = socket;
        this.conexion = conexion;
    }

    @Override
    public void run() {

        String userAddress = socket.getRemoteSocketAddress().toString(); // almaceno host y puerto
        System.out.println("Usuario conectado: " + userAddress); // imprimo por pantalla el host y puerto

        try (
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {

            String inputLine, outputLine;

            Protocolo protocolo = new Protocolo(conexion);

            while ((inputLine = in.readLine()) != null) { // BUCLE INFINITO, leo lo que escribe el cliente
                outputLine = protocolo.processInput(inputLine); // le paso al protocolo el mensaje del cliente y almaceno la respuesta del protocolo
                out.println(outputLine); // envio la respuesta al cliente
                // Debug que tengo que imprimir
                /*
                if (outputLine.equals("PROTOCOLCRISTOPOP1.0#ERROR#BAD_LOGIN")) {
                    System.err.println("Intento de logueo fallido"); // imprimo el mensaje por terminal a modo de DEBUG
                } else {
                    System.out.println("Usuario logueado correctamente");
                }*/
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
