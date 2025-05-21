package Modelo;

import java.util.Date;
import java.util.List;

public class Venta {
    private int idVenta;
    private List<String> productos;
    private double total;
    private Date fecha;
    private String metodoPago; 

    public Venta(int idVenta, List<String> productos, double total, Date fecha, String metodoPago) {
        this.idVenta = idVenta;
        this.productos = productos;
        this.total = total;
        this.fecha = fecha;
        this.metodoPago = metodoPago;
    }

    // Getters y setters

    public int getIdVenta() {
        return idVenta;
    }

    public List<String> getProductos() {
        return productos;
    }

    public double getTotal() {
        return total;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
}
