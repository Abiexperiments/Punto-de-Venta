package Vista;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Modelo.Proveedor;

import java.awt.*;

public class VistaProveedores extends JPanel {

    // Formulario
    public JTextField txtNombre, txtTelefono, txtCorreo, txtCalle,txtColonia, txtCiudad, txtEstado, txtCP;
    public JComboBox<String> comboTipoProducto;
    
    // Búsqueda
    public JTextField txtBuscar;
    public JComboBox<String> comboFiltroTipoProducto;
    public JButton btnBuscar;

    // Tabla
    public JTable tablaProveedores;
    public DefaultTableModel modeloTabla;

    // Botones
    public JButton btnAgregar, btnModificar, btnEliminar, btnExportarCSV,btnReiniciar,btnExportarXML,btnExportarJSON;

    public VistaProveedores() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // === Panel Formulario ===
        JPanel panelFormulario = new JPanel(new GridLayout(1, 2, 10, 0));
        JPanel panelIzquierdo = new JPanel(new GridLayout(5, 2, 5, 5));
        JPanel panelDerecho = new JPanel(new GridLayout(5, 2, 5, 5));

        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del proveedor"));

        txtNombre = new JTextField();
        txtTelefono = new JTextField();
        txtCorreo = new JTextField();
        txtCalle = new JTextField();
        txtColonia = new JTextField();
        txtCiudad = new JTextField();
        txtEstado = new JTextField();
        txtCP = new JTextField();
     // Panel izquierdo
        panelIzquierdo.add(new JLabel("Nombre:"));
        panelIzquierdo.add(txtNombre);
        panelIzquierdo.add(new JLabel("Teléfono:"));
        panelIzquierdo.add(txtTelefono);
        panelIzquierdo.add(new JLabel("Correo electrónico:"));
        panelIzquierdo.add(txtCorreo);
        panelIzquierdo.add(new JLabel("Tipo de producto:"));
        comboTipoProducto = new JComboBox<>(new String[]{"Abarrotes", "Bebidas", "Lácteos", "Verduras", "Carnes", "Otro"});
        panelIzquierdo.add(comboTipoProducto);
        panelIzquierdo.add(new JLabel("")); // Espacio vacío si lo necesitas
        panelIzquierdo.add(new JLabel("")); // Otro espacio

        // Panel derecho
        panelDerecho.add(new JLabel("Calle:"));
        panelDerecho.add(txtCalle);
        panelDerecho.add(new JLabel("Colonia:"));
        panelDerecho.add(txtColonia);
        panelDerecho.add(new JLabel("Ciudad:"));
        panelDerecho.add(txtCiudad);
        panelDerecho.add(new JLabel("Estado:"));
        panelDerecho.add(txtEstado);
        panelDerecho.add(new JLabel("Código Postal:"));
        panelDerecho.add(txtCP);
        
        panelFormulario.add(panelIzquierdo);
        panelFormulario.add(panelDerecho);


        // === Panel búsqueda ===
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        txtBuscar = new JTextField(15);
        comboFiltroTipoProducto = new JComboBox<>(new String[]{"Todos", "Abarrotes", "Bebidas", "Lácteos", "Verduras", "Carnes", "Otro"});
        btnReiniciar = new JButton("Reiniciar");


        panelBusqueda.add(new JLabel("Buscar:"));
        panelBusqueda.add(txtBuscar);
        btnBuscar = new JButton("Buscar");
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(new JLabel("Reiniciar busqueda:"));
        panelBusqueda.add(btnReiniciar);
        panelBusqueda.add(new JLabel("Filtrar por tipo:"));
        panelBusqueda.add(comboFiltroTipoProducto);

