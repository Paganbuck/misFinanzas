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
        if (nombre == null || nombre.trim().isEmpty()) return;
        if (buscarCategoriaPorNombre(nombre) != null) return;
        categorias.add(new Categoria(nombre.trim(), tipo == null ? "VARIABLE" : tipo.trim().toUpperCase()));
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
            if (c.getNombre() != null && c.getNombre().trim().toLowerCase().equals(buscado)) return c;
        }
        return null;
    }

    // 2. Registrar movimiento
    public void agregarMovimiento(String tipo, double monto, LocalDate fecha, String descripcion, String nombreCategoria) {
        if (tipo == null || fecha == null) return;
        String tipoNorm = tipo.trim().toUpperCase();
        Categoria cat = buscarCategoriaPorNombre(nombreCategoria);
        if (cat == null) {
            // si no existe la categoría, crearla como VARIABLE por defecto
            String nombre = (nombreCategoria == null || nombreCategoria.trim().isEmpty()) ? "Sin categoría" : nombreCategoria.trim();
            cat = new Categoria(nombre, "VARIABLE");
            categorias.add(cat);
        }
        movimientos.add(new Movimiento(tipoNorm, monto, fecha, descripcion == null ? "" : descripcion, cat));
    }

    // 3. Listar movimientos de un mes
    public List<Movimiento> listarMovimientosDeMes(int anio, int mes) {
        List<Movimiento> salida = new ArrayList<>();
        for (Movimiento m : movimientos) {
            if (m.getFecha() != null && m.getFecha().getYear() == anio && m.getFecha().getMonthValue() == mes) {
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
            // espera que PresupuestoMensual tenga setMontoTotal(double)
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

    // Comparar gastos vs presupuesto (impresión)
    public void compararGastosConPresupuesto(int anio, int mes) {
        double gastos = calcularGastosMes(anio, mes);
        Double presupuesto = obtenerPresupuestoMes(anio, mes);

        System.out.println("=== Comparación de Gastos y Presupuesto ===");
        System.out.println("Mes: " + mes + "/" + anio);
        System.out.println("Gastos del mes: " + gastos);

        if (presupuesto == null) {
            System.out.println("No hay presupuesto configurado para este mes.");
            return;
        }

        System.out.println("Presupuesto del mes: " + presupuesto);

        if (gastos > presupuesto) {
            System.out.println("Te pasaste del presupuesto por: " + (gastos - presupuesto));
        } else if (gastos < presupuesto) {
            System.out.println("Estás por debajo del presupuesto por: " + (presupuesto - gastos));
        } else {
            System.out.println("Gastaste exactamente tu presupuesto.");
        }
    }

    // Calcula saldo acumulado hasta un mes (inclusive)
    public double calcularSaldoAcumuladoHasta(int anio, int mes) {
        double ingresos = 0.0;
        double gastos = 0.0;

        for (Movimiento m : movimientos) {
            if (m.getFecha() == null) continue;
            int ma = m.getFecha().getYear();
            int mm = m.getFecha().getMonthValue();

            // si la fecha del movimiento es anterior al año consultado, o mismo año y mes <= mes consultado
            if (ma < anio || (ma == anio && mm <= mes)) {
                if ("INGRESO".equalsIgnoreCase(m.getTipo())) ingresos += m.getMonto();
                if ("GASTO".equalsIgnoreCase(m.getTipo())) gastos += m.getMonto();
            }
        }
        return ingresos - gastos;
    }

    // Saldo acumulado hasta hoy
    public double calcularSaldoAcumuladoHastaHoy() {
        LocalDate hoy = LocalDate.now();
        return calcularSaldoAcumuladoHasta(hoy.getYear(), hoy.getMonthValue());
    }

}
