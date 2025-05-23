package Controlador;
import java.awt.EventQueue;
 
import Controlador.ControladorVistaPrincipal;
import java.awt.EventQueue;
import Vista.VistaLogin;
import Controlador.ControladorLogin;

public class PuntoDeVentaMVC {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
            	System.out.println("prueba");
                VistaLogin vistaLogin = new VistaLogin();
                vistaLogin.setVisible(true);
                new ControladorLogin(vistaLogin);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }}