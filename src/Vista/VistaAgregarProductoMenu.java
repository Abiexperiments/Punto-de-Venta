package Vista;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class VistaAgregarProductoMenu extends JPanel {
    
    private JTextField campoNombre;
    private JTextField campoPrecio;
    private JComboBox<String> comboCategoria;
    private JButton botonAgregar;
    private JLabel lblImagenPreview;
    private JButton btnCargarImagen;
    private String rutaImagenSeleccionada;

    
    public VistaAgregarProductoMenu() {
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Agregar Producto al Menú", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        add(titulo, BorderLayout.NORTH);

        JPanel formulario = new JPanel(new GridLayout(4, 2, 10, 10));
        formulario.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        formulario.add(new JLabel("Nombre del producto y Descripcion:"));
        campoNombre = new JTextField();
        formulario.add(campoNombre);

        formulario.add(new JLabel("Precio:"));
        campoPrecio = new JTextField();
        formulario.add(campoPrecio);

        formulario.add(new JLabel("Categoría:"));
        comboCategoria = new JComboBox<>(new String[]{"Platillo", "Bebida", "Postre"});
        formulario.add(comboCategoria);

        botonAgregar = new JButton("Agregar al menú");
        formulario.add(new JLabel()); // espacio
        formulario.add(botonAgregar);

        add(formulario, BorderLayout.CENTER);
        
        ImageIcon imagenPorDefecto = new ImageIcon(getClass().getResource("/Imagenes/noimagen.jpg"));
        Image imagenEscalada = imagenPorDefecto.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        lblImagenPreview = new JLabel(new ImageIcon(imagenEscalada));

        lblImagenPreview.setPreferredSize(new Dimension(100, 100));
        lblImagenPreview.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true)); // borde redondeado opcional


        btnCargarImagen = new JButton("Cargar Imagen");
        btnCargarImagen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	
                JFileChooser fileChooser = new JFileChooser("Imagenes"); // Ruta de inicio
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int resultado = fileChooser.showOpenDialog(null);
                

                if (resultado == JFileChooser.APPROVE_OPTION) {
                    File archivo = fileChooser.getSelectedFile();
                    rutaImagenSeleccionada = archivo.getPath();
                    ImageIcon icono = new ImageIcon(new ImageIcon(rutaImagenSeleccionada).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                    lblImagenPreview.setIcon(icono);
                }
            }
        });
        // Panel inferior para imagen y botón
        JPanel panelImagen = new JPanel(new FlowLayout());
        panelImagen.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelImagen.add(btnCargarImagen);
        panelImagen.add(lblImagenPreview);

        add(panelImagen, BorderLayout.SOUTH);
        EstiloGlobalColor.aplicarEstilo(this); // si extiende de JPanel

    }

    public String getNombre() {
        return campoNombre.getText();
    }

    public String getPrecio() {
        return campoPrecio.getText();
    }

    public String getCategoria() {
        return (String) comboCategoria.getSelectedItem();
    }

    public JButton getBotonAgregar() {
        return botonAgregar;
    }

    public void limpiarCampos() {
    	
    	    campoNombre.setText("");
    	    campoPrecio.setText("");
    	    comboCategoria.setSelectedIndex(0);
    	    rutaImagenSeleccionada = null;
    	    lblImagenPreview.setIcon(null);
    }

	public String getRutaImagenSeleccionada() {
		 return rutaImagenSeleccionada;
	}
}