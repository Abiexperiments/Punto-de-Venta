package Controlador;

import Modelo.ItemCarrito;
import ClasesBD.ModeloProductosMenuDAO;
import ClasesBD.ModeloVentasDAO;
import Modelo.ProductosMenu;
import Modelo.SesionUsuario;
import Modelo.Venta;
import Vista.VistaMenuGeneralMejorada;

import javax.swing.*;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ControladorVistaMenuGeneralMejorada {

    private VistaMenuGeneralMejorada vista;
    private Map<String, List<ProductosMenu>> productosPorCategoria;
    private List<ItemCarrito> carrito = new ArrayList<>();
    private double total;
    

    public ControladorVistaMenuGeneralMejorada(VistaMenuGeneralMejorada vista) {
        this.vista = vista;
        this.total = 0.0;
        this.carrito = new ArrayList<>();

        // Cargar productos por categoría desde la base
        List<ProductosMenu> platillos = ModeloProductosMenuDAO.obtenerProductosPorCategoria("Platillo");
        List<ProductosMenu> bebidas   = ModeloProductosMenuDAO.obtenerProductosPorCategoria("Bebida");
        List<ProductosMenu> postres   = ModeloProductosMenuDAO.obtenerProductosPorCategoria("Postre");

        productosPorCategoria = new HashMap<>();
        productosPorCategoria.put("Platillo", platillos);
        productosPorCategoria.put("Bebida", bebidas);
        productosPorCategoria.put("Postre", postres);

        vista.actualizarListas(productosPorCategoria); 
        agregarListeners();
        verificarPermisos(); 
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
            boolean eliminadoBD = ModeloProductosMenuDAO.eliminarProductoPorId(seleccionado.getId());

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

        // Generar resumen tipo ticket
        StringBuilder resumen = new StringBuilder("Resumen del Pedido:\n\n");
        resumen.append(String.format("%-20s %-5s %-10s\n", "Producto", "Cant", "Subtotal"));
        for (ItemCarrito item : carrito) {
            resumen.append(String.format("%-20s x%-4d $%.2f\n",
                    item.getProducto().getNombre(),
                    item.getCantidad(),
                    item.getSubtotal()));
        }

        resumen.append("\n");
        resumen.append(String.format("Subtotal: $%.2f\n", subtotal));
        resumen.append(String.format("IVA (16%%): $%.2f\n", iva));
        resumen.append(String.format("TOTAL:     $%.2f\n", total));
        resumen.append("Método de Pago: ").append(metodoPago);
        if (metodoPago.equals("Efectivo")) {
            resumen.append(String.format("\nCambio:    $%.2f", cambio));
        }

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

        ModeloVentasDAO.agregarVenta(nuevaVenta);
        // Opciones después de finalizar pedido
        Object[] botones = {"Imprimir", "Salir"};
        int opcion = JOptionPane.showOptionDialog(
                vista,
                resumen.toString(),
                "Pedido Finalizado",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                botones,
                botones[0]
        );

        if (opcion == 0) {
            generarPDF(carrito, subtotal, iva, total, metodoPago, cambio);
        }

        vaciarCarrito();

        JOptionPane.showMessageDialog(vista, resumen.toString(), "Pedido Finalizado", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void generarPDF(List<ItemCarrito> carrito, double subtotal, double iva, double total, String metodoPago, double cambio) {
        try {
            // 1. Convertir carrito a lista de nombres de productos
            List<String> nombresProductos = carrito.stream()
                    .map(item -> item.getProducto().getNombre())
                    .collect(Collectors.toList());

            // 2. Crear instancia de Venta
            Venta venta = new Venta(0, nombresProductos, total, new Date(), metodoPago);

            // 3. Guardar en base de datos y obtener el ID
            int idVenta = ModeloVentasDAO.agregarVentaYObtenerID(venta);

            // Validación por si falla
            if (idVenta == -1) {
                JOptionPane.showMessageDialog(null, "Error al guardar la venta en base de datos", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 4. Crear archivo PDF
            File carpeta = new File("PedidosPDF");
            if (!carpeta.exists()) {
                carpeta.mkdir();
            }

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String nombreArchivo = "pedido_" + timestamp + ".pdf";
            File archivoPDF = new File(carpeta, nombreArchivo);

            // Usamos ID real como número de ticket
            String ticketID = "Ticket #" + idVenta;

            Document documento = new Document();
            PdfWriter.getInstance(documento, new FileOutputStream(archivoPDF));
            documento.open();

            Font fuenteTitulo = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font fuenteNormal = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
            Font fuenteGracias = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLDITALIC, BaseColor.BLUE);

            documento.add(new Paragraph("La Cocina de Raquel", fuenteTitulo));
            documento.add(new Paragraph("----------------------------------------", fuenteNormal));

            String fechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(venta.getFecha());
            documento.add(new Paragraph(ticketID + "     Fecha: " + fechaHora, fuenteNormal));
            documento.add(new Paragraph(" "));

            PdfPTable tabla = new PdfPTable(3);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{4, 2, 2});

            tabla.addCell(new PdfPCell(new Phrase("Producto", fuenteNormal)));
            tabla.addCell(new PdfPCell(new Phrase("Cant", fuenteNormal)));
            tabla.addCell(new PdfPCell(new Phrase("Subtotal", fuenteNormal)));

            for (ItemCarrito item : carrito) {
                tabla.addCell(new PdfPCell(new Phrase(item.getProducto().getNombre(), fuenteNormal)));
                tabla.addCell(new PdfPCell(new Phrase("x" + item.getCantidad(), fuenteNormal)));
                tabla.addCell(new PdfPCell(new Phrase(String.format("$%.2f", item.getSubtotal()), fuenteNormal)));
            }

            documento.add(tabla);
            documento.add(new Paragraph(" "));

            documento.add(new Paragraph(String.format("Subtotal: $%.2f", subtotal), fuenteNormal));
            documento.add(new Paragraph(String.format("IVA (16%%): $%.2f", iva), fuenteNormal));
            documento.add(new Paragraph(String.format("TOTAL:     $%.2f", total), fuenteNormal));
            documento.add(new Paragraph("Método de Pago: " + metodoPago, fuenteNormal));
            if (metodoPago.equals("Efectivo")) {
                documento.add(new Paragraph(String.format("Cambio:    $%.2f", cambio), fuenteNormal));
            }

            documento.add(new Paragraph(" "));
            documento.add(new Paragraph("----------------------------------------", fuenteNormal));
            Paragraph gracias = new Paragraph("¡Gracias por su compra!", fuenteGracias);
            gracias.setAlignment(Element.ALIGN_CENTER);
            documento.add(gracias);

            documento.close();

            JOptionPane.showMessageDialog(null, "Ticket generado: " + nombreArchivo, ".PDF", JOptionPane.INFORMATION_MESSAGE);
            Desktop.getDesktop().open(archivoPDF);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al generar o abrir PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}