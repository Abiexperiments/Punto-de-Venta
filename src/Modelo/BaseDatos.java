package Modelo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

public class BaseDatos {
	 public void eliminarProductoInventario(int id) throws SQLException {
	        String sql = "DELETE FROM Inventario WHERE IdProducto = ?";
	        try (Connection conn = ConexionBD.obtenerConexion();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setInt(1, id);
	            stmt.executeUpdate();
	        }
	    }

	    public void modificarProductoInventario(ProductoInventario producto) throws SQLException {
	        if (producto == null) {
	            throw new IllegalArgumentException("El producto no puede ser null.");
	        }
	        if (producto.getFechaRecepcion() == null || producto.getFechaCaducidad() == null) {
	            throw new IllegalArgumentException("Las fechas de recepción y caducidad no pueden ser null.");
	        }

	        String sql = "UPDATE Inventario SET Nombre=?, Cantidad=?, Categoria=?, UnidadMedida=?, FechaRecepcion=?, FechaCaducidad=?, StockMin=?, StockMax=? WHERE IdProducto=?";
	        try (Connection conn = ConexionBD.obtenerConexion();
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


	    public static void insertarProductoInventario(ProductoInventario producto) throws SQLException {
	        String sql = "INSERT INTO Inventario (Nombre, Cantidad, Categoria, UnidadMedida, FechaRecepcion, FechaCaducidad, StockMin, StockMax, Estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	        try (Connection conn = ConexionBD.obtenerConexion();
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
	    public static List<ProductoInventario> cargarProductosInventario() throws SQLException {
	        List<ProductoInventario> productos = new ArrayList<>();
	        String sql = "SELECT * FROM Inventario";
	        try (Connection conn = ConexionBD.obtenerConexion();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(sql)) {
	            while (rs.next()) {
	                ProductoInventario producto = new ProductoInventario(
	                        rs.getInt("IdProducto"),
	                        rs.getString("Nombre"),
	                        rs.getInt("Cantidad"),
	                        rs.getString("Categoria"),
	                        rs.getString("UnidadMedida"),
	                        rs.getDate("FechaRecepcion"), // devuelve java.sql.Date, que es compatible con java.util.Date
	                        rs.getDate("FechaCaducidad"),
	                        rs.getInt("StockMin"),
	                        rs.getInt("StockMax")
	                );
	                productos.add(producto);
	            }
	        }
	        return productos;
	    }

	    public static List<ProductoInventario> buscarProductosInventario(String textoBusqueda) throws SQLException {
	        List<ProductoInventario> productos = new ArrayList<>();
	        String sql = "SELECT * FROM Inventario WHERE LOWER(Nombre) LIKE ? OR LOWER(Categoria) LIKE ?";
	        try (Connection conn = ConexionBD.obtenerConexion();
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
	                productos.add(producto);}}}
	        return productos;
	    }//menu
	    public static List<ProductosMenu> obtenerTodosLosProductosMenu() {
	        List<ProductosMenu> productos = new ArrayList<>();
	        String sql = "SELECT Nombre, Precio, Categoria, RutaImagen FROM ProductosMenu";

	        try (Connection conn = ConexionBD.obtenerConexion();
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
	    public static boolean insertarProductoMenu(ProductosMenu producto) {
	        String sql = "INSERT INTO ProductosMenu (Nombre, Precio, Categoria, RutaImagen) VALUES (?, ?, ?, ?)";

	        try (Connection conn = ConexionBD.obtenerConexion();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {

	            stmt.setString(1, producto.getNombre());
	            stmt.setDouble(2, producto.getPrecio());
	            stmt.setString(3, producto.getCategoria());
	            stmt.setString(4, producto.getRutaImagen());

	            int filas = stmt.executeUpdate();
	            return filas > 0;
	        } catch (SQLException e) {
	            JOptionPane.showMessageDialog(null, "Error al insertar el producto en la base de datos:\n" + e.getMessage());
	            return false;}}
	    
	    public static boolean eliminarProductoPorIdMenu(int id) {
	        String sql = "DELETE FROM ProductosMenu WHERE Id = ?";

	        try (Connection conn = ConexionBD.obtenerConexion();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {

	            stmt.setInt(1, id);

	            int filas = stmt.executeUpdate();
	            return filas > 0;

	        } catch (SQLException e) {
	            JOptionPane.showMessageDialog(null, "Error al eliminar el producto en la base de datos:\n" + e.getMessage());
	            return false;
	        }
	    }
	public static List<ProductosMenu> obtenerProductosPorCategoriaMenu(String categoria) {
		List<ProductosMenu> productos = new ArrayList<>();
        String sql = "SELECT Id, Nombre, Precio, Categoria, RutaImagen FROM ProductosMenu WHERE Categoria = ?";

        try (Connection conn = ConexionBD.obtenerConexion();
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
            e.printStackTrace(); 
        }
        return productos;}
	//usuario
	 public Usuario verificarUsuario(String usuario, String contraseña) {
	        try {
	            Connection con = ConexionBD.obtenerConexion();
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
	        try (Connection con = ConexionBD.obtenerConexion();
	             PreparedStatement stmt = con.prepareStatement(sql)) {

	            stmt.setString(1, nombre);
	            stmt.setString(2, contraseña);
	            stmt.setString(3, rol);
	            stmt.executeUpdate();
	            return true;

	        } catch (SQLException e) {
	            System.out.println("Error al agregar usuario: " + e.getMessage());
	            return false;}}
	    
	    public static List<Object[]> obtenerUsuarios() {
	        List<Object[]> lista = new ArrayList<>();
	        String sql = "SELECT * FROM usuarios";

	        try (Connection con = ConexionBD.obtenerConexion();
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
	        try (Connection con = ConexionBD.obtenerConexion();
	             PreparedStatement stmt = con.prepareStatement(sql)) {

	            stmt.setString(1, nombre);
	            stmt.setString(2, contraseña);
	            stmt.setString(3, rol);
	            stmt.setInt(4, id);
	            stmt.executeUpdate();
	            return true;

	        } catch (SQLException e) {
	            System.out.println("Error al modificar usuario: " + e.getMessage());
	            return false;}}
	    
	    public static boolean eliminarUsuario(int id) {
	        String sql = "DELETE FROM usuarios WHERE id = ?";
	        try (Connection con = ConexionBD.obtenerConexion();
	             PreparedStatement stmt = con.prepareStatement(sql)) {

	            stmt.setInt(1, id);
	            stmt.executeUpdate();
	            return true;

	        } catch (SQLException e) {
	            System.out.println("Error al eliminar usuario: " + e.getMessage());
	            return false;}}
	    //proveedores
	    public void guardarProveedores(Proveedor proveedor) {
	        String sql = "INSERT INTO Proveedores (ID, Nombre, Telefono, Correo, Calle, Colonia, Ciudad, Estado, CP, TipoProducto) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	        try (Connection conexion = ConexionBD.obtenerConexion();
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

	    public void actualizarProveedores(Proveedor proveedor) {
	        String sql = "UPDATE Proveedores SET Nombre=?, Telefono=?, Correo=?, Calle=?, Colonia=?, Ciudad=?, Estado=?, CP=?, TipoProducto=? WHERE ID=?";

	        try (Connection conexion = ConexionBD.obtenerConexion();
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

	    public void eliminarProveedores(int id) {
	        String sql = "DELETE FROM Proveedores WHERE ID = ?";

	        try (Connection conexion = ConexionBD.obtenerConexion();
	             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

	            pstmt.setInt(1, id);
	            pstmt.executeUpdate();

	        } catch (SQLException e) {
	            JOptionPane.showMessageDialog(null, "Error al eliminar proveedor: " + e.getMessage());
	        }
	    }

	    public List<Proveedor> obtenerTodosProveedores() {
	        List<Proveedor> proveedores = new ArrayList<>();
	        String sql = "SELECT * FROM Proveedores";

	        try (Connection conexion = ConexionBD.obtenerConexion();
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


	    public List<Proveedor> buscarProveedores(String texto) {
	        List<Proveedor> resultados = new ArrayList<>();
	        String sql = "SELECT * FROM Proveedores WHERE " +
	                     "LOWER(Nombre) LIKE ? OR LOWER(Colonia) LIKE ? OR LOWER(Calle) LIKE ? OR LOWER(Estado) LIKE ? OR " +
	                     "LOWER(Correo) LIKE ? OR LOWER(TipoProducto) LIKE ? OR LOWER(Telefono) LIKE ? OR CAST(ID AS TEXT) LIKE ? OR CAST(CP AS TEXT) LIKE ?";

	        try (Connection conexion = ConexionBD.obtenerConexion();
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
	        }return resultados;}
	    //ventas 
	    public static List<Venta> obtenerVentas() {
	        List<Venta> ventas = new ArrayList<>();

	        String sql = "SELECT * FROM Ventas";

	        try (Connection conn = ConexionBD.obtenerConexion();
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

	        try (Connection conn = ConexionBD.obtenerConexion();
	             PreparedStatement pstmt = conn.prepareStatement(sql)) {

	            pstmt.setInt(1, id);
	            pstmt.executeUpdate();

	        } catch (SQLException e) {
	            JOptionPane.showMessageDialog(null, "Error al eliminar venta: " + e.getMessage());
	        }
	    }

	    public static void agregarVenta(Venta venta) {
	        String sql = "INSERT INTO Ventas (Productos, Total, FechaVenta, MetodoPago) VALUES (?, ?, ?, ?)";

	        try (Connection conn = ConexionBD.obtenerConexion();
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

	        try (Connection conn = ConexionBD.obtenerConexion();
	             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

	            pstmt.setString(1, String.join(",", venta.getProductos()));
	            pstmt.setDouble(2, venta.getTotal());
	            pstmt.setTimestamp(3, new java.sql.Timestamp(venta.getFecha().getTime()));
	            pstmt.setString(4, venta.getMetodoPago());

	            int filas = pstmt.executeUpdate();
	            if (filas > 0) {
	                ResultSet rs = pstmt.getGeneratedKeys();
	                if (rs.next()) {
	                    idVenta = rs.getInt(1);}}
	        } catch (SQLException e) {
	            JOptionPane.showMessageDialog(null, "Error al agregar venta y obtener ID: " + e.getMessage());
	        }return idVenta;}
	    //graficas de reporte
	    public Map<String, Integer> obtenerVentasPorMetodoPagoGrafica() throws SQLException {
		    Map<String, Integer> mapa = new HashMap<>();
		    String sql = "SELECT MetodoPago, COUNT(*) as cantidad FROM Ventas GROUP BY MetodoPago";

		    try (Connection conn = ConexionBD.obtenerConexion();
		         PreparedStatement stmt = conn.prepareStatement(sql);
		         ResultSet rs = stmt.executeQuery()) {
		        while (rs.next()) {
		            mapa.put(rs.getString("MetodoPago"), rs.getInt("cantidad"));
		        }
		    }
		    return mapa;
		}

		public Map<String, Double> obtenerVentasPorFechaGrafica() throws SQLException {
		    Map<String, Double> mapa = new LinkedHashMap<>(); // mantener orden por fecha
		    String sql = "SELECT FechaVenta, SUM(Total) as total FROM Ventas GROUP BY FechaVenta ORDER BY FechaVenta";

		    try (Connection conn = ConexionBD.obtenerConexion();
		         PreparedStatement stmt = conn.prepareStatement(sql);
		         ResultSet rs = stmt.executeQuery()) {
		        while (rs.next()) {
		            mapa.put(rs.getString("FechaVenta"), rs.getDouble("Total"));
		        }
		    }
		    return mapa;
		}

		public Map<String, Double> obtenerVentasPorMesGrafica() throws SQLException {
		    Map<String, Double> mapa = new LinkedHashMap<>();
		    String sql = "SELECT Year(FechaVenta) AS Año, Month(FechaVenta) AS Mes, SUM(Total) AS total FROM Ventas GROUP BY Year(FechaVenta), Month(FechaVenta) ORDER BY Año, Mes";
		    try (Connection conn = ConexionBD.obtenerConexion();
		         PreparedStatement stmt = conn.prepareStatement(sql);
		         ResultSet rs = stmt.executeQuery()) {
		        while (rs.next()) {
		            String clave = rs.getInt("Año") + "-" + String.format("%02d", rs.getInt("Mes"));
		            mapa.put(clave, rs.getDouble("Total"));
		        }
		    }
		    return mapa;
		}

		public Map<String, Double> obtenerVentasPorSemanaGrafica() throws SQLException {
		    Map<String, Double> mapa = new LinkedHashMap<>();
		    String sql = "SELECT Year(FechaVenta) AS Año, DatePart('ww', FechaVenta, 2) AS Semana, SUM(Total) AS total FROM Ventas GROUP BY Year(FechaVenta), DatePart('ww', FechaVenta, 2) ORDER BY Año, Semana";
		    try (Connection conn = ConexionBD.obtenerConexion();
		         PreparedStatement stmt = conn.prepareStatement(sql);
		         ResultSet rs = stmt.executeQuery()) {
		        while (rs.next()) {
		            String clave = rs.getInt("Año") + "-W" + String.format("%02d", rs.getInt("Semana"));
		            mapa.put(clave, rs.getDouble("Total"));
		        }
		    }
		    return mapa;
		}

		public Map<String, Double> obtenerVentasPorAñoGrafica() throws SQLException {
		    Map<String, Double> mapa = new LinkedHashMap<>();
		    String sql = "SELECT Year(FechaVenta) AS Año, SUM(Total) AS total FROM Ventas GROUP BY Year(FechaVenta) ORDER BY Año";
		    try (Connection conn = ConexionBD.obtenerConexion();
		         PreparedStatement stmt = conn.prepareStatement(sql);
		         ResultSet rs = stmt.executeQuery()) {
		        while (rs.next()) {
		            String clave = String.valueOf(rs.getInt("Año"));
		            mapa.put(clave, rs.getDouble("Total"));
		        }}return mapa;}

		public double obtenerTotalVentasGrafica() throws SQLException {
		    String sql = "SELECT SUM(Total) as total FROM Ventas";
		    try (Connection conn = ConexionBD.obtenerConexion();
		         PreparedStatement stmt = conn.prepareStatement(sql);
		         ResultSet rs = stmt.executeQuery()) {
		        if (rs.next()) {
		            return rs.getDouble("total");
		        }}return 0.0;}
}
