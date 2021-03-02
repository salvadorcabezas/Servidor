package Modelo;

import CristoPop.servidor.Conexion;
import CristoPop.servidor.Servidor;
import java.io.BufferedInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

public class Producto extends Conexion {

    private int Cod_prod;
    private String nombre;
    private String precio;
    private String ruta_foto;
    private String visualizaciones;
    private String descripcion;
    private String login;
    private String fecha_compra;
    private ArrayList<Producto> array_productos;

    public boolean enviaImagen = false;
    private boolean cargarArray = true; // cuando mande el ultimo paquete cargarArray sera = true
    private int numero_paquete = 0;
    private ArrayList<String> imagen_base64 = new ArrayList<>();
    byte[] bFile;
    byte[] data2;
    Byte a;

    public Producto() {
        this.nombre = "";
        this.precio = "";
        this.ruta_foto = "";
        this.visualizaciones = "";
        this.descripcion = "";
        this.login = "";
        this.fecha_compra = "";
        this.array_productos = new ArrayList<>();
    }

    public Producto(String nombre, String precio, String ruta_foto, String visualizaciones, String descripcion, String login, String fecha_compra, ArrayList<Producto> array_productos) {
        this.nombre = nombre;
        this.precio = precio;
        this.ruta_foto = ruta_foto;
        this.visualizaciones = visualizaciones;
        this.descripcion = descripcion;
        this.login = login;
        this.fecha_compra = fecha_compra;
        this.array_productos = array_productos;
    }

    public int getCod_prod() {
        return Cod_prod;
    }

