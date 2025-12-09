package misFinanzas.java;

import java.time.LocalDate;

public class Movimiento {
    private String tipo; // "INGRESO" o "GASTO"
    private double monto;
    private LocalDate fecha;
    private String descripcion;
    private Categoria categoria;

    public Movimiento(String tipo, double monto, LocalDate fecha, String descripcion, Categoria categoria) {
        this.tipo = tipo;
        this.monto = monto;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.categoria = categoria;
    }

    public String getTipo() { return tipo; }
    public double getMonto() { return monto; }
    public LocalDate getFecha() { return fecha; }
    public String getDescripcion() { return descripcion; }
    public Categoria getCategoria() { return categoria; }
}
