package Patrones.Factory;

import Modelo.ProductosMenu;

public class ProductoMenuFactory {

    public static ProductosMenu crearProducto(String nombre, String precioStr, String categoria, String rutaImagen) throws NumberFormatException {
        double precio = Double.parseDouble(precioStr);
        ProductosMenu producto = new ProductosMenu(nombre, precio, categoria);
        producto.setRutaImagen(rutaImagen != null ? rutaImagen : "");
        return producto;
    }
}
