package Vista;
import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import Modelo.ItemCarrito;
import Modelo.ProductosMenu;

public class VistaMenuGeneralMejorada extends JPanel {
    private JTabbedPane pestañasCategorias;
    private JList<ProductosMenu> listaPlatillos;
    private JList<ProductosMenu> listaBebidas;
    private JList<ProductosMenu> listaPostres;

    private DefaultListModel<ItemCarrito> modeloCarrito;
    private JList<ItemCarrito> listaCarrito;
    private JButton botonAgregar;
    private JButton botonQuitar;
    private JButton botonFinalizar;
    private JButton botonVaciar;
    private JButton botonEliminarDelMenu;
    private JLabel labelTotal;
    private List<ProductosMenu> productosPlatillos = new ArrayList<>();
    private List<ProductosMenu> productosBebidas = new ArrayList<>();
    private List<ProductosMenu> productosPostres = new ArrayList<>();
    private Map<String, Double> mapaPrecios = new HashMap<>();
    
    public VistaMenuGeneralMejorada(List<ProductosMenu> platillos, List<ProductosMenu> bebidas, List<ProductosMenu> postres) {
        setLayout(new BorderLayout());
        this.productosPlatillos = platillos;
        this.productosBebidas = bebidas;
        this.productosPostres = postres;
    
        // Panel izquierdo: Categorías con pestañas
        pestañasCategorias = new JTabbedPane();

        listaPlatillos = new JList<>(new DefaultListModel<>());
        listaBebidas = new JList<>(new DefaultListModel<>());
        listaPostres = new JList<>(new DefaultListModel<>());
        
        listaPlatillos.setCellRenderer(new ProductoMenuRenderer());
        listaBebidas.setCellRenderer(new ProductoMenuRenderer());
        listaPostres.setCellRenderer(new ProductoMenuRenderer());
        
        modeloCarrito = new DefaultListModel<>();
        

        pestañasCategorias.addTab("Platillos", new JScrollPane(listaPlatillos));
        pestañasCategorias.addTab("Bebidas", new JScrollPane(listaBebidas));
        pestañasCategorias.addTab("Postres", new JScrollPane(listaPostres));

        // Panel derecho: Carrito
        JPanel panelCarrito = new JPanel();
        panelCarrito.setLayout(new BoxLayout(panelCarrito, BoxLayout.Y_AXIS));
        panelCarrito.setBorder(BorderFactory.createTitledBorder("Carrito"));

        modeloCarrito = new DefaultListModel<>();
        listaCarrito = new JList<>(modeloCarrito);

        botonAgregar = new JButton("+ Agregar");
        botonQuitar = new JButton("− Quitar");
        botonFinalizar = new JButton("Finalizar Pedido");
        botonVaciar = new JButton("Vaciar Carrito");
        botonEliminarDelMenu = new JButton("Eliminar del Menú"); 
        labelTotal = new JLabel("Total: $0.00");

        panelCarrito.add(new JScrollPane(listaCarrito));
        panelCarrito.add(botonAgregar);
        panelCarrito.add(botonQuitar);
        panelCarrito.add(Box.createVerticalStrut(10));
        panelCarrito.add(botonFinalizar);
        panelCarrito.add(botonVaciar);
        panelCarrito.add(botonEliminarDelMenu);
        panelCarrito.add(Box.createVerticalStrut(10));
        panelCarrito.add(labelTotal);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pestañasCategorias, panelCarrito);
        splitPane.setDividerLocation(600);
        splitPane.setResizeWeight(0.8);

        add(splitPane, BorderLayout.CENTER);
        EstiloGlobalColor.aplicarEstilo(panelCarrito); // si extiende de JPanel
        
    }
  
	// Métodos getter para controlador
    public JTabbedPane getPestañasCategorias() { return pestañasCategorias; }
    public JList<ProductosMenu> getListaPlatillos() { return listaPlatillos; }
    public JList<ProductosMenu> getListaBebidas() { return listaBebidas; }
    public JList<ProductosMenu> getListaPostres() { return listaPostres; }

    public JList<ItemCarrito> getListaCarrito() { return listaCarrito; }
    public DefaultListModel<ItemCarrito> getModeloCarrito() { return modeloCarrito; }

    public JButton getBotonAgregar() { return botonAgregar; }
    public JButton getBotonQuitar() { return botonQuitar; }
    public JButton getBotonFinalizar() { return botonFinalizar; }
    public JButton getBotonVaciar() { return botonVaciar; }
    public JButton getBotonEliminarDelMenu() {
        return botonEliminarDelMenu;
    }
    public JLabel getLabelTotal() { return labelTotal; }

    public void cargarProductos(String categoria) {
        DefaultListModel<ProductosMenu> modelo = new DefaultListModel<>();
        List<ProductosMenu> lista;

        switch (categoria) {
            case "Platillo":
                lista = productosPlatillos;
                break;
            case "Bebida":
                lista = productosBebidas;
                break;
            case "Postre":
                lista = productosPostres;
                break;
            default:
                return;
        }

        for (ProductosMenu p : lista) {
            modelo.addElement(p); 
        }

        switch (categoria) {
            case "Platillo":
                listaPlatillos.setModel(modelo);
                break;
            case "Bebida":
                listaBebidas.setModel(modelo);
                break;
            case "Postre":
                listaPostres.setModel(modelo);
                break;
        }
    }


	public void actualizarListas() {
    cargarProductos("Platillo");
    cargarProductos("Bebida");
    cargarProductos("Postre");
}
	
	public void actualizarListas(Map<String, List<ProductosMenu>> productosPorCategoria) {
	    DefaultListModel<ProductosMenu> modeloPlatillos = new DefaultListModel<>();
	    DefaultListModel<ProductosMenu> modeloBebidas = new DefaultListModel<>();
	    DefaultListModel<ProductosMenu> modeloPostres = new DefaultListModel<>();

	    for (ProductosMenu producto : productosPorCategoria.get("Platillo")) {
	        modeloPlatillos.addElement(producto);
	    }
	    for (ProductosMenu producto : productosPorCategoria.get("Bebida")) {
	        modeloBebidas.addElement(producto);
	    }
	    for (ProductosMenu producto : productosPorCategoria.get("Postre")) {
	        modeloPostres.addElement(producto);
	    }

	    listaPlatillos.setModel(modeloPlatillos);
	    listaBebidas.setModel(modeloBebidas);
	    listaPostres.setModel(modeloPostres);
	}

}