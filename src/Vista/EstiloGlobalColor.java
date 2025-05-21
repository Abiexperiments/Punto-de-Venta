package Vista;

import java.awt.Color;

import javax.swing.JPanel;

public class EstiloGlobalColor {
    public static final Color FONDO_GENERAL = new Color(221, 200, 255);  // un morado pastel suave


    public static void aplicarEstilo(JPanel panel) {
        panel.setBackground(FONDO_GENERAL);
        // Puedes aplicar más estilos aquí
    }
}
