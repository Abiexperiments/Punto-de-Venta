package ClasesBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import Modelo.ConexionBD;

public class VentasDAOReportes {
	
	public Map<String, Integer> obtenerVentasPorMetodoPago() throws SQLException {
	    Map<String, Integer> mapa = new HashMap<>();
	    String sql = "SELECT MetodoPago, COUNT(*) as cantidad FROM Ventas GROUP BY MetodoPago";

	    try (Connection conn = ConexionBD.getConexion();
	         PreparedStatement stmt = conn.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {
	        while (rs.next()) {
	            mapa.put(rs.getString("MetodoPago"), rs.getInt("cantidad"));
	        }
	    }
	    return mapa;
	}

	public Map<String, Double> obtenerVentasPorFecha() throws SQLException {
	    Map<String, Double> mapa = new LinkedHashMap<>(); // mantener orden por fecha
	    String sql = "SELECT FechaVenta, SUM(Total) as total FROM Ventas GROUP BY FechaVenta ORDER BY FechaVenta";

	    try (Connection conn = ConexionBD.getConexion();
	         PreparedStatement stmt = conn.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {
	        while (rs.next()) {
	            mapa.put(rs.getString("FechaVenta"), rs.getDouble("Total"));
	        }
	    }
	    return mapa;
	}

	public Map<String, Double> obtenerVentasPorMes() throws SQLException {
	    Map<String, Double> mapa = new LinkedHashMap<>();
	    String sql = "SELECT Year(FechaVenta) AS Año, Month(FechaVenta) AS Mes, SUM(Total) AS total FROM Ventas GROUP BY Year(FechaVenta), Month(FechaVenta) ORDER BY Año, Mes";
	    try (Connection conn = ConexionBD.getConexion();
	         PreparedStatement stmt = conn.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {
	        while (rs.next()) {
	            String clave = rs.getInt("Año") + "-" + String.format("%02d", rs.getInt("Mes"));
	            mapa.put(clave, rs.getDouble("Total"));
	        }
	    }
	    return mapa;
	}

	public Map<String, Double> obtenerVentasPorSemana() throws SQLException {
	    Map<String, Double> mapa = new LinkedHashMap<>();
	    String sql = "SELECT Year(FechaVenta) AS Año, DatePart('ww', FechaVenta, 2) AS Semana, SUM(Total) AS total FROM Ventas GROUP BY Year(FechaVenta), DatePart('ww', FechaVenta, 2) ORDER BY Año, Semana";
	    try (Connection conn = ConexionBD.getConexion();
	         PreparedStatement stmt = conn.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {
	        while (rs.next()) {
	            String clave = rs.getInt("Año") + "-W" + String.format("%02d", rs.getInt("Semana"));
	            mapa.put(clave, rs.getDouble("Total"));
	        }
	    }
	    return mapa;
	}

	public Map<String, Double> obtenerVentasPorAño() throws SQLException {
	    Map<String, Double> mapa = new LinkedHashMap<>();
	    String sql = "SELECT Year(FechaVenta) AS Año, SUM(Total) AS total FROM Ventas GROUP BY Year(FechaVenta) ORDER BY Año";
	    try (Connection conn = ConexionBD.getConexion();
	         PreparedStatement stmt = conn.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {
	        while (rs.next()) {
	            String clave = String.valueOf(rs.getInt("Año"));
	            mapa.put(clave, rs.getDouble("Total"));
	        }
	    }
	    return mapa;
	}

	public double obtenerTotalVentas() throws SQLException {
	    String sql = "SELECT SUM(Total) as total FROM Ventas";
	    try (Connection conn = ConexionBD.getConexion();
	         PreparedStatement stmt = conn.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {
	        if (rs.next()) {
	            return rs.getDouble("total");
	        }
	    }
	    return 0.0;
	}
}