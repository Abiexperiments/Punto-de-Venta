package Controlador;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import Modelo.BaseDatos;
import Modelo.SesionUsuario;
import Modelo.Usuario;
import Vista.VistaLogin;
import Vista.VistaPrincipal;

public class ControladorLogin {
    private VistaLogin vista;
    

    public ControladorLogin(VistaLogin vista) {
        this.vista = vista;

        this.vista.btnIngresar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                verificarLogin();
            }
        });
    }

    private void verificarLogin() {
        String usuario = vista.txtUsuario.getText();
        String contraseña = new String(vista.txtContraseña.getPassword());

        BaseDatos usuarioDAO = new BaseDatos();
        Usuario user = usuarioDAO.verificarUsuario(usuario, contraseña);

        if (user != null) {
        	SesionUsuario.setUsuarioActual(user); // ← guardar usuario actual en sesión

            JOptionPane.showMessageDialog(null, "Bienvenido " + user.getUsuario() + " (" + user.getRol() + ")");

            vista.dispose();  // cierra login
            VistaPrincipal vistaPrincipal = new VistaPrincipal();
            vistaPrincipal.setVisible(true);

            new ControladorVistaPrincipal(vistaPrincipal, user.getUsuario(), user.getRol());
        } else {
            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
        }
    }
}