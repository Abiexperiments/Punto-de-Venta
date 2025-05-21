package ClasesBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import Modelo.ProductosMenu;
import Modelo.ConexionBD;

public class ModeloProductosMenuDAO{

	    public static List<ProductosMenu> obtenerTodosLosProductos() {
	        List<ProductosMenu> productos = new ArrayList<>();
	        String sql = "SELECT Nombre, Precio, Categoria, RutaImagen FROM ProductosMenu";

	        try (Connection conn = ConexionBD.getConexion();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(sql)) {

	            while (rs.next()) {
	                String nombre = rs.getString("Nombre");
	                double precio = rs.getDouble("Precio");
	                String cat = rs.getString("Categoria");
	                String rutaImagen = rs.getString("RutaImagen");

	                productos.add(new ProductosMenu(nombre, precio, cat, rutaImagen));
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return productos;
	    }
	    public static boolean insertarProducto(ProductosMenu producto) {
	        String sql = "INSERT INTO ProductosMenu (Nombre, Precio, Categoria, RutaImagen) VALUES (?, ?, ?, ?)";

	        try (Connection conn = ConexionBD.getConexion();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {

	            stmt.setString(1, producto.getNombre());
	            stmt.setDouble(2, producto.getPrecio());
	            stmt.setString(3, producto.getCategoria());
	            stmt.setString(4, producto.getRutaImagen());

	            int filas = stmt.executeUpdate();
	            return filas > 0;

	        } catch (SQLException e) {
	            JOptionPane.showMessageDialog(null, "Error al insertar el producto en la base de datos:\n" + e.getMessage());
	            return false;
	        }
	    }
	    public static boolean eliminarProductoPorId(int id) {
	        String sql = "DELETE FROM ProductosMenu WHERE Id = ?";

	        try (Connection conn = ConexionBD.getConexion();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {

	            stmt.setInt(1, id);

	            int filas = stmt.executeUpdate();
	            return filas > 0;

	        } catch (SQLException e) {
	            JOptionPane.showMessageDialog(null, "Error al eliminar el producto en la base de datos:\n" + e.getMessage());
	            return false;
	        }
	    }


	public static List<ProductosMenu> obtenerProductosPorCategoria(String categoria) {
		List<ProductosMenu> productos = new ArrayList<>();
        String sql = "SELECT Id, Nombre, Precio, Categoria, RutaImagen FROM ProductosMenu WHERE Categoria = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoria);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
            	int id = rs.getInt("Id");
                String nombre = rs.getString("Nombre");
                double precio = rs.getDouble("Precio");
                String cat = rs.getString("Categoria");
                String rutaImagen = rs.getString("RutaImagen");

                productos.add(new ProductosMenu(id,nombre, precio, cat, rutaImagen));
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Puedes usar un JOptionPane si es una app visual
        }

        return productos;
    }}