package ClasesBD;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import javax.swing.*;

import Modelo.Venta;
import Modelo.ConexionBD;

import java.text.SimpleDateFormat;

public class ModeloVentasDAO{

    public static List<Venta> obtenerVentas() {
        List<Venta> ventas = new ArrayList<>();

        String sql = "SELECT * FROM Ventas";

        try (Connection conn = ConexionBD.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("ID");
                String productos = rs.getString("Productos");
                double total = rs.getDouble("Total");
                Timestamp fecha = rs.getTimestamp("FechaVenta");
                String metodopago = rs.getString("MetodoPago");

                ventas.add(new Venta(id, Arrays.asList(productos.split(",")), total, new Date(fecha.getTime()), metodopago));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener ventas: " + e.getMessage());
        }

        return ventas;
    }

    public static void eliminarVentaPorId(int id) {
        String sql = "DELETE FROM Ventas WHERE ID = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar venta: " + e.getMessage());
        }
    }

    public static void agregarVenta(Venta venta) {
        String sql = "INSERT INTO Ventas (Productos, Total, FechaVenta, MetodoPago) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, String.join(",", venta.getProductos()));
            pstmt.setDouble(2, venta.getTotal());
            pstmt.setTimestamp(3, new java.sql.Timestamp(venta.getFecha().getTime()));
            pstmt.setString(4, venta.getMetodoPago());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al agregar venta: " + e.getMessage());
        }	
	}
    
    //para los id de los tickets 
    public static int agregarVentaYObtenerID(Venta venta) {
        int idVenta = -1;
        String sql = "INSERT INTO Ventas (Productos, Total, FechaVenta, MetodoPago) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, String.join(",", venta.getProductos()));
            pstmt.setDouble(2, venta.getTotal());
            pstmt.setTimestamp(3, new java.sql.Timestamp(venta.getFecha().getTime()));
            pstmt.setString(4, venta.getMetodoPago());

            int filas = pstmt.executeUpdate();
            if (filas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    idVenta = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al agregar venta y obtener ID: " + e.getMessage());
        }

        return idVenta;
    }
    
}
