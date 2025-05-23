
package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import Modelo.BaseDatos;
import Modelo.ProductosMenu;
import Patrones.Factory.ProductoMenuFactory;
import Vista.VistaAgregarProductoMenu;
import Vista.VistaMenuGeneralMejorada;
import Vista.VistaPrincipal;

public class ControladorAgregarProductoMenu {

    private VistaAgregarProductoMenu vistaAgregar;
    private VistaPrincipal vistaPrincipal;
    private VistaMenuGeneralMejorada vistaMenuGeneralMejorada;

    private List<ProductosMenu> platillos;
    private List<ProductosMenu> bebidas;
    private List<ProductosMenu> postres;
    private Map<String, Double> precios;

    public ControladorAgregarProductoMenu(VistaAgregarProductoMenu vistaAgregar,
                                          VistaPrincipal vistaPrincipal,
                                          List<ProductosMenu> platillos,
                                          List<ProductosMenu> bebidas,
                                          List<ProductosMenu> postres,
                                          Map<String, Double> precios,
                                          VistaMenuGeneralMejorada vistaMenuGeneralMejorada) {
        this.vistaAgregar = vistaAgregar;
        this.vistaPrincipal = vistaPrincipal;
        this.platillos = platillos;
        this.bebidas = bebidas;
        this.postres = postres;
        this.precios = precios;
        this.vistaMenuGeneralMejorada = vistaMenuGeneralMejorada;

        inicializar();
    }

    private void inicializar() {
        vistaAgregar.getBotonAgregar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarProducto();
            }
        });

        vistaPrincipal.getAgregarAlMenu_JMenuItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vistaPrincipal.mostrarVista("AgregarProductoMenu");
            }
        });

        vistaPrincipal.agregarVista("AgregarProductoMenu", vistaAgregar);
    }

    private void agregarProducto() {
        String nombre = vistaAgregar.getNombre().trim();
        String precioStr = vistaAgregar.getPrecio().trim();
        String categoria = vistaAgregar.getCategoria();
        String rutaImagen = vistaAgregar.getRutaImagenSeleccionada();

        if (nombre.isEmpty() || precioStr.isEmpty()) {
            JOptionPane.showMessageDialog(vistaAgregar, "Por favor completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
        	ProductosMenu nuevoProducto = ProductoMenuFactory.crearProducto(nombre, precioStr, categoria, rutaImagen);

            // Guardar en base de datos
           BaseDatos.insertarProductoMenu(nuevoProducto);

            // Refrescar vista del menú desde base de datos
            actualizarListasDesdeBD();

            JOptionPane.showMessageDialog(vistaAgregar, "Producto agregado correctamente al menú.");
            vistaAgregar.limpiarCampos();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vistaAgregar, "El precio debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void actualizarListasDesdeBD() {
        platillos.clear();
        bebidas.clear();
        postres.clear();

        platillos.addAll(BaseDatos.obtenerProductosPorCategoriaMenu("Platillo"));
        bebidas.addAll(BaseDatos.obtenerProductosPorCategoriaMenu("Bebida"));
        postres.addAll(BaseDatos.obtenerProductosPorCategoriaMenu("Postre"));

        precios.clear();
        for (ProductosMenu p : platillos) precios.put(p.getNombre(), p.getPrecio());
        for (ProductosMenu p : bebidas) precios.put(p.getNombre(), p.getPrecio());
        for (ProductosMenu p : postres) precios.put(p.getNombre(), p.getPrecio());

        vistaMenuGeneralMejorada.actualizarListas();
    }

}
