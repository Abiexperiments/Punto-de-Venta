package Modelo;

public class Usuario {
    private int id;
    private String usuario;
    private String contraseña;
    private String rol; 
    
    public Usuario(int id,  String usuario, String contraseña, String rol) {
        this.id = id;
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.rol = rol;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getContraseña() {
		return contraseña;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}
    
}
