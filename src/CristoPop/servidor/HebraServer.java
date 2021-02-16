package CristoPop.servidor;

import java.net.*;
import java.io.*;
import java.sql.*;

// socket que conecta servidor con cliente
// desde aqui imprimiré el usuario que se ha conectado
// 
public class HebraServer implements Runnable{

    // tengo que instanciar controlador
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
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // creo escritor
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));) // creo lector
        {
            String inputLine, outputLine;
            
            Protocolo protocolo = new Protocolo(conexion);
            
            inputLine = in.readLine();
                        
            outputLine = protocolo.processInput(inputLine); // el string outputline sera igual a Usuario Correcto o Usuario Incorrecto
            
            System.err.println(outputLine); // imprimo el mensaje por terminal
            
            out.println(outputLine); // le escribo el mensaje al cliente

            while ((inputLine = in.readLine()) != null) { // leo lo que escribe el cliente
                outputLine = protocolo.processInput(inputLine); // llamo al metodo protocol para que me devuelva un String que es la respuesta
                out.println(outputLine); // le escribo el mensaje al cliente
                if (outputLine.equals("Bye")) { // si el mensaje era igual a Bye acabo
                    break;
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
