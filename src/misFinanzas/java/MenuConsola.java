package misFinanzas.java;

import java.time.LocalDate;
import java.util.Scanner;

public class MenuConsola {

    private FinanzasService service = new FinanzasService();
    private Scanner sc = new Scanner(System.in);

    private int leerEnteroSeguro() {
        while (!sc.hasNextInt()) {
            System.out.println("Número inválido. Intenta de nuevo: ");
            sc.next();
        }
        return sc.nextInt();
    }

    public void iniciar() {

        int opcion;

        do {
            System.out.println("=== Mis Finanzas ===");
            System.out.println("1. Registrar categoría");
            System.out.println("2. Registrar movimiento");
            System.out.println("3. Ver gasto de un mes");
            System.out.println("4. Ver resumen del mes");
            System.out.println("5. Listar categorías");
            System.out.println("0. Salir");
            System.out.print("Opción: ");

            opcion = leerEnteroSeguro();
            sc.nextLine();

            switch (opcion) {
                case 1 -> registrarCategoria();
                case 2 -> registrarMovimiento();
                case 3 -> verGastoMes();
                case 4 -> verResumenMes();
                case 5 -> listarCategorias();
            }

        } while (opcion != 0);
    }

    private void registrarCategoria() {
        System.out.print("Nombre categoría: ");
        String nombre = sc.nextLine();

        System.out.print("Tipo (FIJO/VARIABLE): ");
        String tipo = sc.nextLine();

        service.agregarCategoria(nombre, tipo);
        System.out.println("Categoría registrada.");
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


    private void registrarMovimiento() {
        System.out.print("Tipo (INGRESO/GASTO): ");
        String tipo = sc.nextLine();

        System.out.print("Monto: ");
        double monto = leerEnteroSeguro();
        sc.nextLine();

        System.out.print("Descripción: ");
        String descripcion = sc.nextLine();

        System.out.print("Categoría: ");
        String categoria = normalizar(sc.nextLine());


        System.out.print("Año: ");
        int anio = leerEnteroSeguro();

        System.out.print("Mes: ");
        int mes = leerEnteroSeguro();
        sc.nextLine();

        LocalDate fecha = LocalDate.of(anio, mes, 1);

        service.agregarMovimiento(tipo, monto, fecha, descripcion, categoria);
        System.out.println("Movimiento registrado.");
    }

    private void verGastoMes() {
        System.out.print("Año: ");
        int anio = leerEnteroSeguro();

        System.out.print("Mes: ");
        int mes = leerEnteroSeguro();
        sc.nextLine();

        double total = service.calcularGastosMes(anio, mes);
        System.out.println("Gasto total: " + total);
    }

    private void verResumenMes() {
        System.out.print("Año: ");
        int anio = leerEnteroSeguro();

        System.out.print("Mes: ");
        int mes = leerEnteroSeguro();
        sc.nextLine();

        service.imprimirResumenMes(anio, mes);
    }

    private void listarCategorias() {
        System.out.println("Categorías registradas:");
        for (Categoria c : service.listarCategorias()) {
            System.out.println("- " + c.getNombre() + " (" + c.getTipo() + ")");
        }
    }
}
