package Vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DialogoDevolucion extends JDialog {
    private boolean confirmado = false;

    private JTextField txtNombreProducto;
    private JSpinner spinnerCantidad;
    private JComboBox<String> comboMotivo;
    private JButton btnConfirmar, btnCancelar;

    private int cantidadDevuelta;
    private String motivo;

    public DialogoDevolucion(String nombreProducto, int cantidadDisponible) {
        setTitle("Devolución de Producto");
        setModal(true);
        setSize(350, 250);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel principal
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Nombre producto (no editable)
        panel.add(new JLabel("Producto:"));
        txtNombreProducto = new JTextField(nombreProducto);
        txtNombreProducto.setEditable(false);
        panel.add(txtNombreProducto);

        // Cantidad a devolver (con spinner)
        panel.add(new JLabel("Cantidad a devolver:"));
        spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, cantidadDisponible, 1));
        panel.add(spinnerCantidad);

        // Motivo (combo)
        panel.add(new JLabel("Motivo:"));
        comboMotivo = new JComboBox<>(new String[] {
            "Caducado", "Dañado", "Error de compra", "Sobrante", "Otro"
        });
        panel.add(comboMotivo);

        // Botones
        JPanel panelBotones = new JPanel();
        btnConfirmar = new JButton("Confirmar");
        btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnConfirmar);
        panelBotones.add(btnCancelar);

        // Eventos
        btnConfirmar.addActionListener(e -> {
            cantidadDevuelta = (Integer) spinnerCantidad.getValue();
            motivo = comboMotivo.getSelectedItem().toString();
            confirmado = true;
            dispose();
        });

        btnCancelar.addActionListener(e -> {
            confirmado = false;
            dispose();
        });

        // Agregar todo al diálogo
        add(panel, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    // Getters para recuperar los datos
    public boolean isConfirmado() {
        return confirmado;
    }

    public int getCantidad() {
        return cantidadDevuelta;
    }

    public String getMotivo() {
        return motivo;
    }
}