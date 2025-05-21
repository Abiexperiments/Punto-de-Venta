package Controlador;

import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import Patrones.Strategy.RutaReportesStrategy;
import Patrones.Strategy.*;
import Vista.VistaReimpresionReportes;

public class ControladorReimpresionReportes {

    private VistaReimpresionReportes vista;
    private final String basePath = "./"; 
    private List<RutaReportesStrategy> estrategias = new ArrayList<>();


    public ControladorReimpresionReportes(VistaReimpresionReportes vista) {
        this.vista = vista;
        cargarEstrategias();
        initListeners();
        actualizarListaArchivos();
    }

    private void initListeners() {
        vista.getComboTipo().addActionListener(e -> onTipoCambiado());
        vista.getComboFormato().addActionListener(e -> onFormatoCambiado());
        vista.getBotonReimprimir().addActionListener(e -> onBotonReimprimirClic());
       
    }

    private void onTipoCambiado() {
        actualizarListaArchivos();
    }

    private void onFormatoCambiado() {
        actualizarListaArchivos();
    }

    private void onBotonReimprimirClic() {
        reimprimirSeleccionado();
    }

    private void actualizarListaArchivos() {
        String tipo = vista.getComboTipo().getSelectedItem().toString();
        String formato = vista.getComboFormato().getSelectedItem().toString();
        String carpeta = obtenerCarpetaCorrespondiente(tipo, formato);

        DefaultListModel<String> modeloLista = new DefaultListModel<>();
        File dir = new File(basePath + carpeta);

        if (dir.exists() && dir.isDirectory()) {
            String[] archivos = dir.list();
            if (archivos != null) {
                Arrays.sort(archivos);
                for (String archivo : archivos) {
                    modeloLista.addElement(archivo);
                }
            }
        } else {
            modeloLista.addElement("Carpeta no encontrada: " + carpeta);
        }

        vista.getListaArchivos().setModel(modeloLista);
    }

    private void cargarEstrategias() {
    	estrategias.add(new EstrategiaFormatoPDF());
    	estrategias.add(new DefaultStrategy());
    }

    private String obtenerCarpetaCorrespondiente(String tipo, String formato) {
        for (RutaReportesStrategy estrategia : estrategias) {
            if (estrategia.aplica(tipo, formato)) {
                return estrategia.obtenerCarpeta(tipo, formato);
            }
        }
        // No debería llegar aquí por el DefaultStrategy, pero por si acaso:
        return "";
    }

    private void reimprimirSeleccionado() {
        String archivoSeleccionado = vista.getListaArchivos().getSelectedValue();
        if (archivoSeleccionado == null || archivoSeleccionado.isEmpty() || archivoSeleccionado.startsWith("Carpeta no encontrada")) {
            JOptionPane.showMessageDialog(vista, "Selecciona un archivo válido para reimprimir.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String tipo = vista.getComboTipo().getSelectedItem().toString();
        String formato = vista.getComboFormato().getSelectedItem().toString();
        String rutaArchivo = basePath + obtenerCarpetaCorrespondiente(tipo, formato) + File.separator + archivoSeleccionado;

        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            JOptionPane.showMessageDialog(vista, "El archivo no existe: " + rutaArchivo, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            java.awt.Desktop.getDesktop().open(archivo);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "No se pudo abrir el archivo:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
