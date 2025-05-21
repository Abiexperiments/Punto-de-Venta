package Modelo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
	public static Connection getConexion() {
        Connection conexion = null;
        try {
            // Cargar el driver UCanAccess explícitamente
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

            String rutaBD = "C:\\Users\\abiga\\OneDrive\\Documentos\\DB Punto de Venta.accdb";
            String url = "jdbc:ucanaccess://" + rutaBD;

            conexion = DriverManager.getConnection(url);
            System.out.println("Conexión exitosa a Access.");
        } catch (ClassNotFoundException e) {
            System.out.println("Error: No se encontró el driver UCanAccess.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
            e.printStackTrace();
        }

        return conexion;
    }
}