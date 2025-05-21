package Patrones.Strategy;

public class EstrategiaFormatoPDF implements RutaReportesStrategy {
    @Override
    public boolean aplica(String tipo, String formato) {
        return formato.equalsIgnoreCase("pdf");
    }

    @Override
    public String obtenerCarpeta(String tipo, String formato) {
        // Capitaliza primera letra de tipo y formato, y los junta
        String tipoCapitalizado = capitalizar(tipo);
        String formatoCapitalizado = formato.toUpperCase(); // o capitalizar(formato) si prefieres "Pdf"

        return tipoCapitalizado + formatoCapitalizado; // Ej: "PedidosPDF"
    }

    private String capitalizar(String texto) {
        if (texto == null || texto.isEmpty()) return texto;
        return texto.substring(0, 1).toUpperCase() + texto.substring(1).toLowerCase();
    }
}