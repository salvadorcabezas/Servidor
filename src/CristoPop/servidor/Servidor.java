package CristoPop.servidor;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Servidor {

    public static Ventana ventana = new Ventana();
    public static ArrayList<HebraServer> arrayHebrasServer = new ArrayList<>();
    
    public static void main(String[] args) throws IOException {

        ArrayList<Thread> socketsServidorCliente = new ArrayList<>();
        //socketsServidorCliente.get(0).setName(name); para introducir nombre
        //socketsServidorCliente.get(0).interrupt(); para matar a la hebra
        //socketsServidorCliente.get(0).getName(); para obtener nombre
        int puerto = 5001;
        boolean listening = true;
        ventana.setVisible(true);

        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            while (listening) {
                HebraServer hebraServer = new HebraServer(serverSocket.accept(), ventana);
                arrayHebrasServer.add(hebraServer);
                socketsServidorCliente.add(new Thread(hebraServer));
                socketsServidorCliente.get(socketsServidorCliente.size() - 1).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + puerto);
            System.exit(-1);
        }
    }
}
