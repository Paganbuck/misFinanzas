package misFinanzas.java;

public class Categoria {

    private String nombre;
    private String tipo; // FIJO o VARIABLE

    public Categoria(String nombre, String tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }

    @Override
    public String toString() {
        return nombre + " (" + tipo + ")";
    }

}
