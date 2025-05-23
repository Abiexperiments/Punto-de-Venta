package Modelo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL_ACCESS = "jdbc:ucanaccess://";

    public static Connection conectarUCanAccess(String rutaBD) throws SQLException {
        String cadenaConexion = URL_ACCESS + rutaBD;
        System.out.println("Cadena de conexión: " + cadenaConexion);
        return DriverManager.getConnection(cadenaConexion);
    }

    public static Connection obtenerConexion() {
        Connection conexion = null;
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

            String ruta = "C:\\Users\\abiga\\OneDrive\\Documentos\\DB Punto de Venta.accdb";
            conexion = conectarUCanAccess(ruta);

            System.out.println("Conexión exitosa a Access.");
        } catch (ClassNotFoundException e) {
            System.out.println("Error: No se encontró el driver UCanAccess.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
            e.printStackTrace();
        }

        return conexion;}}