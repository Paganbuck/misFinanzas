package misFinanzas.java;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FinanzasService {

    private final List<Categoria> categorias = new ArrayList<>();
    private final List<Movimiento> movimientos = new ArrayList<>();
    private final List<PresupuestoMensual> presupuestos = new ArrayList<>();

    // 1. Registrar categoría
    public void agregarCategoria(String nombre, String tipo) {
        if (buscarCategoriaPorNombre(nombre) != null) return;
        categorias.add(new Categoria(nombre.trim(), tipo.trim().toUpperCase()));
    }

    // listar categorías
    public List<Categoria> listarCategorias() {
        return new ArrayList<>(categorias);
    }

    // buscar categoria por nombre (ignora mayúsc/minúsc y espacios)
    public Categoria buscarCategoriaPorNombre(String nombre) {
        if (nombre == null) return null;
        String buscado = nombre.trim().toLowerCase();
        for (Categoria c : categorias) {
            if (c.getNombre().trim().toLowerCase().equals(buscado)) return c;
        }
        return null;
    }

    // 2. Registrar movimiento
    public void agregarMovimiento(String tipo, double monto, LocalDate fecha, String descripcion, String nombreCategoria) {
        Categoria cat = buscarCategoriaPorNombre(nombreCategoria);
        if (cat == null) {
            // si no existe la categoría, crearla como VARIABLE por defecto
            cat = new Categoria(nombreCategoria.trim(), "VARIABLE");
            categorias.add(cat);
        }
        movimientos.add(new Movimiento(tipo.trim().toUpperCase(), monto, fecha, descripcion, cat));
    }

    // 3. Listar movimientos de un mes
    public List<Movimiento> listarMovimientosDeMes(int anio, int mes) {
        List<Movimiento> salida = new ArrayList<>();
        for (Movimiento m : movimientos) {
            if (m.getFecha().getYear() == anio && m.getFecha().getMonthValue() == mes) {
                salida.add(m);
            }
        }
        return salida;
    }

    // calcular ingresos de un mes
    public double calcularIngresosMes(int anio, int mes) {
        double total = 0.0;
        for (Movimiento m : listarMovimientosDeMes(anio, mes)) {
            if ("INGRESO".equalsIgnoreCase(m.getTipo())) total += m.getMonto();
        }
        return total;
    }

    // calcular gastos de un mes
    public double calcularGastosMes(int anio, int mes) {
        double total = 0.0;
        for (Movimiento m : listarMovimientosDeMes(anio, mes)) {
            if ("GASTO".equalsIgnoreCase(m.getTipo())) total += m.getMonto();
        }
        return total;
    }

    // 4. Configurar presupuesto mensual
    public void configurarPresupuestoMensual(int anio, int mes, double montoTotal) {
        PresupuestoMensual existente = null;
        for (PresupuestoMensual p : presupuestos) {
            if (p.getAnio() == anio && p.getMes() == mes) { existente = p; break; }
        }
        if (existente != null) {
            existente.setMontoTotal(montoTotal);
        } else {
            presupuestos.add(new PresupuestoMensual(anio, mes, montoTotal));
        }
    }

    // obtener presupuesto (null si no existe)
    public Double obtenerPresupuestoMes(int anio, int mes) {
        for (PresupuestoMensual p : presupuestos) {
            if (p.getAnio() == anio && p.getMes() == mes) return p.getMontoTotal();
        }
        return null;
    }

    // 5. Imprimir resumen mensual (usa ingresos, gastos, presupuesto)
    public void imprimirResumenMes(int anio, int mes) {
        double ingresos = calcularIngresosMes(anio, mes);
        double gastos = calcularGastosMes(anio, mes);
        double saldo = ingresos - gastos;
        Double presupuesto = obtenerPresupuestoMes(anio, mes);

        System.out.println("Resumen del mes " + mes + "/" + anio);
        System.out.println("Ingresos: " + ingresos);
        System.out.println("Gastos: " + gastos);
        System.out.println("Saldo: " + saldo);
        if (presupuesto != null) {
            System.out.println("Presupuesto: " + presupuesto);
            if (gastos > presupuesto) System.out.println("Advertencia: gastos mayores al presupuesto!");
            else System.out.println("Dentro del presupuesto.");
        } else {
            System.out.println("No hay presupuesto configurado para este mes.");
        }
    }
}
