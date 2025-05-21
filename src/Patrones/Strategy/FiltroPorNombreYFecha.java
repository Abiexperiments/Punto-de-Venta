package Patrones.Strategy;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.text.SimpleDateFormat;
import java.util.Date;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class FiltroPorNombreYFecha implements FiltroArchivoStrategy {
    private String textoNombre;
    private String textoFecha;

    public FiltroPorNombreYFecha(String textoBusqueda) {
        textoBusqueda = textoBusqueda.toLowerCase().trim();

        // Intentar detectar si hay una fecha
        LocalDate fecha = null;
        try {
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            fecha = LocalDate.parse(textoBusqueda, formato);
            this.textoFecha = fecha.format(formato); // usamos textoFecha como string para contains
            this.textoNombre = ""; // no hay nombre si solo es fecha
        } catch (DateTimeParseException e) {
            this.textoFecha = ""; // no hay fecha válida
            this.textoNombre = textoBusqueda;
        }
    }

    @Override
    public boolean cumple(String nombreArchivo) {
        nombreArchivo = nombreArchivo.toLowerCase();

        boolean cumpleNombre = textoNombre.isEmpty() || nombreArchivo.contains(textoNombre);
        boolean cumpleFecha = textoFecha.isEmpty() || nombreArchivo.contains(textoFecha);

        return cumpleNombre && cumpleFecha;
    }
}
