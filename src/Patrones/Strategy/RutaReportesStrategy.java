package Patrones.Strategy;

public interface RutaReportesStrategy {
    boolean aplica(String tipo, String formato);
    String obtenerCarpeta(String tipo, String formato);
}
