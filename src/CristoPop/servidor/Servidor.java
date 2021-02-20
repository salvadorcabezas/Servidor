package CristoPop.servidor;

import java.net.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;

// clase encargada de escuchar a nuevos clientes, si se conecta cliente se crea hebra
public class Servidor {

    public static void main(String[] args) throws IOException {

        Conexion conexion = new Conexion();

        //array de hebras de sockets
        ArrayList<Thread> socketsServidorCliente = new ArrayList<Thread>();

        int portNumber = 4001;
        boolean listening = true;
        int numeroHebra = 0;

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) { // creo el serverSocket

            while (listening) {
                HebraServer hebraServer = new HebraServer(serverSocket.accept(), conexion.obtenerConexion()); //thread, le paso por parametro el socket y el objeto Connection
                socketsServidorCliente.add(new Thread(hebraServer)); // añado hebra al array de hebras
                socketsServidorCliente.get(numeroHebra).start(); // inicio el thread
                numeroHebra++;
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }
}
