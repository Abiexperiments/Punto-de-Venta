package Controlador;

import Modelo.BaseDatos;
import Modelo.SesionUsuario;
import Modelo.Venta;
import Vista.VistaVentas;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class ControladorVentas {
    private VistaVentas vista;
    private List<Venta> ventasRegistradas;

    public ControladorVentas(VistaVentas vista) {
        this.vista = vista;
        this.ventasRegistradas = BaseDatos.obtenerVentas(); // ahora se cargan desde Access

        mostrarVentasEnTabla();

        vista.getBtnBuscar().addActionListener(e -> buscarVenta());
        vista.getBtnReiniciar().addActionListener(e -> {
        	 vista.getTxtBuscar().setText("");
        	 vista.getTxtBuscar().requestFocus();
            ventasRegistradas = BaseDatos.obtenerVentas(); // recargar
            mostrarVentasEnTabla();
        });
        vista.getBtnEliminar().addActionListener(e -> eliminarVenta());
        vista.getBtnExportarXML().addActionListener(e-> exportarVentasAXML());
        vista.getBtnExportarCSV().addActionListener(e-> exportarVentasACSV());
        vista.getBtnExportarJSON().addActionListener(e-> exportarVentasAJSON());
verificarPermisos();
    }
    private void verificarPermisos() {
        String rol = SesionUsuario.getRol();

        if (!rol.equals("Administrador") && !rol.equals("Gerente")) {
            vista.getBtnEliminar().setEnabled(false); 
        }
    }

    private void mostrarVentasEnTabla() {
        DefaultTableModel modelo = vista.getModeloTabla();
        modelo.setRowCount(0);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (Venta v : ventasRegistradas) {
        	modelo.addRow(new Object[]{
        		    v.getIdVenta(),
        		    String.join(", ", v.getProductos()),
        		    v.getTotal(),
        		    sdf.format(v.getFecha()),
        		    v.getMetodoPago() != null ? v.getMetodoPago() : "No especificado"
        		    	
        		});
        }
        vista.actualizarResumenVentas();
    }

    private void buscarVenta() {
    	String filtro = vista.getTxtBuscar().getText().trim().toLowerCase();
        if (filtro.isEmpty()) return;

        DefaultTableModel modelo = vista.getModeloTabla();
        modelo.setRowCount(0);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (Venta v : ventasRegistradas) {
            String idVenta = String.valueOf(v.getIdVenta());
            String productos = String.join(", ", v.getProductos()).toLowerCase();
            String metodoPago = v.getMetodoPago() != null ? v.getMetodoPago().toLowerCase() : "";
            String fecha = sdf.format(v.getFecha()).toLowerCase();

            if (idVenta.equals(filtro) ||
                productos.contains(filtro) ||
                metodoPago.contains(filtro) ||
                fecha.contains(filtro)) {

                modelo.addRow(new Object[]{
                    v.getIdVenta(),
                    String.join(", ", v.getProductos()),
                    v.getTotal(),
                    sdf.format(v.getFecha()),
                    v.getMetodoPago() != null ? v.getMetodoPago() : "No especificado"
                });
            }
        }
        vista.actualizarResumenVentas();
    }    

    private void eliminarVenta() {
        int fila = vista.getTablaVentas().getSelectedRow();

        if (fila != -1) {
            int id = (int) vista.getModeloTabla().getValueAt(fila, 0);

            BaseDatos.eliminarVentaPorId(id);
            ventasRegistradas.removeIf(v -> v.getIdVenta() == id);

            mostrarVentasEnTabla();
            
        } else {
            JOptionPane.showMessageDialog(null, "Selecciona una venta para eliminar.");
        }
    }
    private void exportarVentasAXML() {
        // Crear carpeta "Reportes Ventas XML" si no existe
        File carpeta = new File("Reportes Ventas XML");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        // Formato de fecha para el nombre del archivo
        SimpleDateFormat sdfNombre = new SimpleDateFormat("dd-MM-yyyy");
        String fechaActual = sdfNombre.format(new Date());
        String nombreArchivo = "ventas_" + fechaActual + ".xml";

        // Archivo de salida
        File archivo = new File(carpeta, nombreArchivo);

        try {
            // Crear documento XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // Elemento raíz
            Element root = doc.createElement("ventas");
            doc.appendChild(root);

            for (Venta v : ventasRegistradas) {
                Element ventaElement = doc.createElement("venta");

                Element id = doc.createElement("idVenta");
                id.appendChild(doc.createTextNode(String.valueOf(v.getIdVenta())));
                ventaElement.appendChild(id);

                Element productos = doc.createElement("productos");
                productos.appendChild(doc.createTextNode(String.join(", ", v.getProductos())));
                ventaElement.appendChild(productos);

                Element total = doc.createElement("total");
                total.appendChild(doc.createTextNode(String.valueOf(v.getTotal())));
                ventaElement.appendChild(total);

                Element fecha = doc.createElement("fecha");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                fecha.appendChild(doc.createTextNode(sdf.format(v.getFecha())));
                ventaElement.appendChild(fecha);

                Element metodoPago = doc.createElement("metodoPago");
                metodoPago.appendChild(doc.createTextNode(v.getMetodoPago() != null ? v.getMetodoPago() : "No especificado"));
                ventaElement.appendChild(metodoPago);

                root.appendChild(ventaElement);
            }

            // Guardar a archivo
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(archivo);
            transformer.transform(source, result);

            JOptionPane.showMessageDialog(vista, "Ventas exportadas a XML correctamente en la carpeta 'Reportes Ventas XML'.");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error al exportar a XML.");
            ex.printStackTrace();
        }
        }

    private void exportarVentasACSV() {
        // Crear carpeta "Reportes Ventas CSV" si no existe
        File carpeta = new File("Reportes Ventas CSV");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        // Crear nombre dinámico del archivo: ventas_21-05-2025.csv
        SimpleDateFormat sdfNombre = new SimpleDateFormat("dd-MM-yyyy");
        String fechaActual = sdfNombre.format(new Date());
        String nombreArchivo = "ventas_" + fechaActual + ".csv";

        // Crear el archivo directamente en la carpeta
        File archivo = new File(carpeta, nombreArchivo);

        try (PrintWriter pw = new PrintWriter(archivo)) {
            // Escribir encabezados
            pw.println("ID Venta,Pedidos,Total,Fecha,Metodo de Pago");

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            for (Venta v : ventasRegistradas) {
                String id = String.valueOf(v.getIdVenta());
                String productos = "\"" + String.join(", ", v.getProductos()) + "\""; // evitar problemas con comas
                String total = String.valueOf(v.getTotal());
                String fecha = sdf.format(v.getFecha());
                String metodoPago = v.getMetodoPago() != null ? v.getMetodoPago() : "No especificado";

                pw.printf("%s,%s,%s,%s,%s%n", id, productos, total, fecha, metodoPago);
            }

            JOptionPane.showMessageDialog(vista, "Ventas exportadas a JSON correctamente en la carpeta 'Reportes Ventas CSV'.");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error al exportar a CSV.");
            ex.printStackTrace();
        }
    }

    private void exportarVentasAJSON() {
        // Crear carpeta "Reportes Ventas JSON" si no existe
        File carpeta = new File("Reportes Ventas JSON");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        // Crear nombre de archivo dinámico: ventas_14-05-2025.json
        SimpleDateFormat sdfNombre = new SimpleDateFormat("dd-MM-yyyy");
        String fechaActual = sdfNombre.format(new Date());
        String nombreArchivo = "ventas_" + fechaActual + ".json";

        // Ruta completa del archivo
        File archivo = new File(carpeta, nombreArchivo);

        try {
            JSONArray jsonArray = new JSONArray();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            for (Venta v : ventasRegistradas) {
                JSONObject ventaObj = new JSONObject();
                ventaObj.put("idVenta", v.getIdVenta());
                ventaObj.put("productos", v.getProductos());
                ventaObj.put("total", v.getTotal());
                ventaObj.put("fecha", sdf.format(v.getFecha()));
                ventaObj.put("metodoPago", v.getMetodoPago() != null ? v.getMetodoPago() : "No especificado");

                jsonArray.put(ventaObj);
            }

            JSONObject root = new JSONObject();
            root.put("ventas", jsonArray);

            try (FileWriter writer = new FileWriter(archivo)) {
                writer.write(root.toString(4)); // indentación de 4 espacios
            }

            JOptionPane.showMessageDialog(vista, "Ventas exportadas a JSON correctamente en la carpeta 'Reportes Ventas JSON'.");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error al exportar a JSON.");
            ex.printStackTrace();
        }
    }
    }