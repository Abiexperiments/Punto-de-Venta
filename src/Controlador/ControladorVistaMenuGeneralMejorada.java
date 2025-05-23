package Controlador;
import Modelo.BaseDatos;
//arreglarlo con las librerias  
import Modelo.ItemCarrito;
import Modelo.ProductosMenu;
import Modelo.SesionUsuario;
import Modelo.Venta;
import Patrones.Observer.ListaObserver;
import Patrones.Observer.ObserverMenu;
import Vista.TicketVistaPrevia;
import Vista.VistaMenuGeneralMejorada;
import javax.swing.*;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Frame;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ControladorVistaMenuGeneralMejorada implements ObserverMenu{

    private VistaMenuGeneralMejorada vista;
    private Map<String, List<ProductosMenu>> productosPorCategoria;
    private List<ItemCarrito> carrito = new ArrayList<>();
    private double total;
    

    public ControladorVistaMenuGeneralMejorada(VistaMenuGeneralMejorada vista) {
        this.vista = vista;
        this.total = 0.0;
        this.carrito = new ArrayList<>();
     // Me suscribo como observador
        ListaObserver.getInstancia().agregarObservador(this);

        // Obtener productos actuales desde el Subject
        productosPorCategoria = ListaObserver.getInstancia().getProductosPorCategoria();
        vista.actualizarListas(productosPorCategoria);

        // Cargar productos por categoría desde la base
        List<ProductosMenu> platillos = BaseDatos.obtenerProductosPorCategoriaMenu("Platillo");
        List<ProductosMenu> bebidas   = BaseDatos.obtenerProductosPorCategoriaMenu("Bebida");
        List<ProductosMenu> postres   = BaseDatos.obtenerProductosPorCategoriaMenu("Postre");

        productosPorCategoria = new HashMap<>();
        productosPorCategoria.put("Platillo", platillos);
        productosPorCategoria.put("Bebida", bebidas);
        productosPorCategoria.put("Postre", postres);

        // Notifica a los observadores del cambio
        ListaObserver.getInstancia().setProductosPorCategoria(productosPorCategoria);

        vista.actualizarListas(productosPorCategoria);
        agregarListeners();
        verificarPermisos(); 
    }
    
    @Override
    public void onMenuActualizado(Map<String, List<ProductosMenu>> productosPorCategoria) {
        vista.actualizarListas(productosPorCategoria);
    }
    private void agregarListeners() {
        vista.getBotonAgregar().addActionListener(e -> agregarProductoSeleccionado());
        vista.getBotonQuitar().addActionListener(e -> quitarProductoSeleccionado());
        vista.getBotonVaciar().addActionListener(e -> vaciarCarrito());
        vista.getBotonFinalizar().addActionListener(e -> finalizarPedido());
vista.getBotonEliminarDelMenu().addActionListener(e-> eliminarProductoDelMenu());
    }

    private void agregarProductoSeleccionado() {
        int pestaña = vista.getPestañasCategorias().getSelectedIndex();
        String categoria = pestaña == 0 ? "Platillo" : pestaña == 1 ? "Bebida" : "Postre";
        JList<ProductosMenu> lista = pestaña == 0 ? vista.getListaPlatillos()
                : pestaña == 1 ? vista.getListaBebidas()
                               : vista.getListaPostres();


        ProductosMenu seleccionado = lista.getSelectedValue();
        if (seleccionado != null) {
            ProductosMenu producto = buscarProductoPorNombre(categoria, seleccionado.getNombre());
            if (producto != null) {
                boolean encontrado = false;
                for (ItemCarrito item : carrito) {
                    if (item.getProducto().getNombre().equals(producto.getNombre())) {
                        item.incrementarCantidad();
                        encontrado = true;
                        break;
                    }
                }

                if (!encontrado) {
                    carrito.add(new ItemCarrito(producto));
                }

                actualizarModeloCarrito();
                calcularTotal();
            }
        }

    }
//del carrito
    private void quitarProductoSeleccionado() {
    	int index = vista.getListaCarrito().getSelectedIndex();
    	if (index != -1) {
    	    ItemCarrito item = vista.getModeloCarrito().get(index);
    	    item.decrementarCantidad();
    	    if (item.getCantidad() <= 0) {
    	        carrito.remove(index);
    	    }
    	    actualizarModeloCarrito();
    	    calcularTotal();
    	}
    }

    private void vaciarCarrito() {
        carrito.clear();
        vista.getModeloCarrito().clear();
        total = 0.0;
        actualizarTotal();
    }

    private ProductosMenu buscarProductoPorNombre(String categoria, String nombre) {
        for (ProductosMenu producto : productosPorCategoria.get(categoria)) {
            if (producto.getNombre().equals(nombre)) {
                return producto;
            }
        }
        return null;
    }

    private void calcularTotal() {
        total = 0.0;
        for (ItemCarrito item : carrito) {
            total += item.getSubtotal();
        }
        actualizarTotal();
    }

    private void actualizarModeloCarrito() {
    	DefaultListModel<ItemCarrito> modelo = vista.getModeloCarrito();
    	modelo.clear();
    	for (ItemCarrito item : carrito) {
    	    modelo.addElement(item); // ✅ Agrega el objeto completo, no solo el texto
    	}
    }

    private void actualizarTotal() {
        vista.getLabelTotal().setText(String.format("Total: $%.2f", total));
    }
//se puede eliminar
    public void actualizarListaProductos() {
        vista.actualizarListas();
    }
    private void eliminarProductoDelMenu() {
        int pestaña = vista.getPestañasCategorias().getSelectedIndex();
        String categoria = pestaña == 0 ? "Platillo" : pestaña == 1 ? "Bebida" : "Postre";
        JList<ProductosMenu> lista = pestaña == 0 ? vista.getListaPlatillos()
                        : pestaña == 1 ? vista.getListaBebidas()
                                       : vista.getListaPostres();

        ProductosMenu seleccionado = lista.getSelectedValue();
        if (seleccionado != null) {
            // Eliminar por ID
            boolean eliminadoBD = BaseDatos.eliminarProductoPorIdMenu(seleccionado.getId());

            if (eliminadoBD) {
                // Eliminar solo el producto con ese ID
                List<ProductosMenu> productos = productosPorCategoria.get(categoria);
                productos.removeIf(p -> p.getId() == seleccionado.getId());

                vista.actualizarListas(productosPorCategoria);
                JOptionPane.showMessageDialog(vista, "Producto eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(vista, "Error al eliminar el producto de la base de datos.");
            }
        } else {
            JOptionPane.showMessageDialog(vista, "No hay producto seleccionado para eliminar.");
        }
    }

    private void verificarPermisos() {
        String rol = SesionUsuario.getRol();

        if (!rol.equals("Administrador") && !rol.equals("Gerente")) {
            vista.getBotonEliminarDelMenu().setEnabled(false); // o setVisible(false)
        }
    }
 
    private void finalizarPedido() {
        if (carrito.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "El carrito está vacío.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] opciones = {"Efectivo", "Tarjeta", "Cancelar"};
        int seleccion = JOptionPane.showOptionDialog(
                vista,
                "¿Con qué método desea pagar?",
                "Método de Pago",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (seleccion == 2 || seleccion == JOptionPane.CLOSED_OPTION) {
            return; // Cancelado
        }

        String metodoPago = opciones[seleccion];
        double cambio = 0.0;

        // Calcular total
        double total = 0.0;
        for (ItemCarrito item : carrito) {
            total += item.getSubtotal();
        }

        if (metodoPago.equals("Efectivo")) {
            boolean cantidadValida = false;
            while (!cantidadValida) {
                String entrada = JOptionPane.showInputDialog(vista, "Ingrese cantidad con la que paga el cliente:");
                if (entrada == null) {
                    return; // Canceló
                }
                try {
                    double cantidadPagada = Double.parseDouble(entrada);
                    if (cantidadPagada < total) {
                        JOptionPane.showMessageDialog(vista, "Cantidad insuficiente. El total es $" + total, "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        cambio = cantidadPagada - total;
                        cantidadValida = true;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(vista, "Ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        // Calcular IVA y Subtotal
        double subtotal = total / 1.16;
        double iva = total - subtotal;

        // Guardar productos vendidos
        List<String> productosVendidos = new ArrayList<>();
        for (ItemCarrito item : carrito) {
            productosVendidos.add(item.getProducto().getNombre() + " x" + item.getCantidad());
        }

        Venta nuevaVenta = new Venta(
                0,
                productosVendidos,
                total,
                new Date(),
                metodoPago
        );

        BaseDatos.agregarVenta(nuevaVenta); //base de datos agregar venta

        generarPDF(carrito, subtotal, iva, total, metodoPago, cambio);

        vaciarCarrito(); 
    }
    
    private void generarPDF(List<ItemCarrito> carrito, double subtotal, double iva, double total, String metodoPago, double cambio) {
        try {
            List<String> nombresProductos = carrito.stream()
                    .map(item -> item.getProducto().getNombre())
                    .collect(Collectors.toList());

            Venta venta = new Venta(0, nombresProductos, total, new Date(), metodoPago);
            int idVenta = BaseDatos.agregarVentaYObtenerID(venta);

            if (idVenta == -1) {
                JOptionPane.showMessageDialog(null, "Error al guardar la venta en base de datos", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            File carpeta = new File("PedidosPDF");
            if (!carpeta.exists()) carpeta.mkdir();

            String timestamp = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date());
            File archivoPDF = new File(carpeta, "pedido_" + timestamp + ".pdf");

            Document documento = new Document(PageSize.A6); // Más parecido a un ticket pequeño
            PdfWriter.getInstance(documento, new FileOutputStream(archivoPDF));
            documento.open();

            Font fuenteTitulo = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Font fuenteNormal = new Font(Font.FontFamily.COURIER, 10, Font.NORMAL);
            Font fuenteBold = new Font(Font.FontFamily.COURIER, 10, Font.BOLD);
            Font fuenteGracias = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLDITALIC, BaseColor.BLUE);

            documento.add(new Paragraph("La Cocina de Raquel", fuenteTitulo));
            documento.add(new Paragraph("Ticket #" + idVenta, fuenteNormal));

            String fechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(venta.getFecha());
            documento.add(new Paragraph("Fecha: " + fechaHora, fuenteNormal));
            documento.add(new Paragraph("----------------------------------------", fuenteNormal));

            for (ItemCarrito item : carrito) {
                String nombre = item.getProducto().getNombre();
                int cantidad = item.getCantidad();
                double precio = item.getSubtotal();

                String linea = String.format("%-20s %2sx %6.2f", nombre.length() > 20 ? nombre.substring(0, 20) : nombre, cantidad, precio);
                documento.add(new Paragraph(linea, fuenteNormal));
            }

            documento.add(new Paragraph("----------------------------------------", fuenteNormal));
            documento.add(new Paragraph(String.format("Subtotal:           $%6.2f", subtotal), fuenteBold));
            documento.add(new Paragraph(String.format("IVA (16%%):          $%6.2f", iva), fuenteBold));
            documento.add(new Paragraph(String.format("TOTAL:              $%6.2f", total), fuenteBold));
            documento.add(new Paragraph("Método de Pago: " + metodoPago, fuenteNormal));
            if (metodoPago.equalsIgnoreCase("Efectivo")) {
                documento.add(new Paragraph(String.format("Cambio:             $%6.2f", cambio), fuenteBold));
            }

            documento.add(new Paragraph("\n¡Gracias por su compra!", fuenteGracias));
            documento.add(new Paragraph("Vuelva pronto", fuenteNormal));

            documento.close();

            Frame frame = JOptionPane.getFrameForComponent(vista);
            TicketVistaPrevia visor = new TicketVistaPrevia(frame, archivoPDF);
            visor.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al generar o abrir PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}