        // === Panel superior combinado ===
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelFormulario, BorderLayout.CENTER);
        panelSuperior.add(panelBusqueda, BorderLayout.SOUTH);
        
        panelSuperior.add(new JSeparator(), BorderLayout.NORTH);


        // === Tabla de proveedores ===
        modeloTabla = new DefaultTableModel(null, new String[]{
        	    "ID", "Nombre", "Teléfono", "Correo", "Calle", 
        	    "Colonia", "Ciudad", "Estado", "Código Postal", "Tipo de Producto"
        	})  {
        	 public boolean isCellEditable(int row, int column) {
        	        return false; // Ahora ninguna celda editable
        	    }
        	};

        tablaProveedores = new JTable(modeloTabla);
        tablaProveedores.setRowHeight(25);
        tablaProveedores.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaProveedores.getColumnModel().getColumn(0).setMaxWidth(60);
        tablaProveedores.getColumnModel().getColumn(0).setMinWidth(40);
        JScrollPane scrollTabla = new JScrollPane(tablaProveedores);
        
     // Personalización de celdas para bordes más visibles
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            private final Border border = BorderFactory.createLineBorder(Color.GRAY, 2); // Borde más grueso

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
                
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                ((JComponent) c).setBorder(border);
                c.setFont(new Font("Segoe UI", Font.PLAIN, 13)); // Fuente más moderna
                return c;
            }
        };

        for (int i = 0; i < tablaProveedores.getColumnCount(); i++) {
            tablaProveedores.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        // Aumentar tamaño de fila
        tablaProveedores.setRowHeight(30);

        
        
        // === Botones de acción ===
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnAgregar = new JButton("Agregar proveedor");
        btnModificar = new JButton("Modificar proveedor");
        btnEliminar = new JButton("Eliminar proveedor");
        btnExportarCSV = new JButton("Generar Reporte a CSV");
        btnExportarXML = new JButton("Generar Reporte a XML");
        btnExportarJSON = new JButton("Generar Reporte a JSON");
        
        //tooltip cursor
        btnAgregar.setToolTipText("Agregar un nuevo proveedor a la lista");
        txtCorreo.setToolTipText("Ej. proveedor@email.com");
        
//imagenes
        btnAgregar.setIcon(new ImageIcon(getClass().getResource("/Imagenes/agregar.jpg")));
        btnModificar.setIcon(new ImageIcon(getClass().getResource("/Imagenes/modificar.jpg")));
        btnEliminar.setIcon(new ImageIcon(getClass().getResource("/Imagenes/eliminar.jpg")));
        btnExportarCSV.setIcon(new ImageIcon(getClass().getResource("/Imagenes/csvicono.png")));
        btnExportarXML.setIcon(new ImageIcon(getClass().getResource("/Imagenes/xmlicono.png")));
        btnExportarJSON.setIcon(new ImageIcon(getClass().getResource("/Imagenes/jsonicono.png")));
        btnBuscar.setIcon(new ImageIcon(getClass().getResource("/Imagenes/lupa.jpg")));
        btnBuscar.setIcon(new ImageIcon(getClass().getResource("/Imagenes/reiniciar.png")));
        
        
        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnExportarCSV);
        panelBotones.add(btnExportarXML);
        panelBotones.add(btnExportarJSON);
        

        // === Agregar al panel principal ===
        add(panelSuperior, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        
        EstiloGlobalColor.aplicarEstilo(this); // si extiende de JPanel

    }
	public JButton getBtnReiniciar() {return btnReiniciar;}
	public void setBtnReiniciar(JButton btnReiniciar) {this.btnReiniciar = btnReiniciar;}
	public JTextField getTxtNombre() {return txtNombre;}
	public void setTxtNombre(JTextField txtNombre) {this.txtNombre = txtNombre;}
	public JTextField getTxtTelefono() {return txtTelefono;}
	public void setTxtTelefono(JTextField txtTelefono) {this.txtTelefono = txtTelefono;}
	public JTextField getTxtCorreo() {
		return txtCorreo;
	}
	public void setTxtCorreo(JTextField txtCorreo) {
		this.txtCorreo = txtCorreo;
	}
	public JTextField getTxtCalle() {
		return txtCalle;
	}
	public void setTxtCalle(JTextField txtCalle) {
		this.txtCalle = txtCalle;
	}
	public JTextField getTxtColonia() {
		return txtColonia;
	}
	public void setTxtColonia(JTextField txtColonia) {
		this.txtColonia = txtColonia;
	}
	public JTextField getTxtCiudad() {
		return txtCiudad;
	}
	public void setTxtCiudad(JTextField txtCiudad) {
		this.txtCiudad = txtCiudad;
	}
	public JTextField getTxtEstado() {
		return txtEstado;
	}
	public void setTxtEstado(JTextField txtEstado) {
		this.txtEstado = txtEstado;
	}
	public JTextField getTxtCP() {
		return txtCP;
	}
	public void setTxtCP(JTextField txtCP) {
		this.txtCP = txtCP;
	}
	public JComboBox<String> getComboTipoProducto() {
		return comboTipoProducto;
	}
	public void setComboTipoProducto(JComboBox<String> comboTipoProducto) {
		this.comboTipoProducto = comboTipoProducto;
	}
	public JTextField getTxtBuscar() {
		return txtBuscar;
	}
	public void setTxtBuscar(JTextField txtBuscar) {
		this.txtBuscar = txtBuscar;
	}
	public JComboBox<String> getComboFiltroTipoProducto() {
		return comboFiltroTipoProducto;
	}
	public void setComboFiltroTipoProducto(JComboBox<String> comboFiltroTipoProducto) {
		this.comboFiltroTipoProducto = comboFiltroTipoProducto;
	}
	public JButton getBtnBuscar() {
		return btnBuscar;
	}
	public void setBtnBuscar(JButton btnBuscar) {
		this.btnBuscar = btnBuscar;
	}
	public JTable getTablaProveedores() {
		return tablaProveedores;
	}
	public void setTablaProveedores(JTable tablaProveedores) {
		this.tablaProveedores = tablaProveedores;
	}
	public DefaultTableModel getModeloTabla() {
		return modeloTabla;
	}
	public void setModeloTabla(DefaultTableModel modeloTabla) {
		this.modeloTabla = modeloTabla;
	}
	public JButton getBtnAgregar() {
		return btnAgregar;
	}
	public void setBtnAgregar(JButton btnAgregar) {
		this.btnAgregar = btnAgregar;
	}
	public JButton getBtnModificar() {
		return btnModificar;
	}
	public void setBtnModificar(JButton btnModificar) {
		this.btnModificar = btnModificar;
	}
	public JButton getBtnEliminar() {
		return btnEliminar;
	}
	public void setBtnEliminar(JButton btnEliminar) {
		this.btnEliminar = btnEliminar;
	}
	public JButton getBtnExportarCSV() {
		return btnExportarCSV;
	}
	public void setBtnExportarCSV(JButton btnExportarCSV) {this.btnExportarCSV = btnExportarCSV;}
}
