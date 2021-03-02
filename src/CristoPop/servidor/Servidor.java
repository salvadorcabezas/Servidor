package CristoPop.servidor;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Servidor {

    public static Ventana ventana = new Ventana();
    
    public static void main(String[] args) throws IOException {

        ArrayList<Thread> socketsServidorCliente = new ArrayList<Thread>();
        int puerto = 4001;
        boolean listening = true;
        ventana.setVisible(true);

        try (ServerSocket serverSocket = new ServerSocket(puerto)) { // creo el serverSocket
            while (listening) {
                HebraServer hebraServer = new HebraServer(serverSocket.accept(), ventana); //thread, le paso por parametro el socket
                socketsServidorCliente.add(new Thread(hebraServer)); // añado hebra al array de hebras
                socketsServidorCliente.get(socketsServidorCliente.size() - 1).start(); // inicio el thread
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + puerto);
            System.exit(-1);
        }
    }
}
/*
    Tengo que crear la interfaz aqui e instanciarla en cada clase HebraServer para no crear una ventana nueva cada vez que creo un socket
    igualar la ventana que le paso por parametros
*/
