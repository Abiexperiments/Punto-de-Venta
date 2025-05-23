package Controlador;

import Modelo.ProductoInventario;
import Modelo.BaseDatos;
import Modelo.ConexionBD;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

import Vista.DialogoDevolucion;
import Vista.VistaInventario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ControladorInventario {
    private VistaInventario vista;
    
    public ControladorInventario(VistaInventario vista) {
        this.vista = vista;
 
        agregarEventos();
        cargarProductosDesdeBD();
       
        vista.getBtnAgregar().addActionListener(e -> {
            boolean valido = true;
            resetearBordes(); // Limpiar bordes previos

            // Validar nombre
            if (vista.getTxtNombre().getText().trim().isEmpty()) {
                vista.getTxtNombre().setBorder(BorderFactory.createLineBorder(Color.RED));
                valido = false;
            }

            // Validar cantidad
            try {
                int cantidad = Integer.parseInt(vista.getTxtCantidad().getText().trim());
                if (cantidad < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                vista.getTxtCantidad().setBorder(BorderFactory.createLineBorder(Color.RED));
                valido = false;
            }

            // Validar stock mínimo
            try {
                int min = Integer.parseInt(vista.getTxtStockMinimo().getText().trim());
                if (min < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                vista.getTxtStockMinimo().setBorder(BorderFactory.createLineBorder(Color.RED));
                valido = false;
            }

            // Validar stock máximo
            try {
                int max = Integer.parseInt(vista.getTxtStockMaximo().getText().trim());
                if (max < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                vista.getTxtStockMaximo().setBorder(BorderFactory.createLineBorder(Color.RED));
                valido = false;
            }

            // Validar categoría seleccionada
            if (vista.getCbCategoria().getSelectedIndex() == -1) {
                vista.getCbCategoria().setBorder(BorderFactory.createLineBorder(Color.RED));
                valido = false;
            }

            // Validar unidad de medida seleccionada
            if (vista.getCbUnidadMedida().getSelectedIndex() == -1) {
                vista.getCbUnidadMedida().setBorder(BorderFactory.createLineBorder(Color.RED));
                valido = false;
            }

            // Validar fecha de recepción
            if (vista.getDateChooser().getDate() == null) {
                vista.getDateChooser().setBorder(BorderFactory.createLineBorder(Color.RED));
                valido = false;
            }

            // Validar fecha de caducidad
            if (vista.getDateCaducidad().getDate() == null) {
                vista.getDateCaducidad().setBorder(BorderFactory.createLineBorder(Color.RED));
                valido = false;
            }

            if (valido) {
                // ✅ Si todo está bien: procesar normalmente
                JOptionPane.showMessageDialog(null, "Producto agregado exitosamente");
                // Aquí insertas en la tabla, etc.
            } else {
                JOptionPane.showMessageDialog(null, "Corrige los campos en rojo");
            }
        });
    }

    private void agregarEventos() {
    	
    	vista.getTablaInventario().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int filaSeleccionada = vista.getTablaInventario().getSelectedRow();
                if (filaSeleccionada != -1) {  // Asegurarse de que haya una fila seleccionada
                    // Obtener los datos de la fila seleccionada
                    String nombre = vista.getModeloTabla().getValueAt(filaSeleccionada, 1).toString();
                    String cantidad = vista.getModeloTabla().getValueAt(filaSeleccionada, 2).toString();
                    String categoria = vista.getModeloTabla().getValueAt(filaSeleccionada, 3).toString();
                    String unidadMedida = vista.getModeloTabla().getValueAt(filaSeleccionada, 4).toString();
                    String fechaRecepcion = vista.getModeloTabla().getValueAt(filaSeleccionada, 5).toString();
                    String fechaCaducidad = vista.getModeloTabla().getValueAt(filaSeleccionada, 6).toString();
                    String stockMin = vista.getModeloTabla().getValueAt(filaSeleccionada, 7).toString();
                    String stockMax = vista.getModeloTabla().getValueAt(filaSeleccionada, 8).toString();

                    
                    // Autocompletar los campos de texto
                    vista.getTxtNombre().setText(nombre);
                    vista.getTxtCantidad().setText(cantidad);
                    vista.getCbCategoria().setSelectedItem(categoria);
                    vista.getCbUnidadMedida().setSelectedItem(unidadMedida);
                    
                    // Convertir las fechas de String a Date (o String si prefieres)
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        vista.getDateChooser().setDate(sdf.parse(fechaRecepcion));
                        vista.getDateCaducidad().setDate(sdf.parse(fechaCaducidad));
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }

                    vista.getTxtStockMinimo().setText(stockMin);
                    vista.getTxtStockMaximo().setText(stockMax);
                }
            }
        });
    	
        vista.getBtnAgregar().addActionListener(e -> agregarProducto());
        vista.getBtnModificar().addActionListener(e -> modificarProducto());
        vista.getBtnEliminar().addActionListener(e -> eliminarProducto());
        vista.getBtnExportarCSV().addActionListener(e -> exportarACSV());
        vista.getBtnExportarXML().addActionListener(e -> exportarAXML());
        vista.getBtnExportarJSON().addActionListener(e -> exportarInventarioAJSON());
        vista.getBtnDevolucion().addActionListener(e -> {
            int fila = vista.getTablaInventario().getSelectedRow();
            if (fila != -1) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Ajusta el formato si es necesario

                    int id = Integer.parseInt(vista.getTablaInventario().getValueAt(fila, 0).toString());
                    String nombre = vista.getTablaInventario().getValueAt(fila, 1).toString();
                    int cantidadDisponible = Integer.parseInt(vista.getTablaInventario().getValueAt(fila, 2).toString());

                    // Mostrar el diálogo de devolución
                    DialogoDevolucion dialogo = new DialogoDevolucion(nombre, cantidadDisponible);
                    dialogo.setVisible(true);

                    if (dialogo.isConfirmado()) {
                        int cantidadDevuelta = dialogo.getCantidad();
                        String motivo = dialogo.getMotivo();

                        int nuevaCantidad = cantidadDisponible - cantidadDevuelta;

                        if (nuevaCantidad <= 0) {
                            new BaseDatos().eliminarProductoInventario(id);
                            ((DefaultTableModel) vista.getTablaInventario().getModel()).removeRow(fila);
                            System.out.println("Producto eliminado por llegar a 0: " + nombre);
                        } else {
                            // Parsear fechas de la tabla (de String a Date)
                            String fechaRecepcionStr = vista.getTablaInventario().getValueAt(fila, 5).toString();
                            Date fechaRecepcion = sdf.parse(fechaRecepcionStr);

                            String fechaCaducidadStr = vista.getTablaInventario().getValueAt(fila, 6).toString();
                            Date fechaCaducidad = sdf.parse(fechaCaducidadStr);

                            // Crear objeto actualizado con fechas parseadas
                            ProductoInventario productoActualizado = new ProductoInventario(
                                id,
                                nombre,
                                nuevaCantidad,
                                vista.getTablaInventario().getValueAt(fila, 3).toString(), // Categoria
                                vista.getTablaInventario().getValueAt(fila, 4).toString(), // Unidad
                                fechaRecepcion,
                                fechaCaducidad,
                                Integer.parseInt(vista.getTablaInventario().getValueAt(fila, 7).toString()), // StockMin
                                Integer.parseInt(vista.getTablaInventario().getValueAt(fila, 8).toString())  // StockMax
                            );

                            new BaseDatos().modificarProductoInventario(productoActualizado);

                            // Actualizar la tabla en pantalla
                            vista.getTablaInventario().setValueAt(nuevaCantidad, fila, 2);
                            System.out.println("Producto actualizado: " + nombre);
                        }

                        // Mostrar mensaje de devolución exitosa
                        JOptionPane.showMessageDialog(vista,
                            "Se devolvieron correctamente " + cantidadDevuelta + " unidad(es) del producto:\n" + nombre,
                            "Devolución exitosa",
                            JOptionPane.INFORMATION_MESSAGE);

                        // Registrar la devolución (solo imprime por ahora)
                        System.out.println("Producto devuelto: " + nombre);
                        System.out.println("Cantidad devuelta: " + cantidadDevuelta);
                        System.out.println("Motivo: " + motivo);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(vista, "Error al procesar la devolución: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(vista, "Selecciona un producto del inventario.");
            }
        });


        vista.getBtnBuscar().addActionListener(e -> {
        	 String texto = vista.getTxtBuscar().getText().trim();
        	    buscarProducto(texto);
            if (texto.isEmpty()) {
                vista.getTableRowSorter().setRowFilter(null); // Quitar filtro
            } else {
                vista.getTableRowSorter().setRowFilter(RowFilter.regexFilter("(?i)" + texto)); // Filtro insensible a mayúsculas
            }
            vista.actualizarTotales(); // Actualiza si es necesario según el filtro
        });

        vista.getBtnReiniciar().addActionListener(e -> {
            vista.getTxtBuscar().setText("");     // Limpia el campo
            vista.getTxtBuscar().requestFocus();  
            vista.getTableRowSorter().setRowFilter(null); // Quita filtro
            cargarProductosDesdeBD();             // Recarga la tabla si lo necesitas

            if (vista.getTablaInventario().getRowCount() > 0) {
                vista.getTablaInventario().setRowSelectionInterval(0, 0); // Selecciona el primero
            }
        });

            
            vista.getModeloTabla().addTableModelListener(e -> {
                int fila = e.getFirstRow();
                int columna = e.getColumn();

                if (fila >= 0 && (columna == 2 || columna == 7 || columna == 8)) {
                    try {
                    	int id = (int) vista.getModeloTabla().getValueAt(fila, 0);
                    	int cantidad = Integer.parseInt(vista.getModeloTabla().getValueAt(fila, 2).toString());
                    	int stockMin = Integer.parseInt(vista.getModeloTabla().getValueAt(fila, 7).toString());
                    	int stockMax = Integer.parseInt(vista.getModeloTabla().getValueAt(fila, 8).toString());

                    	ProductoInventario producto = new ProductoInventario("", cantidad, "", "", null, null, stockMin, stockMax);

                    	String estado = producto.getEstado();  // Mejor que usar un método duplicado

                        String sql = "UPDATE Inventario SET Cantidad=?, StockMin=?, StockMax=?, Estado=? WHERE IdProducto=?";

                        try (Connection conn = ConexionBD.obtenerConexion();
                             PreparedStatement stmt = conn.prepareStatement(sql)) {

                            stmt.setInt(1, cantidad);
                            stmt.setInt(2, stockMin);
                            stmt.setInt(3, stockMax);
                            stmt.setString(4, estado);
                            stmt.setInt(5, id);

                            stmt.executeUpdate();
                            vista.getModeloTabla().setValueAt(estado, fila, 9);

                        }

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(vista, "Error al actualizar el producto.");
                        ex.printStackTrace();
                    }
                }
            });
    }

    private void agregarProducto() {
        try {
            String nombre = vista.getTxtNombre().getText().trim();
            String cantidadStr = vista.getTxtCantidad().getText().trim();
            String stockMinStr = vista.getTxtStockMinimo().getText().trim();
            String stockMaxStr = vista.getTxtStockMaximo().getText().trim();

            // Validar campos obligatorios
            if (nombre.isEmpty() || cantidadStr.isEmpty() || stockMinStr.isEmpty() || stockMaxStr.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Completa todos los campos obligatorios.");
                return;
            }

            int cantidad = Integer.parseInt(cantidadStr);
            int stockMin = Integer.parseInt(stockMinStr);
            int stockMax = Integer.parseInt(stockMaxStr);

            Object selectedCategoria = vista.getCbCategoria().getSelectedItem();
            Object selectedUnidad = vista.getCbUnidadMedida().getSelectedItem();

            if (selectedCategoria == null || selectedUnidad == null) {
                JOptionPane.showMessageDialog(vista, "Selecciona categoría y unidad de medida.");
                return;
            }

            String categoria = selectedCategoria.toString();
            String unidadMedida = selectedUnidad.toString();

            Date fechaRecepcion = vista.getDateChooser().getDate();
            Date fechaCaducidad = vista.getDateCaducidad().getDate();

            if (fechaRecepcion == null || fechaCaducidad == null) {
                JOptionPane.showMessageDialog(vista, "Selecciona las fechas de recepción y caducidad.");
                return;
            }

            ProductoInventario producto = new ProductoInventario(
                nombre, cantidad, categoria, unidadMedida,
                fechaRecepcion, fechaCaducidad, stockMin, stockMax
            );

            BaseDatos.insertarProductoInventario(producto);
            cargarProductosDesdeBD();
            limpiarCampos();
            vista.actualizarTotales();
            JOptionPane.showMessageDialog(vista, "Producto agregado exitosamente");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "Verifica que los campos numéricos sean válidos.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(vista, "Error al guardar el producto en la base de datos.");
            e.printStackTrace();
        }
    }

    
    private void cargarProductosDesdeBD() {
        DefaultTableModel modelo = vista.getModeloTabla();
        modelo.setRowCount(0); // Limpiar tabla

        try {
            List<ProductoInventario> productos = BaseDatos.cargarProductosInventario();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

            for (ProductoInventario producto : productos) {
                Object[] fila = new Object[]{
                    producto.getId(),
                    producto.getNombre(),
                    producto.getCantidad(),
                    producto.getCategoria(),
                    producto.getUnidadMedida(),
                    sdf.format(producto.getFechaRecepcion()), // ya es Date
                    sdf.format(producto.getFechaCaducidad()),
                    producto.getStockMin(),
                    producto.getStockMax(),
                    producto.getEstado()
                };
                modelo.addRow(fila);
            }

            vista.actualizarTotales();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Error al cargar productos desde la base de datos.");
        }
    }


    private void buscarProducto(String textoBusqueda) {
        DefaultTableModel modelo = vista.getModeloTabla();
        modelo.setRowCount(0);
        
        try {
            List<ProductoInventario> productos = BaseDatos.buscarProductosInventario(textoBusqueda);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

            for (ProductoInventario producto : productos) {
                modelo.addRow(new Object[]{
                    producto.getId(),
                    producto.getNombre(),
                    producto.getCantidad(),
                    producto.getCategoria(),
                    producto.getUnidadMedida(),
                    sdf.format(producto.getFechaRecepcion()),  // ✅ aplicar formato
                    sdf.format(producto.getFechaCaducidad()),  // ✅ aplicar formato
                    producto.getStockMin(),
                    producto.getStockMax(),
                    producto.getEstado()
                });
            }
            vista.actualizarTotales();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Error al buscar productos.");
        }
    }

    
    private void insertarProductoEnBD(ProductoInventario producto) {
        String sql = "INSERT INTO Inventario (Nombre, Cantidad, Categoria, UnidadMedida, FechaRecepcion, FechaCaducidad, StockMin, StockMax, Estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        System.out.println("Nombre: " + producto.getNombre());
        System.out.println("Cantidad: " + producto.getCantidad());
        System.out.println("Categoria: " + producto.getCategoria());
        System.out.println("UnidadMedida: " + producto.getUnidadMedida());
        System.out.println("FechaRecepcion: " + producto.getFechaRecepcion());
        System.out.println("FechaCaducidad: " + producto.getFechaCaducidad());
        System.out.println("StockMin: " + producto.getStockMin());
        System.out.println("StockMax: " + producto.getStockMax());
        System.out.println("Estado: " + producto.getEstado());

        try (Connection conn = ConexionBD.obtenerConexion();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, producto.getNombre());
            stmt.setInt(2, producto.getCantidad());
            stmt.setString(3, producto.getCategoria());
            stmt.setString(4, producto.getUnidadMedida());
            
            if (producto.getFechaRecepcion() != null) {
                stmt.setDate(5, new java.sql.Date(producto.getFechaRecepcion().getTime()));
            } else {
                stmt.setNull(5, java.sql.Types.DATE);
            }

            if (producto.getFechaCaducidad() != null) {
                stmt.setDate(6, new java.sql.Date(producto.getFechaCaducidad().getTime()));
            } else {
                stmt.setNull(6, java.sql.Types.DATE);
            }

            // Convertir Date a java.sql.Date
            stmt.setDate(5, new java.sql.Date(producto.getFechaRecepcion().getTime())); // Convertir fecha de recepción
            stmt.setDate(6, new java.sql.Date(producto.getFechaCaducidad().getTime()));  // Convertir fecha de caducidad
            
            stmt.setInt(7, producto.getStockMin());
            stmt.setInt(8, producto.getStockMax());
            stmt.setString(9, producto.getEstado());

            stmt.executeUpdate();
            String estado = producto.getEstado(); // Lógica para obtener el estado

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Error al insertar producto en la base de datos.");
        }
    }

    private LocalDate convertirFecha(String fecha) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(fecha, formatter);
    }

    private void modificarProducto() {
        int fila = vista.getTablaInventario().getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(vista, "Selecciona un producto para modificar.");
            return;
        }

        try {
            Object idObj = vista.getModeloTabla().getValueAt(fila, 0);
            int id;
            if (idObj instanceof Integer) {
                id = (Integer) idObj;
            } else {
                id = Integer.parseInt(idObj.toString());
            }

            String nombre = vista.getTxtNombre().getText();
            int cantidad = Integer.parseInt(vista.getTxtCantidad().getText());
            String categoria = vista.getCbCategoria().getSelectedItem().toString();
            String unidad = vista.getCbUnidadMedida().getSelectedItem().toString();

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String fechaRecepcion = sdf.format(vista.getDateChooser().getDate());
            String fechaCaducidad = sdf.format(vista.getDateCaducidad().getDate());

            int min = Integer.parseInt(vista.getTxtStockMinimo().getText());
            int max = Integer.parseInt(vista.getTxtStockMaximo().getText());

            String sql = "UPDATE Inventario SET Nombre=?, Cantidad=?, Categoria=?, UnidadMedida=?, FechaRecepcion=?, FechaCaducidad=?, StockMin=?, StockMax=? WHERE IdProducto=?";

            try (Connection conn = ConexionBD.obtenerConexion();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, nombre);
                stmt.setInt(2, cantidad);
                stmt.setString(3, categoria);
                stmt.setString(4, unidad);
                stmt.setDate(5, java.sql.Date.valueOf(convertirFecha(fechaRecepcion)));
                stmt.setDate(6, java.sql.Date.valueOf(convertirFecha(fechaCaducidad)));
                stmt.setInt(7, min);
                stmt.setInt(8, max);
                stmt.setInt(9, id);

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(vista, "Producto modificado exitosamente.");
                cargarProductosDesdeBD();
                vista.actualizarTotales();
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Error al modificar el producto.");
        }
    }

    private void eliminarProducto() {
        int fila = vista.getTablaInventario().getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(vista, "Selecciona un producto para eliminar.");
            return;
        }

        int id = (int) vista.getModeloTabla().getValueAt(fila, 0);

        int opcion = JOptionPane.showConfirmDialog(vista, "¿Estás seguro de eliminar este producto?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM Inventario WHERE IdProducto = ?";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(vista, "Producto eliminado correctamente.");
            cargarProductosDesdeBD();
            vista.actualizarTotales();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Error al eliminar producto.");
        }
    }

    private void agregarFilaTabla(ProductoInventario p) {
        vista.getModeloTabla().addRow(new Object[]{
            p.getId(), p.getNombre(), p.getCantidad(), p.getCategoria(),p.getUnidadMedida(),
            p.getFechaRecepcion(),p.getFechaCaducidad(), p.getStockMin(), p.getStockMax(), p.getEstado()
        });
    }
    
    private void recargarTablaCompleta() {
        DefaultTableModel modelo = vista.getModeloTabla();
        modelo.setRowCount(0); // Limpiar la tabla

        try {
            // Obtener la lista de productos desde la base de datos
            List<ProductoInventario> productos = BaseDatos.cargarProductosInventario();

            // Recorrer los productos y agregar las filas en la tabla
            for (ProductoInventario p : productos) {
                modelo.addRow(new Object[]{
                    p.getId(), p.getNombre(), p.getCantidad(), p.getCategoria(),
                    p.getUnidadMedida(), p.getFechaRecepcion(), p.getFechaCaducidad(),
                    p.getStockMin(), p.getStockMax(), p.getEstado()
                });
            }

            // Actualizar los totales
            vista.actualizarTotales();

        } catch (SQLException e) {
            // Manejo de error, por ejemplo, mostrando un mensaje de error
            JOptionPane.showMessageDialog(vista, "Error al cargar los productos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarFilaTabla(int fila, ProductoInventario p) {
        DefaultTableModel modelo = vista.getModeloTabla();
        modelo.setValueAt(p.getNombre(), fila, 1);
        modelo.setValueAt(p.getCantidad(), fila, 2);
        modelo.setValueAt(p.getCategoria(), fila, 3);
        modelo.setValueAt(p.getUnidadMedida(), fila, 4);
        modelo.setValueAt(p.getFechaRecepcion(), fila, 5);
        modelo.setValueAt(p.getFechaCaducidad(), fila, 6); 
        modelo.setValueAt(p.getStockMin(), fila, 7);
        modelo.setValueAt(p.getStockMax(), fila, 8);
        modelo.setValueAt(p.getEstado(), fila, 9);
        
    }

    private void limpiarCampos() {
        vista.getTxtNombre().setText("");
        vista.getTxtCantidad().setText("");
        vista.getCbCategoria().setSelectedIndex(0);
        vista.getCbUnidadMedida().setSelectedIndex(0);
        vista.getDateChooser().setDate(null);
        vista.getDateCaducidad().setDate(null);
        vista.getTxtStockMinimo().setText("");
        vista.getTxtStockMaximo().setText("");
    }
    
    private void resetearBordes() {
        Border normal = UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border");
        vista.getTxtNombre().setBorder(normal);
        vista.getTxtCantidad().setBorder(normal);
        vista.getTxtStockMinimo().setBorder(normal);
        vista.getTxtStockMaximo().setBorder(normal);
        vista.getCbCategoria().setBorder(normal);
        vista.getCbUnidadMedida().setBorder(normal);
        vista.getDateChooser().setBorder(normal);
        vista.getDateCaducidad().setBorder(normal);
    }


    private void exportarACSV() {
        // Crear la carpeta si no existe
        File carpeta = new File("Reporte Inventario CSV");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        // Generar nombre de archivo con fecha/hora para evitar sobrescritura
        String nombreArchivo = "inventario_" + System.currentTimeMillis() + ".csv";
        File archivo = new File(carpeta, nombreArchivo);

        try (PrintWriter writer = new PrintWriter(new FileWriter(archivo))) {
            // Escribir encabezados CSV
            writer.println("ID,Nombre,Cantidad,Categoría,Unidad de Medida,Fecha Ingreso,Fecha Caducidad,Stock Mínimo,Stock Máximo,Estado");

            // Obtener la lista de productos desde la base de datos
            List<ProductoInventario> productos = BaseDatos.cargarProductosInventario();

            // Escribir cada producto del inventario
            for (ProductoInventario p : productos) {
                writer.printf("%s,%s,%d,%s,%s,%s,%s,%d,%d,%s\n",
                        p.getId(), p.getNombre(), p.getCantidad(), p.getCategoria(),
                        p.getUnidadMedida(), p.getFechaRecepcion(), p.getFechaCaducidad(),
                        p.getStockMin(), p.getStockMax(), p.getEstado());
            }

            JOptionPane.showMessageDialog(vista, "Inventario exportado a CSV correctamente en la carpeta 'Reporte Inventario CSV'.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(vista, "Error al exportar el archivo: " + ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista, "Error al cargar los productos desde la base de datos: " + ex.getMessage());
        }
}
    private void exportarAXML() {
        // Crear carpeta "Reportes Inventario" si no existe
        File carpeta = new File("Reportes Inventario XML");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        // Crear nombre de archivo con fecha actual
        SimpleDateFormat sdfFecha = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        String nombreArchivo = "Inventario_" + sdfFecha.format(new Date()) + ".xml";
        File archivoXML = new File(carpeta, nombreArchivo);

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // Elemento raíz
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Inventario");
            doc.appendChild(rootElement);

            DefaultTableModel modelo = vista.getModeloTabla();

            for (int i = 0; i < modelo.getRowCount(); i++) {
                Element producto = doc.createElement("Producto");
                rootElement.appendChild(producto);

                producto.appendChild(crearElemento(doc, "Id", modelo.getValueAt(i, 0).toString()));
                producto.appendChild(crearElemento(doc, "Nombre", modelo.getValueAt(i, 1).toString()));
                producto.appendChild(crearElemento(doc, "Cantidad", modelo.getValueAt(i, 2).toString()));
                producto.appendChild(crearElemento(doc, "Categoria", modelo.getValueAt(i, 3).toString()));
                producto.appendChild(crearElemento(doc, "UnidadMedida", modelo.getValueAt(i, 4).toString()));
                producto.appendChild(crearElemento(doc, "FechaRecepcion", modelo.getValueAt(i, 5).toString()));
                producto.appendChild(crearElemento(doc, "FechaCaducidad", modelo.getValueAt(i, 6).toString()));
                producto.appendChild(crearElemento(doc, "StockMin", modelo.getValueAt(i, 7).toString()));
                producto.appendChild(crearElemento(doc, "StockMax", modelo.getValueAt(i, 8).toString()));
                producto.appendChild(crearElemento(doc, "Estado", modelo.getValueAt(i, 9).toString()));
            }

            // Escribir el contenido al archivo XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(archivoXML);

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);

            JOptionPane.showMessageDialog(vista, "Inventario exportado a XML correctamente en la carpeta 'Reporte Inventario XML'.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error al exportar a XML.");
            ex.printStackTrace();
        }
    }

    private Element crearElemento(Document doc, String etiqueta, String valor) {
        Element elemento = doc.createElement(etiqueta);
        elemento.appendChild(doc.createTextNode(valor));
        return elemento;
    }
    private void exportarInventarioAJSON() {
        // Crear carpeta "Reporte Inventario JSON" si no existe
        File carpeta = new File("Reporte Inventario JSON");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        // Crear nombre de archivo dinámico: inventario_dd-MM-yyyy.json
        SimpleDateFormat sdfNombre = new SimpleDateFormat("dd-MM-yyyy");
        String fechaActual = sdfNombre.format(new Date());
        String nombreArchivo = "inventario_" + fechaActual + ".json";

        // Ruta completa del archivo
        File archivo = new File(carpeta, nombreArchivo);

        try {
            JSONArray jsonArray = new JSONArray();
            SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy"); // formato para fechas

            // Asumo que InventarioDAO.cargarProductos() devuelve List<ProductoInventario>
            List<ProductoInventario> productos = BaseDatos.cargarProductosInventario();

            for (ProductoInventario p : productos) {
                JSONObject prodObj = new JSONObject();
                prodObj.put("id", p.getId());
                prodObj.put("nombre", p.getNombre());
                prodObj.put("cantidad", p.getCantidad());
                prodObj.put("categoria", p.getCategoria());
                prodObj.put("unidadMedida", p.getUnidadMedida());

                // Asumiendo que getFechaRecepcion y getFechaCaducidad devuelven java.util.Date o String
                if (p.getFechaRecepcion() != null) {
                    prodObj.put("fechaRecepcion", 
                        p.getFechaRecepcion() instanceof Date
                            ? sdfFecha.format((Date) p.getFechaRecepcion())
                            : p.getFechaRecepcion());
                } else {
                    prodObj.put("fechaRecepcion", JSONObject.NULL);
                }

                if (p.getFechaCaducidad() != null) {
                    prodObj.put("fechaCaducidad", 
                        p.getFechaCaducidad() instanceof Date
                            ? sdfFecha.format((Date) p.getFechaCaducidad())
                            : p.getFechaCaducidad());
                } else {
                    prodObj.put("fechaCaducidad", JSONObject.NULL);
                }

                prodObj.put("stockMin", p.getStockMin());
                prodObj.put("stockMax", p.getStockMax());
                prodObj.put("estado", p.getEstado() != null ? p.getEstado() : "Desconocido");

                jsonArray.put(prodObj);
            }

            JSONObject root = new JSONObject();
            root.put("inventario", jsonArray);

            try (FileWriter writer = new FileWriter(archivo)) {
                writer.write(root.toString(4)); // JSON con indentación de 4 espacios
            }

            JOptionPane.showMessageDialog(vista, "Inventario exportado a JSON correctamente en la carpeta 'Reporte Inventario JSON'.");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error al exportar el inventario a JSON.");
            ex.printStackTrace();
        }
    }
}