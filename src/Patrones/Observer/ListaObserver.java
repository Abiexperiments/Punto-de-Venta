package Patrones.Observer;
import java.util.*;

import Modelo.ProductosMenu;

public class ListaObserver {
    private static final ListaObserver instancia = new ListaObserver();

    private List<ObserverMenu> observadores = new ArrayList<>();
    private Map<String, List<ProductosMenu>> productosPorCategoria = new HashMap<>();

    private ListaObserver() {}

    public static ListaObserver getInstancia() {
        return instancia;
    }

    public void agregarObservador(ObserverMenu o) {
        observadores.add(o);
    }

    public void quitarObservador(ObserverMenu o) {
        observadores.remove(o);
    }

    public void setProductosPorCategoria(Map<String, List<ProductosMenu>> nuevosProductos) {
        this.productosPorCategoria = nuevosProductos;
        notificarObservadores();
    }

    public Map<String, List<ProductosMenu>> getProductosPorCategoria() {
        return productosPorCategoria;
    }

    private void notificarObservadores() {
        for (ObserverMenu o : observadores) {
            o.onMenuActualizado(productosPorCategoria);
        }
    }
}
