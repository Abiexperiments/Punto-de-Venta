package Controlador;

import Vista.VistaPrincipal;
import Vista.VistaAgregarProductoMenu;
import Vista.VistaInventario;
import Modelo.ProductosMenu;
import Modelo.SesionUsuario;
import ClasesBD.VentasDAOReportes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import Vista.VistaMenuGeneralMejorada;
import Vista.VistaProveedores;
import Vista.VistaReimpresionReportes;
import Vista.VistaReportes;
import Vista.VistaUsuarios;
import Vista.VistaVentas;


public class ControladorVistaPrincipal implements ActionListener {

    private VistaPrincipal menu;
    private VistaInventario vistaInventario;
    private VistaMenuGeneralMejorada vistaMenuGeneralMejorada;
    private VistaAgregarProductoMenu vistaAgregarProductoMenu;
    private VistaProveedores vistaProveedores;
    private VistaUsuarios vistaUsuarios;
    private VistaVentas vistaVentas;
    private VistaReportes vistaReportes;
   private VistaReimpresionReportes vistaReimpresion;
    
    private String rol;
    
    private Map<String, List<ProductosMenu>> productosPorCategoria;
    private Map<String, Double> mapaPrecios;

    private List<ProductosMenu> platillos;
    private List<ProductosMenu> bebidas;
    private List<ProductosMenu> postres;
    private String nombreUsuario;


    public ControladorVistaPrincipal(VistaPrincipal vistaPrincipal, String nombreUsuario, String rol) {
    	this.nombreUsuario = nombreUsuario;

        this.rol = rol;
        this.menu = vistaPrincipal;
        menu.setTitulo("Punto de Venta La Cocina de Raquel");
        menu.actualizarRol(nombreUsuario, rol);
        configurarPermisos(); 

        // Inicializar listas
        platillos = new ArrayList<>();
        bebidas = new ArrayList<>();
        postres = new ArrayList<>();
        mapaPrecios = new HashMap<>();

        // Inicializar mapa por categoría
        productosPorCategoria = new HashMap<>();
        productosPorCategoria.put("Platillos", platillos);
        productosPorCategoria.put("Bebidas", bebidas);
        productosPorCategoria.put("Postres", postres);

        // Crear vistas
        vistaInventario = new VistaInventario();
        vistaUsuarios = new VistaUsuarios();
        new ControladorUsuarios(vistaUsuarios);
        menu.agregarVista("Usuarios", vistaUsuarios);
        menu.getGestionarUsuarios_JMenuItem().addActionListener(this);

        vistaVentas = new VistaVentas();
        new ControladorVentas(vistaVentas);
       
        menu.getGestionarVentas_JMenuItem().addActionListener(this);
        
               
        vistaReimpresion = new VistaReimpresionReportes();
        new ControladorReimpresionReportes(vistaReimpresion); // asumiendo que tienes este controlador
        menu.agregarVista("Reimpresion", vistaReimpresion);

        try {
            vistaReportes = new VistaReportes(new VentasDAOReportes());
            menu.agregarVista("Reportes", vistaReportes);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(menu, "Error al cargar reportes de ventas", "Error", JOptionPane.ERROR_MESSAGE);
        }

        vistaAgregarProductoMenu = new VistaAgregarProductoMenu();
        vistaMenuGeneralMejorada = new VistaMenuGeneralMejorada(platillos, bebidas, postres);
        vistaProveedores = new VistaProveedores();
        new ControladorProveedores(vistaProveedores);
      
               // Crear controladores
        new ControladorInventario(vistaInventario);
        new ControladorVistaMenuGeneralMejorada(vistaMenuGeneralMejorada); 
        new ControladorAgregarProductoMenu(
            vistaAgregarProductoMenu,
            menu,
            platillos,
            bebidas,
            postres,
            mapaPrecios,
            vistaMenuGeneralMejorada
        );

        // Agregar vistas al menú
        menu.agregarVista("Inventario", vistaInventario);
        menu.agregarVista("Menu", vistaMenuGeneralMejorada);
        menu.agregarVista("AgregarProductoMenu", vistaAgregarProductoMenu);
          menu.agregarVista("Proveedores", vistaProveedores);
 menu.agregarVista("Ventas", vistaVentas);
 
        // Mostrar vista por defecto
        menu.mostrarVista("Menu");

        // Configurar ventana
        menu.setVisible(true);
        menu.setExtendedState(JFrame.MAXIMIZED_BOTH);
        menu.setResizable(false);
        menu.setLocationRelativeTo(null);

        // Asignar listeners
        menu.getMenuItemInventario().addActionListener(this);
        menu.getMenuItemMenu().addActionListener(this);
        menu.getAgregarAlMenu_JMenuItem().addActionListener(this);
        menu.getBtnCerrarSesion().addActionListener(this);
        menu.getVerProveedores_JMenuItem().addActionListener(this);
        menu.getGestionarVentas_JMenuItem().addActionListener(this);
        menu.getVerReportesVentas_JMenuItem().addActionListener(this);
        menu.getReimpresion_JMenuItem().addActionListener(this);

        
    }
        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();

