package Modelo;
// Clase auxiliar para manejar productos en el carrito
public class ItemCarrito {
    private ProductosMenu producto;
    private int cantidad;

    public ItemCarrito(ProductosMenu producto) {
        this.producto = producto;
        this.cantidad = 1;
    }

    public void incrementarCantidad() {
        cantidad++;
    }

    public void decrementarCantidad() {
        cantidad--; // permite que llegue a 0
    }

    public double getSubtotal() {
        return producto.getPrecio() * cantidad;
    }

    public String toString() {
        return producto.getNombre() + " x" + cantidad + " - $" + String.format("%.2f", getSubtotal());
    }
    public int getCantidad() {
        return cantidad;
    }
    public ProductosMenu getProducto() {
        return producto;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ItemCarrito other = (ItemCarrito) obj;
        return producto.getNombre().equals(other.producto.getNombre());
    }

    @Override
    public int hashCode() {
        return producto.getNombre().hashCode();
    }
}