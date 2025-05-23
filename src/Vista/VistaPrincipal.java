package Vista;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
 
public class VistaPrincipal extends JFrame {

	private JMenuBar menuBar;
	private JMenu Ventas_JMenu ,InventarioJMenu,Menu_JMenu;
	private JMenu Proveedores_JMenu;
	private JMenu Usuarios_JMenu;
	private JMenuItem menuItemInventario;
		private JMenuItem VerMenu_JMenuItem;
		private JPanel panelContenido;
		
		private CardLayout cardLayout;
		private JMenuItem AgregarAlMenu_JMenuItem;
		private JMenuItem verProveedores_JMenuItem;
		private JButton btnCerrarSesion;
		private JMenuItem GestionarUsuarios_JMenuItem;
		private JMenuItem GestionarVentas_JMenuItem;
		private JMenu Reportes_JMenu;
		private JMenuItem VerReportesVentas_JMenuItem;
		
		private JLabel lblUsuarioRol;
		private JMenuItem Reimpresion_JMenuItem;
	
		public VistaPrincipal() {
			super("Punto de Venta");
		    setBounds(100, 100, 873, 488);
		    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		    cardLayout = new CardLayout();
		    panelContenido = new JPanel(cardLayout);
		    getContentPane().add(panelContenido, BorderLayout.CENTER);

		    // Menú
		    menuBar = new JMenuBar();
		    setJMenuBar(menuBar);

		    Ventas_JMenu = new JMenu(" Ventas");
		    menuBar.add(Ventas_JMenu);
		    
		    GestionarVentas_JMenuItem = new JMenuItem("Gestionar Ventas");
		    Ventas_JMenu.add(GestionarVentas_JMenuItem);

		    InventarioJMenu = new JMenu("Inventario");
		    menuBar.add(InventarioJMenu);

		    Menu_JMenu = new JMenu("Menu ");
		    menuBar.add(Menu_JMenu);

		    VerMenu_JMenuItem = new JMenuItem("Ver Menu");
		    Menu_JMenu.add(VerMenu_JMenuItem);
		    
		    AgregarAlMenu_JMenuItem = new JMenuItem("Agregar al Menu");
		    Menu_JMenu.add(AgregarAlMenu_JMenuItem);

		    Proveedores_JMenu = new JMenu("Proveedores");
		    menuBar.add(Proveedores_JMenu);
		    
		    verProveedores_JMenuItem = new JMenuItem("ver Proveedores");
		    Proveedores_JMenu.add(verProveedores_JMenuItem);

		    Usuarios_JMenu = new JMenu("Usuarios");
		    menuBar.add(Usuarios_JMenu);
		    
		    GestionarUsuarios_JMenuItem = new JMenuItem("Gestionar Usuarios");
		    Usuarios_JMenu.add(GestionarUsuarios_JMenuItem);

		    menuItemInventario = new JMenuItem("Ver Inventario");
		    InventarioJMenu.add(menuItemInventario);
		    
		    Reportes_JMenu = new JMenu("Reportes");
		    menuBar.add(Reportes_JMenu);
		    
		    VerReportesVentas_JMenuItem = new JMenuItem("Ver Reportes de Ventas");
		    Reportes_JMenu.add(VerReportesVentas_JMenuItem);
		    
		    Reimpresion_JMenuItem = new JMenuItem("Reimpresion");
		    Reportes_JMenu.add(Reimpresion_JMenuItem);
		    
		    btnCerrarSesion = new JButton("Cerrar Sesion");
		    menuBar.add(btnCerrarSesion);
		    
		    //rol usuario
		 // 🔹 Aquí agregamos el glue (esto empuja lo que sigue hacia la derecha)
		    menuBar.add(Box.createHorizontalGlue());

lblUsuarioRol = new JLabel("Rol: Invitado"); // ahora es variable de instancia
lblUsuarioRol.setFont(new Font("Segoe UI", Font.PLAIN, 14));
lblUsuarioRol.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
menuBar.add(lblUsuarioRol);
		    
		 // Asignar JMenuBar al JFrame
		    setJMenuBar(menuBar);
		}
	public void mostrarPanel(JPanel nuevoPanel) {
	    panelContenido.removeAll();
	    panelContenido.add(nuevoPanel, BorderLayout.CENTER);
	    panelContenido.revalidate();
	    panelContenido.repaint();
	}
	public void agregarVista(String nombre, Component comp) {
	    if (comp != null) {
	        panelContenido.add(comp, nombre); // <- esta línea debe recibir algo válido
	    } else {
	        System.err.println("Error: Se intentó agregar un componente nulo con el nombre: " + nombre);
	    }
	}

	public JMenu getInventarioJMenu() {return InventarioJMenu;}

	public void setInventarioJMenu(JMenu inventarioJMenu) {InventarioJMenu = inventarioJMenu;}

