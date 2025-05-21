package ClasesBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import Modelo.Proveedor;
import Modelo.ConexionBD;

public class ProveedorDAO {

    public void guardar(Proveedor proveedor) {
        String sql = "INSERT INTO Proveedores (ID, Nombre, Telefono, Correo, Calle, Colonia, Ciudad, Estado, CP, TipoProducto) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, proveedor.getId());
            pstmt.setString(2, proveedor.getNombre());
            pstmt.setString(3, proveedor.getTelefono());
            pstmt.setString(4, proveedor.getCorreo());
            pstmt.setString(5, proveedor.getCalle());
            pstmt.setString(6, proveedor.getColonia());
            pstmt.setString(7, proveedor.getCiudad());
            pstmt.setString(8, proveedor.getEstado());
            pstmt.setString(9, proveedor.getCp());
            pstmt.setString(10, proveedor.getTipoProducto());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar proveedor: " + e.getMessage());
        }
    }

    public void actualizar(Proveedor proveedor) {
        String sql = "UPDATE Proveedores SET Nombre=?, Telefono=?, Correo=?, Calle=?, Colonia=?, Ciudad=?, Estado=?, CP=?, TipoProducto=? WHERE ID=?";

        try (Connection conexion = ConexionBD.getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setString(1, proveedor.getNombre());
            pstmt.setString(2, proveedor.getTelefono());
            pstmt.setString(3, proveedor.getCorreo());
            pstmt.setString(4, proveedor.getCalle());
            pstmt.setString(5, proveedor.getColonia());
            pstmt.setString(6, proveedor.getCiudad());
            pstmt.setString(7, proveedor.getEstado());
            pstmt.setString(8, proveedor.getCp());
            pstmt.setString(9, proveedor.getTipoProducto());
            pstmt.setInt(10, proveedor.getId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar proveedor: " + e.getMessage());
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM Proveedores WHERE ID = ?";

        try (Connection conexion = ConexionBD.getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar proveedor: " + e.getMessage());
        }
    }

    public List<Proveedor> obtenerTodos() {
        List<Proveedor> proveedores = new ArrayList<>();
        String sql = "SELECT * FROM Proveedores";

        try (Connection conexion = ConexionBD.getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Proveedor proveedor = new Proveedor();//me da error
                proveedor.setId(rs.getInt("ID"));
                proveedor.setNombre(rs.getString("Nombre"));
                proveedor.setTelefono(rs.getString("Telefono"));
                proveedor.setCorreo(rs.getString("Correo"));
                proveedor.setCalle(rs.getString("Calle"));
                proveedor.setColonia(rs.getString("Colonia"));
                proveedor.setCiudad(rs.getString("Ciudad"));
                proveedor.setEstado(rs.getString("Estado"));
                proveedor.setCp(rs.getString("CP"));
                proveedor.setTipoProducto(rs.getString("TipoProducto"));
                proveedores.add(proveedor);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener proveedores: " + e.getMessage());
        }

        return proveedores;
    }


    public List<Proveedor> buscar(String texto) {
        List<Proveedor> resultados = new ArrayList<>();
        String sql = "SELECT * FROM Proveedores WHERE " +
                     "LOWER(Nombre) LIKE ? OR LOWER(Colonia) LIKE ? OR LOWER(Calle) LIKE ? OR LOWER(Estado) LIKE ? OR " +
                     "LOWER(Correo) LIKE ? OR LOWER(TipoProducto) LIKE ? OR LOWER(Telefono) LIKE ? OR CAST(ID AS TEXT) LIKE ? OR CAST(CP AS TEXT) LIKE ?";

        try (Connection conexion = ConexionBD.getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            String wildcard = "%" + texto.toLowerCase() + "%";
            for (int i = 1; i <= 9; i++) {
                pstmt.setString(i, wildcard);
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Proveedor p = new Proveedor(
                    rs.getInt("ID"),
                    rs.getString("Nombre"),
                    rs.getString("Telefono"),
                    rs.getString("Correo"),
                    rs.getString("Calle"),
                    rs.getString("Colonia"),
                    rs.getString("Ciudad"),
                    rs.getString("Estado"),
                    rs.getString("CP"),
                    rs.getString("TipoProducto")
                );
                resultados.add(p);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar proveedores: " + e.getMessage());
        }

        return resultados;
    }   
}