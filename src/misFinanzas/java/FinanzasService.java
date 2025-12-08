package misFinanzas.java;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FinanzasService {

    private List<Categoria> categorias = new ArrayList<>();
    private List<Movimiento> movimientos = new ArrayList<>();
    private List<PresupuestoMensual> presupuestos = new ArrayList<>();

    public void agregarCategoria(String nombre, String tipo) {
        nombre = normalizar(nombre);
        tipo = normalizar(tipo);
        categorias.add(new Categoria(nombre, tipo));
    }


    public List<Categoria> listarCategorias() {
        return categorias;
    }

    public Categoria buscarCategoriaPorNombre(String nombre) {
        nombre = normalizar(nombre);
        for (Categoria c : categorias) {
            if (normalizar(c.getNombre()).equals(nombre)) {
                return c;
            }
        }
        return null;
    }

    public void agregarMovimiento(String tipo,
                                  double monto,
                                  LocalDate fecha,
                                  String descripcion,
                                  String nombreCategoria) {

        Categoria categoria = buscarCategoriaPorNombre(nombreCategoria);

        if (categoria == null) {
            System.out.println("Categoría no encontrada.");
            return;
        }

        movimientos.add(new Movimiento(tipo, monto, fecha, descripcion, categoria));
    }

    public double calcularGastosMes(int anio, int mes) {
        double total = 0;

        for (Movimiento m : movimientos) {
            if (m.getFecha().getYear() == anio &&
                    m.getFecha().getMonthValue() == mes &&
                    m.getTipo().equalsIgnoreCase("GASTO")) {
                total += m.getMonto();
            }
        }
        return total;
    }

    public double calcularIngresosMes(int anio, int mes) {
        double total = 0;

        for (Movimiento m : movimientos) {
            if (m.getFecha().getYear() == anio &&
                    m.getFecha().getMonthValue() == mes &&
                    m.getTipo().equalsIgnoreCase("INGRESO")) {
                total += m.getMonto();
            }
        }
        return total;
    }

    public void configurarPresupuestoMensual(int anio, int mes, double montoTotal) {

        PresupuestoMensual existente = null;

        for (PresupuestoMensual p : presupuestos) {
            if (p.getAnio() == anio && p.getMes() == mes) {
                existente = p;
                break;
            }
        }

        if (existente != null) {
            existente.setMontoTotal(montoTotal);
        } else {
            presupuestos.add(new PresupuestoMensual(anio, mes, montoTotal));
        }
    }

    public Double obtenerPresupuestoMes(int anio, int mes) {
        for (PresupuestoMensual p : presupuestos) {
            if (p.getAnio() == anio && p.getMes() == mes) {
                return p.getMontoTotal();
            }
        }
        return null;
    }

    public double totalIngresosAcumulados() {
        return movimientos.stream()
                .filter(m -> m.getTipo().equalsIgnoreCase("INGRESO"))
                .mapToDouble(m -> m.getMonto())
                .sum();
    }

    public double totalGastosAcumulados() {
        return movimientos.stream()
                .filter(m -> m.getTipo().equalsIgnoreCase("GASTO"))
                .mapToDouble(m -> m.getMonto())
                .sum();
    }

    public double saldoAcumulado() {
        return totalIngresosAcumulados() - totalGastosAcumulados();
    }

    public double calcularIngresosTotales() {
        return movimientos.stream()
                .filter(m -> m.getTipo().equalsIgnoreCase("INGRESO"))
                .mapToDouble(Movimiento::getMonto)
                .sum();
    }

    public double calcularGastosTotales() {
        return movimientos.stream()
                .filter(m -> m.getTipo().equalsIgnoreCase("GASTO"))
                .mapToDouble(Movimiento::getMonto)
                .sum();
    }

    public double calcularSaldoTotal() {
        return calcularIngresosTotales() - calcularGastosTotales();
    }

    public void imprimirResumenMes(int anio, int mes) {

        double ingresosMes = calcularIngresosMes(anio, mes);
        double gastosMes = calcularGastosMes(anio, mes);
        double saldoMes = ingresosMes - gastosMes;

        Double presupuesto = obtenerPresupuestoMes(anio, mes);

        System.out.println("Resumen del mes " + mes + "/" + anio);
        System.out.println("Ingresos del mes: " + ingresosMes);
        System.out.println("Gastos del mes: " + gastosMes);
        System.out.println("Saldo del mes: " + saldoMes);

        double saldoTotal = calcularSaldoTotal();
        System.out.println("Saldo acumulado total: " + saldoTotal);

        if (presupuesto != null) {
            System.out.println("Presupuesto: " + presupuesto);
            if (gastosMes > presupuesto) {
                System.out.println("Advertencia: Gastos mayores al presupuesto!");
            } else {
                System.out.println("Dentro del presupuesto.");
            }
        } else {
            System.out.println("No hay presupuesto configurado para este mes.");
        }
    }

    private String normalizar(String texto) {
        texto = texto.toLowerCase().trim();

        texto = texto
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u")
                .replace("ñ", "n");

        return texto;
    }

}
