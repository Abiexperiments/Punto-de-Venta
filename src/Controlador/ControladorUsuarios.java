package Controlador;

import Vista.VistaUsuarios;

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
import Modelo.BaseDatos;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ControladorUsuarios implements ActionListener {

    private VistaUsuarios vista;
    private DefaultTableModel modelo;

    public ControladorUsuarios(VistaUsuarios vista) {
        this.vista = vista;
        this.modelo = (DefaultTableModel) vista.getTablaUsuarios().getModel();

        agregarListeners();
        cargarUsuarios(); 
    }

    private void agregarListeners() {
        vista.getBtnAgregar().addActionListener(this);
        vista.getBtnModificar().addActionListener(this);
        vista.getBtnEliminar().addActionListener(this);
        vista.getBtnBuscar().addActionListener(this);
        vista.getBtnExportarCSV().addActionListener(e->exportarACSV());
        vista.getBtnExportarXML().addActionListener(e-> exportarAXML());
        vista.getBtnExportarJSON().addActionListener(e-> exportarAJSON());
        vista.getBtnReiniciar().addActionListener(this);
        vista.getTablaUsuarios().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cargarCamposDesdeTabla();
            }
        });
    }

    private void cargarCamposDesdeTabla() {
        int fila = vista.getTablaUsuarios().getSelectedRow();
        if (fila != -1) {
            vista.getTxtUsuario().setText(modelo.getValueAt(fila, 1).toString());
            vista.getTxtContraseña().setText(modelo.getValueAt(fila, 2).toString());
            vista.getCbRol().setSelectedItem(modelo.getValueAt(fila, 3).toString());
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == vista.getBtnAgregar()) {
            String usuario = vista.getTxtUsuario().getText();
            String contraseña = new String(vista.getTxtContraseña().getPassword());
            String rol = vista.getCbRol().getSelectedItem().toString();

            if (!usuario.isEmpty() && !contraseña.isEmpty()) {
                agregarUsuario(usuario, contraseña, rol); // usa el método que guarda en BD
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(vista, "Por favor, completa todos los campos.");
                
            }

        } else if (src == vista.getBtnModificar()) {
            int fila = vista.getTablaUsuarios().getSelectedRow();
            if (fila != -1) {
                int id = (int) modelo.getValueAt(fila, 0);
                String usuario = vista.getTxtUsuario().getText();
                String contraseña = new String(vista.getTxtContraseña().getPassword());
                String rol = vista.getCbRol().getSelectedItem().toString();

                modificarUsuario(id, usuario, contraseña, rol);
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(vista, "Selecciona un usuario para modificar.");
            }

        } else if (src == vista.getBtnEliminar()) {
            int fila = vista.getTablaUsuarios().getSelectedRow();
            if (fila != -1) {
                int id = (int) modelo.getValueAt(fila, 0);
                eliminarUsuario(id);
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(vista, "Selecciona un usuario para eliminar.");
            }

        } else if (src == vista.getBtnBuscar()) {
            String textoBusqueda = vista.getTxtBuscar().getText().toLowerCase();
            if (!textoBusqueda.isEmpty()) {
                for (int i = 0; i < modelo.getRowCount(); i++) {
                    String usuario = modelo.getValueAt(i, 1).toString().toLowerCase();
                    if (usuario.contains(textoBusqueda)) {
                        vista.getTablaUsuarios().setRowSelectionInterval(i, i);
                        cargarCamposDesdeTabla();
                        return;
                    }
                }
                JOptionPane.showMessageDialog(vista, "Usuario no encontrado.");
            }

        } else if (src == vista.getBtnReiniciar()) {
            limpiarCampos();
            vista.getTxtBuscar().setText("");
            vista.getTablaUsuarios().clearSelection();
            cargarUsuarios(); // para recargar la tabla completa
        }
    }
    private void agregarUsuario(String nombre, String contraseña, String rol) {
        if (BaseDatos.agregarUsuario(nombre, contraseña, rol)) {
            JOptionPane.showMessageDialog(null, "Usuario agregado exitosamente.");
            cargarUsuarios();
        } else {
            JOptionPane.showMessageDialog(null, "Error al agregar usuario.");
        }
    }


    private void cargarUsuarios() {
        modelo.setRowCount(0); // Limpia la tabla

        List<Object[]> usuarios = BaseDatos.obtenerUsuarios();
        for (Object[] fila : usuarios) {
            modelo.addRow(fila);
        }
    }

    private void modificarUsuario(int id, String nombre, String contraseña, String rol) {
        if (BaseDatos.modificarUsuario(id, nombre, contraseña, rol)) {
            JOptionPane.showMessageDialog(null, "Usuario actualizado.");
            cargarUsuarios();
        } else {
            JOptionPane.showMessageDialog(null, "Error al modificar usuario.");
        }
    }
    private void eliminarUsuario(int idUsuario) {
        if (BaseDatos.eliminarUsuario(idUsuario)) {
            JOptionPane.showMessageDialog(null, "Usuario eliminado exitosamente.");
            cargarUsuarios();
        } else {
            JOptionPane.showMessageDialog(null, "Error al eliminar usuario.");
        }
    }

    public void exportarACSV() {
        DefaultTableModel modelo = (DefaultTableModel) vista.getTablaUsuarios().getModel();

        // Crear carpeta si no existe
        String nombreCarpeta = "Reportes Usuarios CSV";
        File carpeta = new File(nombreCarpeta);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        // Crear nombre de archivo con fecha
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        String fecha = sdf.format(new Date());
        String nombreArchivo = "usuarios_" + fecha + ".csv";

        File archivo = new File(carpeta, nombreArchivo);

        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(archivo), StandardCharsets.UTF_8))) {
            // Escribir encabezados
            for (int i = 0; i < modelo.getColumnCount(); i++) {
                pw.print(modelo.getColumnName(i));
                if (i < modelo.getColumnCount() - 1) pw.print(",");
            }
            pw.println();

            // Escribir datos de la tabla
            for (int fila = 0; fila < modelo.getRowCount(); fila++) {
                for (int columna = 0; columna < modelo.getColumnCount(); columna++) {
                    pw.print(modelo.getValueAt(fila, columna));
                    if (columna < modelo.getColumnCount() - 1) pw.print(",");
                }
                pw.println();
            }

            JOptionPane.showMessageDialog(vista, "Exportación a CSV completada:\n" + archivo.getAbsolutePath());

        } catch (IOException e) {
            JOptionPane.showMessageDialog(vista, "Error al exportar a CSV:\n" + e.getMessage());
            e.printStackTrace();
        }
    }
    public void exportarAXML() {
        DefaultTableModel modelo = (DefaultTableModel) vista.getTablaUsuarios().getModel();

        // Crear carpeta si no existe
        String nombreCarpeta = "Reportes Usuario XML";
        File carpeta = new File(nombreCarpeta);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        // Crear nombre de archivo con fecha
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        String fecha = sdf.format(new Date());
        String nombreArchivo = "usuarios_" + fecha + ".xml";

        File archivo = new File(carpeta, nombreArchivo);

        try {
            // Crear documento XML
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            // Elemento raíz
            Element rootElement = doc.createElement("usuarios");
            doc.appendChild(rootElement);

            // Recorrer filas de la tabla y crear nodos
            for (int i = 0; i < modelo.getRowCount(); i++) {
                Element usuario = doc.createElement("usuario");

                Element id = doc.createElement("id");
                id.appendChild(doc.createTextNode(modelo.getValueAt(i, 0).toString()));
                usuario.appendChild(id);

                Element nombre = doc.createElement("nombre_usuario");
                nombre.appendChild(doc.createTextNode(modelo.getValueAt(i, 1).toString()));
                usuario.appendChild(nombre);

                Element contraseña = doc.createElement("contraseña");
                contraseña.appendChild(doc.createTextNode(modelo.getValueAt(i, 2).toString()));
                usuario.appendChild(contraseña);

                Element rol = doc.createElement("rol");
                rol.appendChild(doc.createTextNode(modelo.getValueAt(i, 3).toString()));
                usuario.appendChild(rol);

                rootElement.appendChild(usuario);
            }

            // Escribir el contenido del XML en archivo
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(archivo);

            transformer.transform(source, result);

            JOptionPane.showMessageDialog(vista, "Exportación a XML completada:\n" + archivo.getAbsolutePath());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al exportar a XML:\n" + e.getMessage());
            e.printStackTrace();
        }
    }
    public void exportarAJSON() {
        DefaultTableModel modelo = (DefaultTableModel) vista.getTablaUsuarios().getModel();

        // Crear carpeta si no existe
        String nombreCarpeta = "Reportes Usuario JSON";
        File carpeta = new File(nombreCarpeta);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        // Crear nombre de archivo con fecha
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        String fecha = sdf.format(new Date());
        String nombreArchivo = "usuarios_" + fecha + ".json";

        File archivo = new File(carpeta, nombreArchivo);

        JSONArray listaUsuarios = new JSONArray();

        // Convertir cada fila en un objeto JSON
        for (int fila = 0; fila < modelo.getRowCount(); fila++) {
            JSONObject usuario = new JSONObject();
            for (int columna = 0; columna < modelo.getColumnCount(); columna++) {
                String nombreColumna = modelo.getColumnName(columna);
                Object valor = modelo.getValueAt(fila, columna);
                usuario.put(nombreColumna, valor);
            }
            listaUsuarios.put(usuario);
        }

        // Escribir a archivo
        try (FileWriter writer = new FileWriter(archivo)) {
            writer.write(listaUsuarios.toString(4)); // 4 espacios de indentación para formato bonito
            JOptionPane.showMessageDialog(vista, "Exportación a JSON completada:\n" + archivo.getAbsolutePath());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(vista, "Error al exportar a JSON:\n" + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void limpiarCampos() {
        vista.getTxtUsuario().setText("");
        vista.getTxtContraseña().setText("");
        vista.getCbRol().setSelectedIndex(0);
    }
}