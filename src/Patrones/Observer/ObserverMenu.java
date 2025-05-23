package Patrones.Observer;

import java.util.List;
import java.util.Map;

import Modelo.ProductosMenu;

public interface ObserverMenu {
    void onMenuActualizado(Map<String, List<ProductosMenu>> productosPorCategoria);
}