	public void mostrarVista(String nombre) {cardLayout.show(panelContenido, nombre);}
	
	public JMenuItem getMenuItemInventario() {
	    return menuItemInventario;
	}
	
	public JMenu getVentas_JMenu() {
		return Ventas_JMenu;
	}

	public void setVentas_JMenu(JMenu ventas_JMenu) {
		Ventas_JMenu = ventas_JMenu;
	}

	public JMenu getGestionar_Productos_JMenu() {
		return InventarioJMenu;
	}

	public void setGestionar_Productos_JMenu(JMenu gestionar_Productos_JMenu) {
		InventarioJMenu = gestionar_Productos_JMenu;
	}

	public JMenu getMenu_JMenu() {
		return Menu_JMenu;
	}

	public void setMenu_JMenu(JMenu menuDia_JMenu) {
		Menu_JMenu = menuDia_JMenu;
	}
	
	public JMenu getProveedores_JMenu() {
		return Proveedores_JMenu;
	}

	public void setProveedores_JMenu(JMenu proveedores_JMenu) {
		Proveedores_JMenu = proveedores_JMenu;
	}

	public JMenu getEmpleados_JMenu() {
		return Usuarios_JMenu;
	}

	public void setEmpleados_JMenu(JMenu empleados_JMenu) {
		Usuarios_JMenu = empleados_JMenu;
	}
	
	public JMenuItem getMenuItemMenu() {
	    return VerMenu_JMenuItem;
	}
	public JMenuItem getAgregarAlMenu_JMenuItem() {
		return AgregarAlMenu_JMenuItem;
	}

	public void setAgregarAlMenu_JMenuItem(JMenuItem agregarAlMenu_JMenuItem) {
		AgregarAlMenu_JMenuItem = agregarAlMenu_JMenuItem;
	}

	public void setTitulo(String string) {
	}

	public JMenuItem getVerProveedores_JMenuItem() {
		return verProveedores_JMenuItem;
	}

	public void setVerProveedores_JMenuItem(JMenuItem verProveedores_JMenuItem) {this.verProveedores_JMenuItem = verProveedores_JMenuItem;}

	public JButton getBtnCerrarSesion() {return btnCerrarSesion;}

	public void setBtnCerrarSesion(JButton btnCerrarSesion) {this.btnCerrarSesion = btnCerrarSesion;}

	public JMenu getProductos_JMenu() {return InventarioJMenu;}

	public void setProductos_JMenu(JMenu productos_JMenu) {InventarioJMenu = productos_JMenu;}

	public JMenu getUsuarios_JMenu() {return Usuarios_JMenu;}

	public void setUsuarios_JMenu(JMenu usuarios_JMenu) {Usuarios_JMenu = usuarios_JMenu;}

	public JMenuItem getVerMenu_JMenuItem() {return VerMenu_JMenuItem;}

	public void setVerMenu_JMenuItem(JMenuItem verMenu_JMenuItem) {VerMenu_JMenuItem = verMenu_JMenuItem;}

	public JMenuItem getGestionarUsuarios_JMenuItem() {return GestionarUsuarios_JMenuItem;}

	public void setGestionarUsuarios_JMenuItem(JMenuItem gestionarUsuarios_JMenuItem) {GestionarUsuarios_JMenuItem = gestionarUsuarios_JMenuItem;}

	public JMenuItem getGestionarVentas_JMenuItem() {return GestionarVentas_JMenuItem;}

	public void setGestionarVentas_JMenuItem(JMenuItem gestionarVentas_JMenuItem) {GestionarVentas_JMenuItem = gestionarVentas_JMenuItem;}

	public void setMenuItemInventario(JMenuItem menuItemInventario) {this.menuItemInventario = menuItemInventario;}

	public JMenuItem getMenuItemProductos() {return menuItemInventario;}

	public void setMenuItemProductos(JMenuItem menuItemProductos) {this.menuItemInventario = menuItemProductos;}

	public JMenu getReportes_JMenu() {return Reportes_JMenu;}

	public void setReportes_JMenu(JMenu reportes_JMenu_) {Reportes_JMenu = reportes_JMenu_;}

	public JMenuItem getVerReportesVentas_JMenuItem() {return VerReportesVentas_JMenuItem;}
	
	public void setVerReportesVentas_JMenuItem(JMenuItem verReportesVentas_JMenuItem) {VerReportesVentas_JMenuItem = verReportesVentas_JMenuItem;}

	public void actualizarRol(String usuario, String rol) {lblUsuarioRol.setText("Usuario: " + usuario + " | Rol: " + rol);}
	public JMenuItem getReimpresion_JMenuItem() {return Reimpresion_JMenuItem;}
	
	public void setReimpresion_JMenuItem(JMenuItem reimpresion_JMenuItem) {Reimpresion_JMenuItem = reimpresion_JMenuItem;}
}
