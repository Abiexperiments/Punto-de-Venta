package ClasesBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Modelo.ProductoInventario;
import Modelo.ConexionBD;

public class InventarioDAO {

    public void eliminarProducto(int id) throws SQLException {
        String sql = "DELETE FROM Inventario WHERE IdProducto = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void modificarProducto(ProductoInventario producto) throws SQLException {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser null.");
        }
        if (producto.getFechaRecepcion() == null || producto.getFechaCaducidad() == null) {
            throw new IllegalArgumentException("Las fechas de recepción y caducidad no pueden ser null.");
        }

        String sql = "UPDATE Inventario SET Nombre=?, Cantidad=?, Categoria=?, UnidadMedida=?, FechaRecepcion=?, FechaCaducidad=?, StockMin=?, StockMax=? WHERE IdProducto=?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre());
            stmt.setInt(2, producto.getCantidad());
            stmt.setString(3, producto.getCategoria());
            stmt.setString(4, producto.getUnidadMedida());
            stmt.setDate(5, new java.sql.Date(producto.getFechaRecepcion().getTime()));
            stmt.setDate(6, new java.sql.Date(producto.getFechaCaducidad().getTime()));
            stmt.setInt(7, producto.getStockMin());
            stmt.setInt(8, producto.getStockMax());
            stmt.setInt(9, producto.getId());

            int filasActualizadas = stmt.executeUpdate();
            if (filasActualizadas == 0) {
                throw new SQLException("No se encontró producto con IdProducto = " + producto.getId());
            }
        }
    }


    public static void insertarProducto(ProductoInventario producto) throws SQLException {
        String sql = "INSERT INTO Inventario (Nombre, Cantidad, Categoria, UnidadMedida, FechaRecepcion, FechaCaducidad, StockMin, StockMax, Estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre());
            stmt.setInt(2, producto.getCantidad());
            stmt.setString(3, producto.getCategoria());
            stmt.setString(4, producto.getUnidadMedida());
            stmt.setDate(5, producto.getFechaRecepcion() != null ? new java.sql.Date(producto.getFechaRecepcion().getTime()) : null);
            stmt.setDate(6, producto.getFechaCaducidad() != null ? new java.sql.Date(producto.getFechaCaducidad().getTime()) : null);
           stmt.setInt(7, producto.getStockMin());
            stmt.setInt(8, producto.getStockMax());
            stmt.setString(9, producto.getEstado()); 

            
            stmt.executeUpdate();
        }
    }

    public static List<ProductoInventario> cargarProductos() throws SQLException {
        List<ProductoInventario> productos = new ArrayList<>();
        String sql = "SELECT * FROM Inventario";
        try (Connection conn = ConexionBD.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ProductoInventario producto = new ProductoInventario(
                        rs.getInt("IdProducto"),
                        rs.getString("Nombre"),
                        rs.getInt("Cantidad"),
                        rs.getString("Categoria"),
                        rs.getString("UnidadMedida"),
                        rs.getDate("FechaRecepcion"), // ✔️ devuelve java.sql.Date, que es compatible con java.util.Date
                        rs.getDate("FechaCaducidad"),
                        rs.getInt("StockMin"),
                        rs.getInt("StockMax")
                        
                );
                productos.add(producto);
            }
        }
        return productos;
    }

    public static List<ProductoInventario> buscarProductos(String textoBusqueda) throws SQLException {
        List<ProductoInventario> productos = new ArrayList<>();
        String sql = "SELECT * FROM Inventario WHERE LOWER(Nombre) LIKE ? OR LOWER(Categoria) LIKE ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String texto = "%" + textoBusqueda.toLowerCase() + "%";
            stmt.setString(1, texto);
            stmt.setString(2, texto);
            try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ProductoInventario producto = new ProductoInventario(
                        rs.getInt("IdProducto"),
                        rs.getString("Nombre"),
                        rs.getInt("Cantidad"),
                        rs.getString("Categoria"),
                        rs.getString("UnidadMedida"),
                        rs.getDate("FechaRecepcion"), // ✔️ devuelve java.sql.Date, que es compatible con java.util.Date
                        rs.getDate("FechaCaducidad"),
                        rs.getInt("StockMin"),
                        rs.getInt("StockMax")
                        
                );
                productos.add(producto);
            }
        }
            }
        return productos;
    }
}
