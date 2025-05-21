package Vista;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VistaUsuarios extends JPanel {

	 private JTextField txtUsuario;
	    private JPasswordField txtContraseña;
	    private JComboBox<String> cbRol;

	    private JTextField txtBuscar;
	    private JButton btnAgregar,btnModificar,btnEliminar,btnBuscar,btnReiniciar,btnExportarCSV,btnExportarXML,btnExportarJSON;
	    
	    private JTable tablaUsuarios;
	    private DefaultTableModel modeloTabla;

    public VistaUsuarios() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Gestión de Usuarios"));


        // ---------- Panel del formulario ----------
        JPanel panelFormulario = new JPanel(new GridLayout(3, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        txtUsuario = new JTextField();
        txtContraseña = new JPasswordField();
        cbRol = new JComboBox<>(new String[]{"Administrador", "Cajero","Gerente"});

        panelFormulario.add(new JLabel("Usuario:"));
        panelFormulario.add(txtUsuario);
        panelFormulario.add(new JLabel("Contraseña:"));
        panelFormulario.add(txtContraseña);
        panelFormulario.add(new JLabel("Rol:"));
        panelFormulario.add(cbRol);

     // ---------- Panel búsqueda ----------
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        txtBuscar = new JTextField(20);
        btnBuscar = new JButton("Buscar");
        btnReiniciar = new JButton("Reiniciar");

        panelBusqueda.add(new JLabel("Buscar por usuario:"));
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(btnReiniciar);

        // ---------- Panel combinado superior (formulario + búsqueda) ----------
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelFormulario, BorderLayout.NORTH);
        panelSuperior.add(panelBusqueda, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);

        // ---------- Botones inferiores ----------
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnAgregar = new JButton("Agregar");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");
        btnExportarCSV = new JButton("Generar Reporte a CSV");
        btnExportarXML = new JButton("Generar Reporte a XML");
        btnExportarJSON = new JButton("Generar Reporte a JSON");
        
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

        add(panelBotones, BorderLayout.SOUTH);
       
        // ---------- Tabla de usuarios ----------
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Usuario", "Contraseña", "Rol"}, 0);
        tablaUsuarios = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
        tablaUsuarios.setDefaultEditor(Object.class, null);
        add(scrollPane, BorderLayout.CENTER);
        
        
     // -------- Mejoras visuales a la tabla --------
        tablaUsuarios.setRowHeight(25); // Altura de fila más cómoda
        tablaUsuarios.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tablaUsuarios.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        // Establecer ancho preferido por columna (puedes ajustar a gusto)
        tablaUsuarios.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        tablaUsuarios.getColumnModel().getColumn(1).setPreferredWidth(150); // Usuario
        tablaUsuarios.getColumnModel().getColumn(2).setPreferredWidth(150); // Contraseña
        tablaUsuarios.getColumnModel().getColumn(3).setPreferredWidth(100); // Rol
        
     // Renderizador personalizado para bordes gruesos
        DefaultTableCellRenderer bordeGruesoRenderer = new DefaultTableCellRenderer() {
            private final Border borde = BorderFactory.createLineBorder(Color.GRAY, 2);

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                ((JComponent) c).setBorder(borde);
                return c;
            }
        };

        // Aplicar el renderizador a todas las columnas
        for (int i = 0; i < tablaUsuarios.getColumnCount(); i++) {
            tablaUsuarios.getColumnModel().getColumn(i).setCellRenderer(bordeGruesoRenderer);
        }
        EstiloGlobalColor.aplicarEstilo(this); // si extiende de JPanel
    }

    // Métodos públicos para acceder a los componentes si los necesitas después (getters)
    public JTextField getTxtUsuario() { return txtUsuario; }
    public JPasswordField getTxtContraseña() { return txtContraseña; }
    public JComboBox<String> getCbRol() { return cbRol; }
    public JTextField getTxtBuscar() { return txtBuscar; }
    public JButton getBtnAgregar() { return btnAgregar; }
    public JButton getBtnModificar() { return btnModificar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnBuscar() { return btnBuscar; }
    public JButton getBtnReiniciar() { return btnReiniciar; }
    public JTable getTablaUsuarios() { return tablaUsuarios; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }

	public JButton getBtnExportarCSV() {
		return btnExportarCSV;
	}

	public void setBtnExportarCSV(JButton btnExportarCSV) {
		this.btnExportarCSV = btnExportarCSV;
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
