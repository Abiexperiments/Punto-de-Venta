package Modelo;

public class ProductosMenu {
	 private int id;
    private String nombre;
    private double precio;
    private String categoria;
    private String rutaImagen; // NUEVO CAMPO

    public ProductosMenu(String nombre, double precio, String categoria) {
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
        this.rutaImagen = null; // Por defecto sin imagen
    }

    // Constructor adicional con imagen
    public ProductosMenu(String nombre, double precio, String categoria, String rutaImagen) {
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
        this.rutaImagen = rutaImagen;
    }
 // NUEVO constructor con ID
    public ProductosMenu(int id, String nombre, double precio, String categoria, String rutaImagen) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
        this.rutaImagen = rutaImagen;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    @Override
    public String toString() {
        return nombre + " - $" + String.format("%.2f", precio);
    }
}
