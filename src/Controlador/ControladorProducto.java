package Controlador;

import Modelo.Producto;
import java.io.IOException;

public class ControladorProducto {

    public Producto producto = new Producto();

    public ControladorProducto() {
    }

    public String dameProductos() {
        String mensaje = producto.dameProductos();
        return mensaje;
    }

    public String dameItem(String dameItem) {
        String mensaje = producto.dameItem(dameItem);
        return mensaje;
    }
    
    public String dameImagen(String dameImagen) throws IOException{
        String mensaje = producto.dameImagen(dameImagen);
        return mensaje;
    }
    

}