            if (source == menu.getBtnCerrarSesion()) {
                System.exit(0);
            } else if (source == menu.getMenuItemInventario()) {
                menu.mostrarVista("Inventario");
            } else if (source == menu.getMenuItemMenu()) {
                menu.mostrarVista("Menu");
            }else if (source == menu.getVerProveedores_JMenuItem()) {
                menu.mostrarVista("Proveedores");
            }else if (source == menu.getGestionarUsuarios_JMenuItem()) {
                menu.mostrarVista("Usuarios");
            }else if (source == menu.getGestionarVentas_JMenuItem()) {
                menu.mostrarVista("Ventas");
            }else if (source == menu.getVerReportesVentas_JMenuItem()) {
                menu.mostrarVista("Reportes");
            } else if (source == menu.getReimpresion_JMenuItem()) {  
                menu.mostrarVista("Reimpresion");
            
            }}
     
        private void mostrarVistaInventario() {
            if (vistaInventario == null) {
                vistaInventario = new VistaInventario();
                new ControladorInventario(vistaInventario);
            }
            menu.getContentPane().removeAll();
            menu.getContentPane().add(vistaInventario);
            menu.revalidate();
            menu.repaint();
        }
    private void mostrarVistaMenu() {
        if (vistaMenuGeneralMejorada == null) {
            vistaMenuGeneralMejorada = new VistaMenuGeneralMejorada(platillos, bebidas, postres);
            new ControladorVistaMenuGeneralMejorada(vistaMenuGeneralMejorada);
        }
        menu.mostrarPanel(vistaMenuGeneralMejorada);
    }
    
    private void configurarPermisos() {
        switch (rol) {
            case "Administrador":
                menu.actualizarRol(nombreUsuario, rol);
                // Acceso completo, no se restringe nada
                break;

            case "Gerente":

            	menu.getReportes_JMenu().setEnabled(false);
            	menu.getUsuarios_JMenu().setEnabled(false);
            	  menu.getAgregarAlMenu_JMenuItem().setEnabled(false);
            	  menu.getVentas_JMenu().setEnabled(false);
            	              	//nadamas inventario y proveedores
                break;

            case "Cajero":
                menu.getInventarioJMenu().setEnabled(false);
                menu.getUsuarios_JMenu().setEnabled(false);
                menu.getReportes_JMenu().setEnabled(false);
                menu.getAgregarAlMenu_JMenuItem().setEnabled(false); 
                menu.getProveedores_JMenu().setEnabled(false);
                break;

            default:
                // Rol desconocido: se deshabilita todo por seguridad
                menu.getMenuItemInventario().setEnabled(false);
                menu.getGestionarUsuarios_JMenuItem().setEnabled(false);
                menu.getVerReportesVentas_JMenuItem().setEnabled(false);
                menu.getGestionarVentas_JMenuItem().setEnabled(false);
                menu.getAgregarAlMenu_JMenuItem().setEnabled(false);
                menu.getProveedores_JMenu().setVisible(false);
        }
    }
}