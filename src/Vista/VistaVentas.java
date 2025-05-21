package Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.border.Border;
public class VistaVentas extends JPanel {
    private JTable tablaVentas;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;
    private JButton btnBuscar, btnReiniciar, btnEliminar, btnExportarCSV, btnExportarXML,btnExportarJSON;
    private JLabel lblResumenVentas;


    public VistaVentas() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Historial de Ventas"));

        // --- Panel superior (título + búsqueda) ---
        JPanel panelSuperior = new JPanel(new BorderLayout());

        // Panel de búsqueda a la izquierda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtBuscar = new JTextField(20);
        btnBuscar = new JButton("Buscar");
        btnReiniciar = new JButton("Reiniciar");

        panelBusqueda.add(new JLabel("Buscar:"));
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(btnReiniciar);

        
        panelSuperior.add(panelBusqueda, BorderLayout.WEST);
        

        add(panelSuperior, BorderLayout.NORTH);

        // --- Tabla de ventas ---
        modeloTabla = new DefaultTableModel(new String[]{"ID Venta", "Pedidos", "Total", "Fecha", "Metodo de Pago"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // ninguna celda será editable
            }
        };
        tablaVentas = new JTable(modeloTabla);
        tablaVentas.setRowHeight(25);
        tablaVentas.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tablaVentas.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        tablaVentas.setFillsViewportHeight(true);
        tablaVentas.setAutoCreateRowSorter(true);

     // Establecer borde más grueso a cada celda
        DefaultTableCellRenderer bordeGruesoRenderer = new DefaultTableCellRenderer() {
        	private final Border bordeGrueso = BorderFactory.createLineBorder(Color.GRAY, 2); 

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                ((JComponent) c).setBorder(bordeGrueso);
                c.setFont(new Font("Segoe UI", Font.PLAIN, 13)); // Fuente más moderna
                return c;
            }
        };

        // Aplicar a todas las columnas
        for (int i = 0; i < tablaVentas.getColumnCount(); i++) {
            tablaVentas.getColumnModel().getColumn(i).setCellRenderer(bordeGruesoRenderer);
        }

        int filaSeleccionada = tablaVentas.getSelectedRow();
        if (filaSeleccionada != -1) {
            int confirmacion = JOptionPane.showConfirmDialog(this, "¿Deseas eliminar esta venta?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                // lógica de eliminación
            }
        }

        JScrollPane scroll = new JScrollPane(tablaVentas);
        add(scroll, BorderLayout.CENTER);

        // --- Panel inferior (botones y resumen) ---
        JPanel panelInferior = new JPanel(new BorderLayout());

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnExportarCSV = new JButton("Exportar a CSV");
        btnEliminar = new JButton("Eliminar Venta");
        btnExportarXML = new JButton("Exportar a XML");
        btnExportarJSON = new JButton("Exportar a JSON");
        
        JPanel panelResumen = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblResumenVentas = new JLabel();
        lblResumenVentas.setFont(new Font("SansSerif", Font.BOLD, 14));
        panelResumen.add(lblResumenVentas);

        panelSuperior.add(panelBusqueda, BorderLayout.WEST);
        panelSuperior.add(panelResumen, BorderLayout.EAST);
        
        //imagenes
        btnEliminar.setIcon(new ImageIcon(getClass().getResource("/Imagenes/eliminar.jpg")));
        btnExportarCSV.setIcon(new ImageIcon(getClass().getResource("/Imagenes/csvicono.png")));
        btnExportarXML.setIcon(new ImageIcon(getClass().getResource("/Imagenes/xmlicono.png")));
        btnExportarJSON.setIcon(new ImageIcon(getClass().getResource("/Imagenes/jsonicono.png")));
        btnBuscar.setIcon(new ImageIcon(getClass().getResource("/Imagenes/lupa.jpg")));
        btnReiniciar.setIcon(new ImageIcon(getClass().getResource("/Imagenes/reiniciar.png")));

        panelBotones.add(btnExportarCSV);
        panelBotones.add(btnExportarXML);
        panelBotones.add(btnExportarJSON);
        panelBotones.add(btnEliminar);

      
        panelInferior.add(panelBotones, BorderLayout.CENTER);
        

        add(panelInferior, BorderLayout.SOUTH);

        // Calcular resumen al inicio
        actualizarResumenVentas();
        EstiloGlobalColor.aplicarEstilo(this);

    }

    // Método para actualizar el resumen de ventas
    public void actualizarResumenVentas() {
        int totalVentas = modeloTabla.getRowCount();
        double montoTotal = 0.0;

        for (int i = 0; i < totalVentas; i++) {
            try {
                Object valor = modeloTabla.getValueAt(i, 2); // Columna "Total"
                if (valor != null) {
                    montoTotal += Double.parseDouble(valor.toString());
                }
            } catch (NumberFormatException e) {
                // Ignorar valores no numéricos
            }
            lblResumenVentas.setText("Total de Ventas: " + totalVentas + " | Monto Total: $" + String.format("%.2f", montoTotal));

        }

    }

    // Getters públicos para usar en el controlador
    public JTable getTablaVentas() { return tablaVentas; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }
    public JTextField getTxtBuscar() { return txtBuscar; }
    public JButton getBtnBuscar() { return btnBuscar; }
    public JButton getBtnReiniciar() { return btnReiniciar; }
    public JButton getBtnEliminar() { return btnEliminar; }

	public JButton getBtnExportarCSV() {
		return btnExportarCSV;
	}

	public void setBtnExportarCSV(JButton btnExportar) {
		this.btnExportarCSV = btnExportar;
	}

	public JButton getBtnExportarXML() {
		return btnExportarXML;
	}

	public void setBtnExportarXML(JButton btnExportarXML) {
		this.btnExportarXML = btnExportarXML;
	}


	public JButton getBtnExportarJSON() {
		return btnExportarJSON;
	}


	public void setBtnExportarJSON(JButton btnExportarJSON) {
		this.btnExportarJSON = btnExportarJSON;
	}
    
}
