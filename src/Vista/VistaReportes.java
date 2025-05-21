package Vista;

import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.ui.RectangleEdge;

import ClasesBD.VentasDAOReportes;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Map;

public class VistaReportes extends JPanel {
    private JLabel lblTotalVentas;
// agregar botones para imprimir cada uno de los reportes
    
    public VistaReportes(VentasDAOReportes ventasDAO) throws SQLException {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Reportes de Ventas"));

        // Establecer un ChartTheme personalizado
        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());

        // Panel con pestañas
        JTabbedPane pestañas = new JTabbedPane();

        // Gráfico de pastel: Métodos de pago
        JFreeChart graficoPastel = crearGraficoPastel(ventasDAO.obtenerVentasPorMetodoPago());
        ChartPanel panelPie = new ChartPanel(graficoPastel);
        panelPie.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // padding
        pestañas.addTab("Métodos de Pago", null, panelPie, "Distribución de ventas por forma de pago");

        // Gráfico de barras: Ventas por fecha
        JFreeChart graficoBarras = crearGraficoBarras(ventasDAO.obtenerVentasPorFecha());
        ChartPanel panelBarras = new ChartPanel(graficoBarras);
        panelBarras.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // padding
        pestañas.addTab("Ventas por Día", null, panelBarras, "Ventas totales por fecha");

     // Gráfico de barras: Ventas por Mes
     JFreeChart graficoMes = crearGraficoBarras(ventasDAO.obtenerVentasPorMes());
     ChartPanel panelMes = new ChartPanel(graficoMes);
     panelMes.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
     pestañas.addTab("Ventas por Mes", null, panelMes, "Ventas mensuales agrupadas");

     // Gráfico de barras: Ventas por Semana
     JFreeChart graficoSemana = crearGraficoBarras(ventasDAO.obtenerVentasPorSemana());
     ChartPanel panelSemana = new ChartPanel(graficoSemana);
     panelSemana.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
     pestañas.addTab("Ventas por Semana", null, panelSemana, "Ventas semanales agrupadas");

     // Gráfico de barras: Ventas por Año
     JFreeChart graficoAnio = crearGraficoBarras(ventasDAO.obtenerVentasPorAño());
     ChartPanel panelAnio = new ChartPanel(graficoAnio);
     panelAnio.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
     pestañas.addTab("Ventas por Año", null, panelAnio, "Ventas anuales agrupadas");

     // Tooltips
        pestañas.setToolTipTextAt(0, "Gráfica de pastel por método de pago");
        pestañas.setToolTipTextAt(1, "Ventas diarias en formato de barras");
        pestañas.setToolTipTextAt(2, "Ventas mensuales agrupadas");
        pestañas.setToolTipTextAt(3, "Ventas semanales agrupadas");
        pestañas.setToolTipTextAt(4, "Ventas anuales agrupadas");

        add(pestañas, BorderLayout.CENTER);

        // Etiqueta de total de ventas
        lblTotalVentas = new JLabel("Total de ventas: $0.00");
        lblTotalVentas.setHorizontalAlignment(SwingConstants.CENTER);
        lblTotalVentas.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblTotalVentas, BorderLayout.SOUTH);

        // Mostrar valor real
        double total = ventasDAO.obtenerTotalVentas();
        lblTotalVentas.setText(String.format("Total de ventas: $%.2f", total));
        EstiloGlobalColor.aplicarEstilo(this); // si extiende de JPanel
    }

    private JFreeChart crearGraficoPastel(Map<String, Integer> datos) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<String, Integer> entry : datos.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Distribución por Método de Pago",
                dataset,
                true,
                true,
                false
        );

        // Mejoras visuales
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint("Efectivo", new Color(144, 238, 144)); // verde claro
        plot.setSectionPaint("Tarjeta", new Color(100, 149, 237)); // azul
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        chart.getTitle().setFont(new Font("Arial", Font.BOLD, 16));
        chart.getLegend().setPosition(RectangleEdge.RIGHT);

        return chart;
    }

    private JFreeChart crearGraficoBarras(Map<String, Double> datos) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Double> entry : datos.entrySet()) {
            dataset.addValue(entry.getValue(), "Ventas", entry.getKey());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Ventas por Fecha",
                "Fecha",
                "Monto ($)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Mejoras visuales
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(255, 153, 51)); // naranja

        chart.getTitle().setFont(new Font("Arial", Font.BOLD, 16));
        chart.getLegend().setPosition(RectangleEdge.RIGHT);
        
        LegendTitle legend = chart.getLegend();
        legend.setItemFont(new Font("Arial", Font.PLAIN, 12));

        return chart;
    }
}