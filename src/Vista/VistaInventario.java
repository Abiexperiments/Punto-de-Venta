package Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.toedter.calendar.JDateChooser;

public class VistaInventario extends JPanel {
    private JTextField txtNombre;
    private JTextField txtCantidad;
    private JComboBox<String> cbCategoria;
    private JComboBox<String> cbUnidadMedida;
    private JDateChooser dateChooser;
    private JDateChooser dateCaducidad;
    private JTextField txtStockMinimo;
    private JTextField txtStockMaximo;
    private JTextField txtBuscar;
    private TableRowSorter<TableModel> sorter;

    private JButton btnAgregar;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JButton btnExportarCSV;
    private JButton btnExportarXML;
    private JButton btnExportarJSON;
private JButton btnBuscar;
private JButton btnReiniciar; 
private JButton btnDevolucion;

    private JTable tablaInventario;
    private DefaultTableModel modeloTabla;
	private JLabel lblTotales;
    

    public VistaInventario() {
        setLayout(new BorderLayout(10, 10)); // Espaciado general

        // === FORMULARIO IZQUIERDO ===
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Producto"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNombre = new JTextField(15);
        txtCantidad = new JTextField(15);
        txtStockMinimo = new JTextField(15);
        txtStockMaximo = new JTextField(15);

        String[] categorias = {"Granos", "Lácteos", "Verduras", "Carnes", "Bebidas"};
        cbCategoria = new JComboBox<>(categorias);

        String[] unidades = {"Kg", "g", "L", "ml", "Pza", "Paquete", "Caja", "Botella", "Bolsa"};
        cbUnidadMedida = new JComboBox<>(unidades);

        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd-MM-yyyy");
        
        dateCaducidad = new JDateChooser(); 
        dateCaducidad.setDateFormatString("dd-MM-yyyy");

        int y = 0;
        agregarCampoFormulario(panelFormulario, gbc, y++, "Nombre:", txtNombre);
        agregarCampoFormulario(panelFormulario, gbc, y++, "Cantidad Disponible:", txtCantidad);
        agregarCampoFormulario(panelFormulario, gbc, y++, "Categoría:", cbCategoria);
        agregarCampoFormulario(panelFormulario, gbc, y++, "Unidad de Medida:", cbUnidadMedida);
        agregarCampoFormulario(panelFormulario, gbc, y++, "Fecha de Última Recepción:", dateChooser);
        agregarCampoFormulario(panelFormulario, gbc, y++, "Fecha de Caducidad:", dateCaducidad);
        agregarCampoFormulario(panelFormulario, gbc, y++, "Stock Mínimo:", txtStockMinimo);
        agregarCampoFormulario(panelFormulario, gbc, y++, "Stock Máximo:", txtStockMaximo);
        
        add(panelFormulario, BorderLayout.WEST); 
     // === PANEL DE BÚSQUEDA + TÍTULO ===
        JPanel panelBusqueda = new JPanel(new BorderLayout());
        panelBusqueda.setBorder(BorderFactory.createTitledBorder("Buscar Ingredientes y Productos"));

        JPanel panelIzquierdaBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtBuscar = new JTextField(20);
        btnBuscar = new JButton("Buscar");
        btnReiniciar = new JButton("Reiniciar");
        btnDevolucion = new JButton("Devolución");
        btnDevolucion.setEnabled(false); // Solo se habilita si hay algo seleccionado
        

        panelIzquierdaBusqueda.add(new JLabel("Buscar:"));
        panelIzquierdaBusqueda.add(txtBuscar);
        panelIzquierdaBusqueda.add(btnBuscar);
        panelIzquierdaBusqueda.add(btnReiniciar);
        panelIzquierdaBusqueda.add(btnDevolucion);
        panelBusqueda.add(panelIzquierdaBusqueda, BorderLayout.WEST);

     // Panel derecho para mostrar totales
        lblTotales = new JLabel("Total productos: 0 | Total unidades: 0");
        lblTotales.setFont(new Font("Arial", Font.BOLD, 12));
        lblTotales.setHorizontalAlignment(SwingConstants.RIGHT);
        JPanel panelDerechaTotales = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelDerechaTotales.add(lblTotales);
        panelBusqueda.add(panelDerechaTotales, BorderLayout.EAST);

     // === PANEL SUPERIOR DE SIMBOLOGÍA ===
        JPanel panelSimbolos = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSimbolos.setBorder(BorderFactory.createTitledBorder("Simbología de colores"));

        // Colores para Stock
        panelSimbolos.add(crearCuadroColor(new Color(255, 102, 102))); // Bajo (Rojo)
        panelSimbolos.add(new JLabel("Bajo Stock"));

        panelSimbolos.add(crearCuadroColor(new Color(204, 255, 204))); // En Stock (Verde)
        panelSimbolos.add(new JLabel("Stock Adecuado"));

        panelSimbolos.add(crearCuadroColor(new Color(255, 255, 153))); // Sobre Stock (Amarillo)
        panelSimbolos.add(new JLabel("Sobre Stock"));

        // Separador visual
        panelSimbolos.add(Box.createHorizontalStrut(30)); // Espacio

        // Colores para Caducidad
        panelSimbolos.add(crearCuadroColor(new Color(255, 153, 153))); // Caducado (Rojo claro)
        panelSimbolos.add(new JLabel("Producto Caducado"));

        panelSimbolos.add(crearCuadroColor(new Color(255, 204, 153))); // Casi caduca (Naranja claro)
        panelSimbolos.add(new JLabel("Cercano a caducar"));

        panelSimbolos.add(crearCuadroColor(Color.WHITE));
        panelSimbolos.add(new JLabel("Fecha válida"));
        
                
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelSimbolos, BorderLayout.NORTH);
        panelSuperior.add(panelBusqueda, BorderLayout.SOUTH);
        add(panelSuperior, BorderLayout.NORTH);
        
