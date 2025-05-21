package Patrones.Strategy;

public class DefaultStrategy implements RutaReportesStrategy {
    @Override
    public boolean aplica(String tipo, String formato) {
        return true; // Siempre aplica si no coincidió otro
    }

    @Override
    public String obtenerCarpeta(String tipo, String formato) {
        return "Reportes " + tipo + " " + formato;
    }
}
