package Controlador;

import Modelo.BaseDatos;
import Modelo.Proveedor;
import Vista.VistaProveedores;
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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ControladorProveedores {
    private VistaProveedores vista;
    private List<Proveedor> listaProveedores;
    private int idProveedor = 1;
    private BaseDatos proveedorbd = new BaseDatos();

    public ControladorProveedores(VistaProveedores vista) {
        this.vista = vista;
        this.listaProveedores = new ArrayList<>();

        agregarListeners();
        cargarProveedoresDesdeBD();

    }
    private void cargarProveedoresDesdeBD() {
        listaProveedores = proveedorbd.obtenerTodosProveedores(); //  error aqui 
        // Asegúrate de actualizar el ID automático
        idProveedor = 1;
        for (Proveedor p : listaProveedores) {
            if (p.getId() >= idProveedor) {
                idProveedor = p.getId() + 1;
            }
        }
        cargarTablaProveedores(listaProveedores);
    }

    private void agregarListeners() {
        vista.btnAgregar.addActionListener(e -> agregarProveedor());
        vista.btnModificar.addActionListener(e -> modificarProveedor());
        vista.btnEliminar.addActionListener(e -> eliminarProveedor());
        vista.btnBuscar.addActionListener(e -> buscarProveedores());
        vista.comboFiltroTipoProducto.addActionListener(e -> filtrarPorTipo());
        vista.btnReiniciar.addActionListener(e -> reiniciarBusqueda());
        vista.btnExportarCSV.addActionListener(e -> exportarCSV());
        vista.btnExportarXML.addActionListener(e -> exportarProveedoresAXML());
        vista.btnExportarJSON.addActionListener(e -> exportarProveedoresAJSON());
        vista.tablaProveedores.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int filaSeleccionada = vista.tablaProveedores.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    // Rellenar campos de texto con los valores de la tabla
                    vista.txtNombre.setText(vista.modeloTabla.getValueAt(filaSeleccionada, 1).toString());
                    vista.txtTelefono.setText(vista.modeloTabla.getValueAt(filaSeleccionada, 2).toString());
                    vista.txtCorreo.setText(vista.modeloTabla.getValueAt(filaSeleccionada, 3).toString());
                    vista.txtCalle.setText(vista.modeloTabla.getValueAt(filaSeleccionada, 4).toString());
                    vista.txtColonia.setText(vista.modeloTabla.getValueAt(filaSeleccionada, 5).toString());
                    vista.txtCiudad.setText(vista.modeloTabla.getValueAt(filaSeleccionada, 6).toString());
                    vista.txtEstado.setText(vista.modeloTabla.getValueAt(filaSeleccionada, 7).toString());
                    vista.txtCP.setText(vista.modeloTabla.getValueAt(filaSeleccionada, 8).toString());
                    vista.comboTipoProducto.setSelectedItem(vista.modeloTabla.getValueAt(filaSeleccionada, 9).toString());
                }
            }
        });

    }

    private void agregarProveedor() {
        Proveedor proveedor = obtenerDatosFormulario();
        if (proveedor != null) {
            proveedor.setId(idProveedor++);
            proveedorbd.guardarProveedores(proveedor);
            
            listaProveedores.add(proveedor);
            cargarTablaProveedores(listaProveedores);
            limpiarFormulario();
        }
    }

    private void modificarProveedor() {
        int fila = vista.tablaProveedores.getSelectedRow();
        if (fila >= 0) {
            Proveedor proveedorModificado = obtenerDatosFormulario();
            if (proveedorModificado != null) {
                int id = Integer.parseInt(vista.modeloTabla.getValueAt(fila, 0).toString());
                proveedorModificado.setId(id);

                listaProveedores.set(fila, proveedorModificado);
                proveedorbd.actualizarProveedores(proveedorModificado);
                
                cargarTablaProveedores(listaProveedores);
                limpiarFormulario();
            }
        } else {
            JOptionPane.showMessageDialog(vista, "Selecciona un proveedor para modificar.");
        }
    }
   
    private void eliminarProveedorDeBD(int id) {
        String sql = "DELETE FROM Proveedores WHERE ID = ?";

        try (Connection conexion = Modelo.ConexionBD.obtenerConexion();
             java.sql.PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(vista, "Error al eliminar proveedor en la base de datos: " + e.getMessage());
        }
    }
    private void eliminarProveedor() {
        int fila = vista.tablaProveedores.getSelectedRow();
        if (fila >= 0) {
            int confirmacion = JOptionPane.showConfirmDialog(vista, "¿Estás seguro de eliminar este proveedor?");
            if (confirmacion == JOptionPane.YES_OPTION) {
                // Obtener el ID desde la tabla
                int id = Integer.parseInt(vista.modeloTabla.getValueAt(fila, 0).toString());
                proveedorbd.eliminarProveedores(id);
                
                listaProveedores.remove(fila);
                cargarTablaProveedores(listaProveedores);
                limpiarFormulario();
            }
        } else {
            JOptionPane.showMessageDialog(vista, "Selecciona un proveedor para eliminar.");
        }
    }


   
    private void buscarProveedores() {
        String textoBusqueda = vista.txtBuscar.getText().trim().toLowerCase();
        if (!textoBusqueda.isEmpty()) {
            ArrayList<Proveedor> resultados = new ArrayList<>();
            for (Proveedor p : listaProveedores) {
                if (p.getNombre().toLowerCase().contains(textoBusqueda) ||
                    p.getColonia().toLowerCase().contains(textoBusqueda) ||
                    p.getCalle().toLowerCase().contains(textoBusqueda) ||
                    p.getEstado().toLowerCase().contains(textoBusqueda) ||
                    String.valueOf(p.getId()).toLowerCase().contains(textoBusqueda) ||
                    p.getTipoProducto().toLowerCase().contains(textoBusqueda) ||
                    String.valueOf(p.getCp()).toLowerCase().contains(textoBusqueda) ||
                    p.getTelefono().toLowerCase().contains(textoBusqueda) ||
                    p.getCorreo().toLowerCase().contains(textoBusqueda)) {
                    
                    resultados.add(p);
                }
            }
            cargarTablaProveedores(resultados);
        }
    }


    private void filtrarPorTipo() {
        String tipoSeleccionado = (String) vista.comboFiltroTipoProducto.getSelectedItem();
        if (!tipoSeleccionado.equals("Todos")) {
            ArrayList<Proveedor> filtrados = new ArrayList<>();
            for (Proveedor p : listaProveedores) {
                if (p.getTipoProducto().equals(tipoSeleccionado)) {
                    filtrados.add(p);
                }
            }
            cargarTablaProveedores(filtrados);
        } else {
            cargarTablaProveedores(listaProveedores);
        }
    }
    private void reiniciarBusqueda() {
        vista.txtBuscar.setText("");
        vista.comboFiltroTipoProducto.setSelectedIndex(0);
        cargarTablaProveedores(listaProveedores);
        vista.getTxtBuscar().requestFocus();
    }
    private void exportarCSV() {
        // Crear carpeta si no existe
        File carpeta = new File("Reportes Proveedores CSV");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
        // Nombre del archivo con fecha actual
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String fechaActual = sdf.format(new Date());
        String nombreArchivo = "proveedores_" + fechaActual + ".csv";

        File archivo = new File(carpeta, nombreArchivo);

        try (FileWriter writer = new FileWriter(archivo)) {
            // Escribir encabezado
            writer.write("ID,Nombre,Teléfono,Correo,Calle,Colonia,Ciudad,Estado,CP,Tipo de Producto\n");

            // Escribir contenido
            for (Proveedor p : listaProveedores) {
                writer.write(String.format("%d,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                        p.getId(), p.getNombre(), p.getTelefono(), p.getCorreo(),
                        p.getCalle(), p.getColonia(), p.getCiudad(), p.getEstado(),
                        p.getCp(), p.getTipoProducto()));
            }

            JOptionPane.showMessageDialog(vista, "Reporte generado automáticamente en:\n" + archivo.getAbsolutePath());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(vista, "Error al generar el CSV: " + ex.getMessage());
        }
    }
    private void exportarProveedoresAXML() {
        // Crear carpeta si no existe
        File carpeta = new File("Reportes Proveedores XML");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        // Nombre del archivo con fecha actual
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String fechaActual = sdf.format(new Date());
        String nombreArchivo = "proveedores_" + fechaActual + ".xml";

        File archivo = new File(carpeta, nombreArchivo);

        try {
            // Crear documento XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // Elemento raíz
            Element root = doc.createElement("proveedores");
            doc.appendChild(root);

            for (Proveedor p : listaProveedores) {
                Element proveedor = doc.createElement("proveedor");

                Element id = doc.createElement("id");
                id.appendChild(doc.createTextNode(String.valueOf(p.getId())));
                proveedor.appendChild(id);

                Element nombre = doc.createElement("nombre");
                nombre.appendChild(doc.createTextNode(p.getNombre()));
                proveedor.appendChild(nombre);

                Element telefono = doc.createElement("telefono");
                telefono.appendChild(doc.createTextNode(p.getTelefono()));
                proveedor.appendChild(telefono);

                Element correo = doc.createElement("correo");
                correo.appendChild(doc.createTextNode(p.getCorreo()));
                proveedor.appendChild(correo);

                Element calle = doc.createElement("calle");
                calle.appendChild(doc.createTextNode(p.getCalle()));
                proveedor.appendChild(calle);

                Element colonia = doc.createElement("colonia");
                colonia.appendChild(doc.createTextNode(p.getColonia()));
                proveedor.appendChild(colonia);

                Element ciudad = doc.createElement("ciudad");
                ciudad.appendChild(doc.createTextNode(p.getCiudad()));
                proveedor.appendChild(ciudad);

                Element estado = doc.createElement("estado");
                estado.appendChild(doc.createTextNode(p.getEstado()));
                proveedor.appendChild(estado);

                Element cp = doc.createElement("cp");
                cp.appendChild(doc.createTextNode(p.getCp()));
                proveedor.appendChild(cp);

                Element tipoProducto = doc.createElement("tipoProducto");
                tipoProducto.appendChild(doc.createTextNode(p.getTipoProducto()));
                proveedor.appendChild(tipoProducto);

                root.appendChild(proveedor);
            }

            // Guardar a archivo
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(archivo);

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);

            JOptionPane.showMessageDialog(vista, "Reporte XML generado automáticamente en:\n" + archivo.getAbsolutePath());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error al generar el XML: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    private void exportarProveedoresAJSON() {
        // Crear carpeta si no existe
        File carpeta = new File("Reportes Proveedores JSON");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        // Nombre del archivo con fecha actual
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String fechaActual = sdf.format(new Date());
        String nombreArchivo = "proveedores_" + fechaActual + ".json";

        File archivo = new File(carpeta, nombreArchivo);

        try (FileWriter writer = new FileWriter(archivo)) {
            // Crear un JSONArray para almacenar los proveedores
            JSONArray proveedoresArray = new JSONArray();

            for (Proveedor p : listaProveedores) {
                JSONObject proveedor = new JSONObject();
                proveedor.put("id", p.getId());
                proveedor.put("nombre", p.getNombre());
                proveedor.put("telefono", p.getTelefono());
                proveedor.put("correo", p.getCorreo());
                proveedor.put("calle", p.getCalle());
                proveedor.put("colonia", p.getColonia());
                proveedor.put("ciudad", p.getCiudad());
                proveedor.put("estado", p.getEstado());
                proveedor.put("cp", p.getCp());
                proveedor.put("tipoProducto", p.getTipoProducto());

                proveedoresArray.put(proveedor);
            }

            // Crear objeto raíz
            JSONObject root = new JSONObject();
            root.put("proveedores", proveedoresArray);

            // Escribir en archivo
            writer.write(root.toString(4)); // Indentado con 4 espacios

            JOptionPane.showMessageDialog(vista, "Reporte JSON generado automáticamente en:\n" + archivo.getAbsolutePath());

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(vista, "Error al generar el JSON: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    private Proveedor obtenerDatosFormulario() {
        try {
            String nombre = vista.txtNombre.getText().trim();
            String telefono = vista.txtTelefono.getText().trim();
            String correo = vista.txtCorreo.getText().trim();
            String calle = vista.txtCalle.getText().trim();
            String colonia = vista.txtColonia.getText().trim();
            String ciudad = vista.txtCiudad.getText().trim();
            String estado = vista.txtEstado.getText().trim();
            String cp = vista.txtCP.getText().trim();
            String tipoProducto = (String) vista.comboTipoProducto.getSelectedItem();
            
            if (nombre.isEmpty() || telefono.isEmpty() || correo.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Completa al menos nombre, teléfono y correo.");
                return null;
            }

            return new Proveedor(0, nombre, telefono, correo, calle, colonia, ciudad, estado, cp, tipoProducto);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error en los datos del formulario.");
            return null;
        }
    }

    private void cargarTablaProveedores(List<Proveedor> listaProveedores2) {
        DefaultTableModel modelo = vista.modeloTabla;
        modelo.setRowCount(0);

        for (Proveedor p : listaProveedores2) {
            modelo.addRow(new Object[]{
                    p.getId(), p.getNombre(), p.getTelefono(), p.getCorreo(),
                    p.getCalle(), p.getColonia(), p.getCiudad(), p.getEstado(),
                    p.getCp(), p.getTipoProducto()
            });
        }
    }

    private void limpiarFormulario() {
        vista.txtNombre.setText("");
        vista.txtTelefono.setText("");
        vista.txtCorreo.setText("");
        vista.txtCalle.setText("");
        vista.txtColonia.setText("");
        vista.txtCiudad.setText("");
        vista.txtEstado.setText("");
        vista.txtCP.setText("");
        vista.comboTipoProducto.setSelectedIndex(0);
    }
}