                // === TABLA CENTRO ===
        modeloTabla = new DefaultTableModel(new Object[]{
        	    "ID", "Nombre", "Cantidad", "Categoria", "Unidad de Medida",
        	    "Fecha Recepción", "Fecha Caducidad", "Stock Mín.", "Stock Máx.", "Estado" 
        	}, 0);
        
tablaInventario = new JTable(modeloTabla) {
    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component c = super.prepareRenderer(renderer, row, column);
        if (c instanceof JComponent) {
            ((JComponent) c).setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Borde visible
        }
        return c;
    }
};
tablaInventario.setRowHeight(25);

        sorter = new TableRowSorter<>(modeloTabla);
        tablaInventario.setRowSorter(sorter);
        
       
        JScrollPane scrollPane = new JScrollPane(tablaInventario);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);
        tablaInventario.setAutoCreateRowSorter(true);
        
        JTableHeader header = tablaInventario.getTableHeader();
        header.setFont(header.getFont().deriveFont(Font.BOLD));
        
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        tablaInventario.getColumnModel().getColumn(9).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if ("Bajo".equals(value)) {
                    c.setBackground(new Color(255, 102, 102)); // rojo suave
                } else if ("En Stock".equals(value)) {
                    c.setBackground(new Color(204, 255, 204)); // verde claro
                } else if ("Sobre Stock".equals(value)) {
                    c.setBackground(new Color(255, 255, 153)); // amarillo claro
                } else {
                    c.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
                }

                return c;
            }
        });
        //fecha caducidad
        tablaInventario.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setForeground(Color.BLACK); // Siempre texto negro

                LocalDate hoy = LocalDate.now();
                LocalDate fechaCaducidad = null;

                try {
                    if (value instanceof java.util.Date) {
                        fechaCaducidad = ((java.util.Date) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    } else if (value instanceof LocalDate) {
                        fechaCaducidad = (LocalDate) value;
                    } else if (value instanceof String) {
                        // Intentar parsear como fecha
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        fechaCaducidad = LocalDate.parse((String) value, formatter);
                    }

                    if (fechaCaducidad != null) {
                        if (fechaCaducidad.isBefore(hoy)) {
                            c.setBackground(new Color(255, 153, 153)); // Rojo claro: caducado
                        } else if (!fechaCaducidad.isAfter(hoy.plusDays(3))) {
                            c.setBackground(new Color(255, 204, 153)); // Naranja claro: casi caduca
                        } else {
                            c.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE); // OK
                        }
                    } else {
                        c.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE); // Sin fecha
                    }
                } catch (Exception ex) {
                    c.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE); // Error al parsear
                }
                return c;
            }
        });
        
        tablaInventario.getSelectionModel().addListSelectionListener(e -> {
            boolean seleccionado = tablaInventario.getSelectedRow() != -1;
            btnDevolucion.setEnabled(seleccionado);
        });
        
        // === PANEL INFERIOR: BOTONES + TOTALES ===
        JPanel panelInferior = new JPanel(new BorderLayout());

        // Botones alineados a la izquierda
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnAgregar = new JButton("Agregar");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");
        btnExportarCSV = new JButton("Exportar Reporte a CSV");
        btnExportarXML = new JButton("Exportar Reporte a XML");
        btnExportarJSON = new JButton("Exportar Reporte a JSON");
        
        //imagenes 
        btnAgregar.setIcon(new ImageIcon(getClass().getResource("/Imagenes/agregar.jpg")));
        btnModificar.setIcon(new ImageIcon(getClass().getResource("/Imagenes/modificar.jpg")));
        btnEliminar.setIcon(new ImageIcon(getClass().getResource("/Imagenes/eliminar.jpg")));
        btnExportarCSV.setIcon(new ImageIcon(getClass().getResource("/Imagenes/csvicono.png")));
        btnExportarXML.setIcon(new ImageIcon(getClass().getResource("/Imagenes/xmlicono.png")));
        btnExportarJSON.setIcon(new ImageIcon(getClass().getResource("/Imagenes/jsonicono.png")));
        btnBuscar.setIcon(new ImageIcon(getClass().getResource("/Imagenes/lupa.jpg")));
        btnReiniciar.setIcon(new ImageIcon(getClass().getResource("/Imagenes/reiniciar.png")));
        
        
        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnExportarCSV);
        panelBotones.add(btnExportarXML);
        panelBotones.add(btnExportarJSON);
        panelInferior.add(panelBotones, BorderLayout.WEST);

       
        add(panelInferior, BorderLayout.SOUTH);
        EstiloGlobalColor.aplicarEstilo(this); // si extiende de JPanel

    }

    private void agregarCampoFormulario(JPanel panel, GridBagConstraints gbc, int fila, String etiqueta, Component campo) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel(etiqueta), gbc);
        gbc.gridx = 1;
        panel.add(campo, gbc);
        
    }
    private JPanel crearCuadroColor(Color color) {
        JPanel panelColor = new JPanel();
        panelColor.setBackground(color);
        panelColor.setPreferredSize(new Dimension(20, 20));
        panelColor.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return panelColor;
    }
    
    // Getters
    public JTextField getTxtNombre() { return txtNombre; }
    public JTextField getTxtCantidad() { return txtCantidad; }
    public JComboBox<String> getCbUnidadMedida() { return cbUnidadMedida; }
    public JComboBox<String> getCbCategoria() { return cbCategoria; }
    public JDateChooser getDateChooser() { return dateChooser; }
    public JDateChooser getDateCaducidad() { return dateCaducidad; }
    public JTextField getTxtStockMinimo() { return txtStockMinimo; }
    public JTextField getTxtStockMaximo() { return txtStockMaximo; }
    public JTextField getTxtBuscar() {return txtBuscar;}
	public JButton getBtnAgregar() { return btnAgregar; }
    public JButton getBtnModificar() { return btnModificar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnExportarCSV() {return btnExportarCSV;}
    public JButton getBtnDevolucion() {
        return btnDevolucion;
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

	public void setBtnExportarCSV(JButton btnExportarCSV) {
		this.btnExportarCSV = btnExportarCSV;
	}

	public JButton getBtnBuscar() {return btnBuscar;}
    public JButton getBtnReiniciar() { return btnReiniciar; }
	public JTable getTablaInventario() { return tablaInventario; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }
    public TableRowSorter<TableModel> getTableRowSorter() {return sorter;}
    public boolean isCellEditable(int row, int column) {return false; // Ninguna celda editable
    }
    public void actualizarTotales() {
        int totalFilas = modeloTabla.getRowCount();
        int totalUnidades = 0;

        for (int i = 0; i < totalFilas; i++) {
            Object valor = modeloTabla.getValueAt(i, 2); // Columna de cantidad
            if (valor != null) {
                try {
                    int cantidad = Integer.parseInt(valor.toString());
                    totalUnidades += cantidad;
                } catch (NumberFormatException e) {
                    // ignorar errores de formato
                }
            }
        }

        lblTotales.setText("Total productos: " + totalFilas + " | Total unidades: " + totalUnidades);
    }
};