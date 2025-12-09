package misFinanzas.java;

import java.time.LocalDate;
import java.util.Scanner;

public class MenuConsola {

    private final FinanzasService service = new FinanzasService();
    private final Scanner sc = new Scanner(System.in);

    private int leerEnteroSeguro() {
        while (!sc.hasNextInt()) {
            System.out.println("Número inválido. Intenta de nuevo: ");
            sc.next();
        }
        int v = sc.nextInt();
        sc.nextLine();
        return v;
    }

    public void iniciar() {
        int opcion;
        do {
            System.out.println("=== Mis Finanzas ===");
            System.out.println("1. Registrar categoría");
            System.out.println("2. Registrar movimiento");
            System.out.println("3. Listar movimientos de un mes");
            System.out.println("4. Ver resumen del mes");
            System.out.println("5. Listar categorías");
            System.out.println("6. Configurar presupuesto mensual");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            opcion = leerEnteroSeguro();

            switch (opcion) {
                case 1 -> registrarCategoria();
                case 2 -> registrarMovimiento();
                case 3 -> listarMovimientosMes();
                case 4 -> verResumenMes();
                case 5 -> listarCategorias();
                case 6 -> configurarPresupuesto();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private void registrarCategoria() {
        System.out.print("Nombre categoría: ");
        String nombre = sc.nextLine();
        System.out.print("Tipo (FIJO/VARIABLE): ");
        String tipo = sc.nextLine();
        service.agregarCategoria(nombre, tipo);
        System.out.println("Categoría guardada.");
    }

    private void registrarMovimiento() {
        System.out.print("Tipo (INGRESO/GASTO): ");
        String tipo = sc.nextLine();
        System.out.print("Monto: ");
        double monto = 0;
        try { monto = Double.parseDouble(sc.nextLine()); } catch (NumberFormatException e) { System.out.println("Monto inválido."); return; }
        System.out.print("Descripción: ");
        String descripcion = sc.nextLine();
        System.out.print("Categoría: ");
        String categoria = sc.nextLine();
        System.out.print("Año (ej. 2025): ");
        int anio = leerEnteroSeguro();
        System.out.print("Mes (1-12): ");
        int mes = leerEnteroSeguro();
        LocalDate fecha;
        try { fecha = LocalDate.of(anio, mes, 1); } catch (Exception e) { System.out.println("Fecha inválida."); return; }

        service.agregarMovimiento(tipo, monto, fecha, descripcion, categoria);
        System.out.println("Movimiento guardado.");
    }

    private void listarMovimientosMes() {
        System.out.print("Año: ");
        int anio = leerEnteroSeguro();
        System.out.print("Mes: ");
        int mes = leerEnteroSeguro();
        var lista = service.listarMovimientosDeMes(anio, mes);
        if (lista.isEmpty()) System.out.println("No hay movimientos en ese mes.");
        else {
            for (Movimiento m : lista) {
                System.out.println(m.getFecha() + " - " + m.getTipo() + " - " + m.getMonto() + " - " + m.getCategoria().getNombre() + " - " + m.getDescripcion());
            }
        }
    }

    private void verResumenMes() {
        System.out.print("Año: ");
        int anio = leerEnteroSeguro();
        System.out.print("Mes: ");
        int mes = leerEnteroSeguro();
        service.imprimirResumenMes(anio, mes);
    }

    private void listarCategorias() {
        var cats = service.listarCategorias();
        if (cats.isEmpty()) System.out.println("No hay categorías registradas.");
        else {
            System.out.println("Categorías:");
            for (Categoria c : cats) System.out.println("- " + c.toString());
        }
    }

    private void configurarPresupuesto() {
        System.out.print("Año: ");
        int anio = leerEnteroSeguro();
        System.out.print("Mes: ");
        int mes = leerEnteroSeguro();
        System.out.print("Monto total del presupuesto: ");
        double monto = 0;
        try { monto = Double.parseDouble(sc.nextLine()); } catch (NumberFormatException e) { System.out.println("Monto inválido."); return; }
        service.configurarPresupuestoMensual(anio, mes, monto);
        System.out.println("Presupuesto guardado.");
    }
}

