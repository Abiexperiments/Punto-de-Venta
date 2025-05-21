package Vista;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.*;
import javax.swing.*;
import java.awt.*;

public class VistaLogin extends JFrame {
    public JTextField txtUsuario;
    public JPasswordField txtContraseña;
    public JButton btnIngresar;

    public VistaLogin() {
        setTitle("Inicio de Sesión - Punto de Venta");
        setSize(400, 350); // un poco más alto para la imagen
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel lblTitulo = new JLabel("La Cocina de Raquel", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(0x333333));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblTitulo, gbc);

        // Imagen
        ImageIcon icon = new ImageIcon(getClass().getResource("/imagenes/logo.png")); 
        // Escalar imagen a 150x100 px (opcional)
        Image img = icon.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
        JLabel lblImagen = new JLabel(new ImageIcon(img));
        gbc.gridy = 1;
        panel.add(lblImagen, gbc);

        // Etiqueta usuario
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Usuario:"), gbc);

        // Campo usuario
        txtUsuario = new JTextField();
        txtUsuario.setPreferredSize(new Dimension(200, 25));
        txtUsuario.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        gbc.gridx = 1;
        panel.add(txtUsuario, gbc);

        // Etiqueta contraseña
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Contraseña:"), gbc);

        // Campo contraseña
        txtContraseña = new JPasswordField();
        txtContraseña.setEchoChar('●');
        txtContraseña.setPreferredSize(new Dimension(200, 25));
        txtContraseña.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        gbc.gridx = 1;
        panel.add(txtContraseña, gbc);

        // Botón ingresar
        btnIngresar = new JButton("Ingresar");
        btnIngresar.setBackground(new Color(0x007BFF));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(btnIngresar, gbc);

        // Hacer que ENTER active el botón ingresar
        getRootPane().setDefaultButton(btnIngresar);
    }
}
