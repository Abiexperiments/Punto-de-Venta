package Modelo;

import java.util.Date;

public class ProductoInventario {
 

    private int id;
    private String nombre;
    private int cantidad;
    private String categoria;
    private String unidadMedida; 
    private Date fechaRecepcion;
    private Date fechaCaducidad;
    private String estado;


    private int stockMin;
    private int stockMax;
    
    public ProductoInventario(String nombre, int cantidad, String categoria, String unidadMedida,
            Date fechaRecepcion, Date fechaCaducidad, int stockMin, int stockMax) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.categoria = categoria;
        this.unidadMedida = unidadMedida;
        this.fechaRecepcion = fechaRecepcion;
        this.fechaCaducidad = fechaCaducidad;
        this.stockMin = stockMin;
        this.stockMax = stockMax;
        actualizarEstado();
    }


    public ProductoInventario(int id, String nombre, int cantidad, String categoria, String unidadMedida,
            Date fechaRecepcion, Date fechaCaducidad, int stockMin, int stockMax) {
this.id = id;
this.nombre = nombre;
this.cantidad = cantidad;
this.categoria = categoria;
this.unidadMedida = unidadMedida;
this.fechaRecepcion = fechaRecepcion;
this.fechaCaducidad = fechaCaducidad;
this.stockMin = stockMin;
this.stockMax = stockMax;
actualizarEstado();
}

    public ProductoInventario() {
		// TODO Auto-generated constructor stub
	}

	public int getId() { return id; }
    public String getNombre() { return nombre; }
    public int getCantidad() { return cantidad; }
    public String getCategoria() { return categoria; }
    public String getUnidadMedida() { return unidadMedida; }
	public int getStockMin() { return stockMin; }
    public int getStockMax() { return stockMax; }
    
    public String getEstado() {
        return estado;
    }

    private void actualizarEstado() {
        if (cantidad < stockMin) {
            estado = "Bajo";
        } else if (cantidad > stockMax) {
            estado = "Sobre Stock";
        } else {
            estado = "En Stock";
        }
    }

    public Date getFechaRecepcion() { return this.fechaRecepcion; }
    public Date getFechaCaducidad() { return this.fechaCaducidad; }

    public void setFechaRecepcion(Date fechaRecepcion) { this.fechaRecepcion = fechaRecepcion; }
    public void setFechaCaducidad(Date fechaCaducidad) { this.fechaCaducidad = fechaCaducidad; }
    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        actualizarEstado();
    }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }
    public void setStockMin(int stockMin) {
        this.stockMin = stockMin;
        actualizarEstado();
    }
    public void setStockMax(int stockMax) {
        this.stockMax = stockMax;
        actualizarEstado();
    }
}