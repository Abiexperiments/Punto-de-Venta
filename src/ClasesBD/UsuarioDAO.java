package ClasesBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import Modelo.Usuario;
import Modelo.ConexionBD;

public class UsuarioDAO {
    public Usuario verificarUsuario(String usuario, String contraseña) {
        try {
            Connection con = ConexionBD.getConexion();
            PreparedStatement stmt = con.prepareStatement(
                "SELECT * FROM Usuarios WHERE nombre_usuario=? AND contraseña=?"
            );
            stmt.setString(1, usuario);
            stmt.setString(2, contraseña);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre_usuario");
                String pass = rs.getString("contraseña");
                String rol = rs.getString("rol");

                return new Usuario(id, nombre, pass, rol);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error en DB: " + e.getMessage());
        }

        return null; // no encontrado
    }
    public static boolean agregarUsuario(String nombre, String contraseña, String rol) {
        String sql = "INSERT INTO usuarios (nombre_usuario, contraseña, rol) VALUES (?, ?, ?)";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.setString(2, contraseña);
            stmt.setString(3, rol);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al agregar usuario: " + e.getMessage());
            return false;
        }}


    public static List<Object[]> obtenerUsuarios() {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (Connection con = ConexionBD.getConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre_usuario");
                String contraseña = rs.getString("contraseña");
                String rol = rs.getString("rol");

                lista.add(new Object[]{id, nombre, contraseña, rol});
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener usuarios: " + e.getMessage());
        }

        return lista;
    }
    public static boolean modificarUsuario(int id, String nombre, String contraseña, String rol) {
        String sql = "UPDATE usuarios SET nombre_usuario = ?, contraseña = ?, rol = ? WHERE id = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.setString(2, contraseña);
            stmt.setString(3, rol);
            stmt.setInt(4, id);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al modificar usuario: " + e.getMessage());
            return false;
        }
    }


    public static boolean eliminarUsuario(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

}