    public void setCod_prod(int Cod_prod) {
        this.Cod_prod = Cod_prod;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getRuta_foto() {
        return ruta_foto;
    }

    public void setRuta_foto(String ruta_foto) {
        this.ruta_foto = ruta_foto;
    }

    public String getVisualizaciones() {
        return visualizaciones;
    }

    public void setVisualizaciones(String visualizaciones) {
        this.visualizaciones = visualizaciones;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFecha_compra() {
        return fecha_compra;
    }

    public void setFecha_compra(String fecha_compra) {
        this.fecha_compra = fecha_compra;
    }

    public ArrayList<Producto> getArray_productos() {
        return array_productos;
    }

    public void setArray_productos(ArrayList<Producto> array_productos) {
        this.array_productos = array_productos;
    }

    /*
        metodo que devuelve String con algunos datos de varios objetos productos
        parametros: String mensaje del tipo PROTOCOLCRISTOPOP1.0#GET_PRODUCTS#<user_login>#<palabra_secreta>
     */
    public String dameProductos() {
        obtenerConexion();

        String productos = "";
        String mensaje = "";
        int rowCount = 0;
        try {
            Statement statement = this.conexion.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM PRODUCTO WHERE login_compra IS NULL");
            while (resultSet.next()) {
                productos = productos + "#" + resultSet.getString("codigo_producto") + "@" + resultSet.getString("nombre")
                        + "@" + resultSet.getString("precio") + "@" + resultSet.getString("visualizaciones");
                rowCount++;
            }
            mensaje = "PROTOCOLCRISTOPOP1.0#AVAILABLE_PRODUCTS#" + rowCount + productos;
        } catch (Exception e) {
            System.out.println(e);
            mensaje = "PROTOCOLCRISTOPOP1.0#ERROR#CANT_GET_FILES";
        }

        cerrarConexion();

        return mensaje;
    }

    /*
        metodo que devuelve String con los datos de un objeto producto
        parametros: String mensaje del tipo PROTOCOLCRISTOPOP1.0#GET_ITEM#<user_login>#<token>#<cod_prod>
     */
    public String dameItem(String dameItem) {
        obtenerConexion();

        String[] peticion = dameItem.split("#");
        String item = "";
        String mensaje = "";
        String visualizaciones = "";
        String login = "";
        String sql;

        try {
            Statement statement = this.conexion.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM PRODUCTO WHERE codigo_producto='" + peticion[4] + "'");
            while (resultSet.next()) {
                visualizaciones = resultSet.getString("visualizaciones");
                login = resultSet.getString("login");
                Cod_prod = Integer.parseInt(resultSet.getString("codigo_producto"));

                item = resultSet.getString("codigo_producto") + "@" + resultSet.getString("nombre") + "@" + resultSet.getString("login") + "@"
                        + resultSet.getString("precio") + "@" + resultSet.getString("visualizaciones") + "@" + resultSet.getString("descripcion")
                        + "#" + dameFormatoImagen(dameItem, login) + "#" + dameTamanioImagen(dameItem, login);
            }

            mensaje = "PROTOCOLCRISTOPOP1.0#GET_ITEM#" + item;
        } catch (Exception e) {
            System.out.println(e);
            mensaje = "PROTOCOLCRISTOPOP1.0#ERROR#CANT_GET_ITEM";
        }

        sql = "UPDATE producto SET visualizaciones = " + (Integer.parseInt(visualizaciones) + 1) + " WHERE codigo_producto = " + peticion[4];
        try (PreparedStatement preparedStatement = this.conexion.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        cerrarConexion();

        return mensaje;
    }

    // metodo que devuelve el formato de la imagen
    public String dameFormatoImagen(String item, String login) {
        String formato[];
        String[] peticion = item.split("#");

        File carpeta = new File(".\\" + login + "\\" + peticion[4]);
        String[] listado = carpeta.list();
        formato = (listado[0]).split("[.]");

        return formato[1];
    }

    //metodo que devuelve el tamaño en bytes de la imagen
    public long dameTamanioImagen(String item, String login) {
        String[] peticion = item.split("#");
        long tamanio;

        File carpeta = new File(".\\" + login + "\\" + peticion[4]);
        String[] listado = carpeta.list();

        File f = new File(".\\" + login + "\\" + peticion[4] + "\\" + listado[0]);
        tamanio = f.length();

        return tamanio;
    }

    /*
        metodo que devuelve String en base 64, correspondiente a un paquete de bytes de tamaño 512
        parametros: String mensaje del tipo PROTOCOLCRISTOPOP1.0#PREPARED_TO_RECEIVE#<user_login>#<token>#<cod_prod>#SIZE_PACKAGE#<size_package>
     */
    public String dameImagen(String dameItem) throws IOException {

        enviaImagen = true;
        String login = "";
        String[] peticion;
        String mensaje = "";

        // en la primera iteracion tendré que dividir el archivo en bytes, y rellenar un array de String en el que cada posicion del string contenga 512 bytes
        if (cargarArray == true) {
            cargarArray = false;
            // consulto en la base de datos el id del archivo
            obtenerConexion();
            peticion = dameItem.split("#");
            try {
                Statement statement = this.conexion.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM PRODUCTO WHERE codigo_producto='" + peticion[4] + "'");
                while (resultSet.next()) {
                    login = resultSet.getString("login");
                }
            } catch (Exception e) {
                System.out.println(e);
            }
            cerrarConexion();

            // cargo el archivo para convertirlo a byte[]
            File carpeta = new File(".\\" + login + "\\" + peticion[4]);
            String[] listado = carpeta.list();
            File image = new File(".\\" + login + "\\" + peticion[4] + "\\" + listado[0]);
            FileInputStream fileInputStream;

            try {
                //convert file into array of bytes
                fileInputStream = new FileInputStream(image);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                bFile = bufferedInputStream.readAllBytes();

                fileInputStream.close(); // importante cerrar 
                bufferedInputStream.close(); // importante cerrar
            } catch (Exception e) {
                e.printStackTrace();
            }

            // bucle para recoger de 512 en 512 del array de byte, codificarlo a Base64 y añadirlo a arrayList String
            for (int n_bytes = 0; n_bytes < bFile.length; n_bytes += 512) {
                byte[] aux = new byte[512];
                int j = 0;
                for (int i = n_bytes; i < n_bytes + 512 && i < bFile.length; i++) { // relleno los paquetes consecutivamente, 0-511, 512-1023, ...
                    aux[j] = bFile[i];
                    j++;
                }
                String encodedString = Base64.getEncoder().encodeToString(aux); // convierto el paquete a Base64
                imagen_base64.add(encodedString); // añado paquete al arrayList String
            }
        }

        mensaje = "PROTOCOLCRISTOPOP1.0#" + Cod_prod + "#512" + "#" + imagen_base64.get(numero_paquete);

        if (numero_paquete < imagen_base64.size() - 1) {
            numero_paquete++;
        } else {
            numero_paquete = 0;
            cargarArray = true;
            enviaImagen = false;
            imagen_base64.clear();
        }

        return mensaje;
    }

    /*
        metodo que devuelve String con los datos de un objeto producto
        parametros: String mensaje del tipo PROTOCOLCRISTOPOP1.0#BUY_PRODUCT#<user_login>#<token>#<cod_prod>
     */
    public String compraProducto(String mensajeCliente) {
        obtenerConexion();

        String[] peticion = mensajeCliente.split("#");
        String item = "";
        String mensaje = "";
        String sql;
        boolean estaComprado = false;

        try {
            Statement statement = this.conexion.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM PRODUCTO WHERE codigo_producto='" + peticion[4] + "'");
            while (resultSet.next()) {
                item = resultSet.getString("login_compra");
                System.err.println("DEBUGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG: " + item);
                if (item.equals("null")) {
                    estaComprado = false;
                } else {
                    estaComprado = true;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        if (estaComprado == false) {
            sql = "UPDATE producto SET login_compra = '" + peticion[2] + "' WHERE codigo_producto = " + peticion[4];
            try (PreparedStatement preparedStatement = this.conexion.prepareStatement(sql)) {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            mensaje = "PROTOCOLCRISTOPOP1.0#BUY_ACCEPTED#" + peticion[4] + "#" + peticion[2];

            // si este producto se ha comprado, tendré que recorrer el array de hebras y llamar al metodo de estas hebras para enviar mensaje a clientes
            for (int i = 0; i < Servidor.arrayHebrasServer.size(); i++) {
                Servidor.arrayHebrasServer.get(i).itemASidoComprado(peticion[4], peticion[2]);
            }
        } else {
            mensaje = "PROTOCOLOCRISTOPOP1.0#BUY_REJECTED#" + peticion[4] + "#" + peticion[2];
        }

        cerrarConexion();

        return mensaje;
    }

}
