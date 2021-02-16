package cliente;

import java.io.*;
import java.net.*;

// interfaz en el que tendré que añadir usuario y contraseña                mas ip y puerto en una hebra (opcional)
// dependiendo de lo que me envie el servidor, creare una nueva hebra con otra interfaz en el que imprimiré tabla con productos
public class Cliente {

    public static void main(String[] args) throws IOException {

        String hostName = "193.39.92.110";
        int portNumber = 4001;

        String user = "Salva";
        String pwd = "josegras";

        try (
                Socket cliente_servidor = new Socket(hostName, portNumber); // creo socket y establezco conexion con el SERVIDOR
                PrintWriter out = new PrintWriter(cliente_servidor.getOutputStream(), true); // escritor
                BufferedReader in = new BufferedReader(new InputStreamReader(cliente_servidor.getInputStream())); // lector
                ) {

            String fromServer;
            String fromUser;

            // CAMBIAR siguiente sentencia
            BufferedReader stdInUsuario = new BufferedReader(new InputStreamReader(System.in)); // creo un lector para leer lo que escribo por terminal
            BufferedReader stdInContrasenia = new BufferedReader(new InputStreamReader(System.in)); // creo un lector para leer lo que escribo por terminal

            do {
                System.out.println("");
                System.out.println("Introduce usuario: ");
                user = stdInUsuario.readLine();
                
                System.out.println("Introduce contraseña: ");
                pwd = stdInContrasenia.readLine();
                System.out.println("");
                // ENVIO usuario y contraseña para establecer conexion con la BD
                out.println("PROTOCOLCRISTOPOP1.0#LOGIN#" + user + "#" + pwd); // REMPLAZAR con interfaz grafica
                fromServer = in.readLine();
                System.out.println(fromServer);
            } while (fromServer.equals("Usuario incorrecto"));

            // RECIBIR la respuesta del servidor, si el mensaje es OK inicio una ventana grafica con tabla, si no, continuo escuchando

            /*while ((fromServer = in.readLine()) != null) { // leo lo que me envia el servidor
                
                // if (fromServer == Incorrecto){
            
                    System.out.println("Server: " + fromServer); // imprimo lo que me envia el servidor

                    fromUser = stdIn.readLine(); // escribo en la terminal

                    if (fromUser != null) {
                        System.out.println("Client: " + fromUser); // imprimo lo que he escrito
                        out.println(fromUser); // envio lo que he escrito
                    }
                }else{
                    // le paso el mensaje al protocolo del cliente
                    // el protocolo me devuelve un array de strings
                    // imprimo ese array de strings en la tabla
                }
            }*/
 /*
            while ((fromServer = in.readLine()) != null) { // leo lo que me envia el servidor

                System.out.println("Server: " + fromServer); // lo imprimo
                if (fromServer.equals("Bye.")) // si el mensaje es Bye acabo
                {
                    break;
                }

                fromUser = stdIn.readLine(); // escribo en la terminal
                if (fromUser != null) {
                    System.out.println("Client: " + fromUser); // imprimo lo que he escrito
                    out.println(fromUser); // se lo envio al servidor
                }
            }*/
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }
    }
}
