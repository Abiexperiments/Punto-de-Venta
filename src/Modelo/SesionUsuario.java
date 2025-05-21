package Modelo;
public class SesionUsuario {
    private static Usuario usuarioActual;

    public static void setUsuarioActual(Usuario usuario) {
        usuarioActual = usuario;
    }

    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public static String getRol() {
        return usuarioActual != null ? usuarioActual.getRol() : null;
    }

    public static String getNombreUsuario() {
        return usuarioActual != null ? usuarioActual.getUsuario() : null;
    }
}