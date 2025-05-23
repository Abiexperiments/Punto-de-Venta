package Vista;

import Modelo.ProductosMenu;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;

public class ProductoMenuRenderer extends JPanel implements ListCellRenderer<ProductosMenu> {
    private JLabel lblImagen = new JLabel();
    private JLabel lblTexto = new JLabel();
    
    public ProductoMenuRenderer() {
        setLayout(new BorderLayout(10, 5));
        add(lblImagen, BorderLayout.WEST);
        add(lblTexto, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }
    
    @Override
    public Component getListCellRendererComponent(JList<? extends ProductosMenu> list, ProductosMenu producto,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        if (producto == null) {
            lblTexto.setText("<html><b>Producto inválido</b></html>");
            lblImagen.setIcon(null);
            return this;
        }

        String nombre = (producto.getNombre() != null) ? producto.getNombre() : "Sin nombre";
        double precio = producto.getPrecio(); // Asumiendo double, no Double

        lblTexto.setText("<html><b>" + nombre + "</b><br>$" + String.format("%.2f", precio) + "</html>");
        lblTexto.setVerticalAlignment(SwingConstants.CENTER);

        String ruta = producto.getRutaImagen();
        ImageIcon icono = null;

        if (ruta != null && !ruta.isEmpty()) {
            File archivo = new File(ruta);
            if (archivo.exists()) {
                icono = new ImageIcon(ruta);
            } else {
                URL url = getClass().getClassLoader().getResource(ruta);
                if (url != null) {
                    icono = new ImageIcon(url);
                }
            }
        }

        if (icono == null) {
            try {
                URL urlDefault = getClass().getClassLoader().getResource("Imagenes/noimagen.jpg");
                if (urlDefault != null) {
                    icono = new ImageIcon(urlDefault);
                }
            } catch (Exception e) {
                System.err.println("Error cargando imagen por defecto: " + e.getMessage());
            }
        }

        if (icono != null) {
            Image imagenEscalada = icono.getImage().getScaledInstance(100, 90, Image.SCALE_SMOOTH);
            lblImagen.setIcon(new ImageIcon(imagenEscalada));
        } else {
            lblImagen.setIcon(null);
        }

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setOpaque(true);

        return this;
    }
}