package Vista;
import javax.swing.*;
import java.awt.*;

public class VistaReimpresionReportes extends JPanel {
    private JComboBox<String> comboTipo;
    private JComboBox<String> comboFormato;
    private DefaultListModel<String> modeloLista;
    private JList<String> listaArchivos;
    private JButton botonReimprimir;
    
    public VistaReimpresionReportes() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelSuperior = new JPanel();
        comboTipo = new JComboBox<>(new String[]{"Inventario", "Ventas", "Proveedores", "Pedidos"});
        comboFormato = new JComboBox<>(new String[]{"PDF", "CSV", "JSON", "XML"});
        panelSuperior.add(new JLabel("Tipo: "));
        panelSuperior.add(comboTipo);
        panelSuperior.add(new JLabel("Formato: "));
        panelSuperior.add(comboFormato);
        add(panelSuperior, BorderLayout.NORTH);
        
        // Panel central con búsqueda + lista
        JPanel panelCentro = new JPanel(new BorderLayout(5, 5));
        
     // Lista de archivos
        modeloLista = new DefaultListModel<>();
        listaArchivos = new JList<>(modeloLista);
        panelCentro.add(new JScrollPane(listaArchivos), BorderLayout.CENTER);

        add(panelCentro, BorderLayout.CENTER);
        
        botonReimprimir = new JButton("Reimprimir");
        add(botonReimprimir, BorderLayout.SOUTH);
        EstiloGlobalColor.aplicarEstilo(this); // si extiende de JPanel
    }
    
    public JComboBox<String> getComboTipo() { return comboTipo; }
    public JComboBox<String> getComboFormato() { return comboFormato; }
    public DefaultListModel<String> getModeloLista() { return modeloLista; }
    public JList<String> getListaArchivos() { return listaArchivos; }
    public JButton getBotonReimprimir() { return botonReimprimir; }
   
}