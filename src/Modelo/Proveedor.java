package Modelo;

public class Proveedor {
    private int id;
    private String nombre, telefono, correo, calle, colonia, ciudad, estado, cp, tipoProducto;

    // Constructor con id
    public Proveedor(int id, String nombre, String telefono, String correo, String calle,
                     String colonia, String ciudad, String estado, String cp,
                     String tipoProducto) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.calle = calle;
        this.colonia = colonia;
        this.ciudad = ciudad;
        this.estado = estado;
        this.cp = cp;
        this.tipoProducto = tipoProducto;
    }


    public Proveedor() {
		// TODO Auto-generated constructor stub
	}


	// Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getCorreo() { return correo; }
    public String getCiudad() { return ciudad; }
    public String getTipoProducto() { return tipoProducto; }
    public String getCalle() { return calle; }
    public String getColonia() { return colonia; }
    public String getEstado() { return estado; }
    public String getCp() { return cp; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setCalle(String calle) { this.calle = calle; }
    public void setColonia(String colonia) { this.colonia = colonia; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setCp(String cp) { this.cp = cp; }
    public void setTipoProducto(String tipoProducto) { this.tipoProducto = tipoProducto; }
}