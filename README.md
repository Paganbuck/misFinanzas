misFinanzas

Proyecto en Java (consola) — FASE 1: Sistema de gestión de finanzas personales (FinpApp - versión simplificada)

Descripción

Aplicación de consola para registrar categorías, movimientos (ingresos/gastos) y ver resúmenes mensuales. Todo funciona en memoria (sin base de datos) según las pautas del PDF de la Fase 1.

Clases principales implementadas:

Main — punto de entrada.

MenuConsola — interfaz por consola (menú).

Categoria — representa una categoría (nombre, tipo).

Movimiento — representa un ingreso o gasto (tipo, monto, fecha, descripción, categoría).

PresupuestoMensual — presupuesto para un año/mes.

FinanzasService — lógica simple (almacena listas en memoria, cálculos).

Paquete usado en el código: misFinanzas.java

Requisitos

IntelliJ IDEA (recomendado) o cualquier editor/IDE que acepte Java

Git y cuenta en GitHub

Estructura del proyecto
misFinanzas/
 └─ src/
    └─ misFinanzas/
       └─ java/
           Main.java
           MenuConsola.java
           FinanzasService.java
           Categoria.java
           Movimiento.java
           PresupuestoMensual.java

Ejemplo mínimo de uso en la app (flujo)

Ejecutar Main.

Elegir 1 para “Registrar categoría” y crear, por ejemplo, Ocio tipo VARIABLE.

Elegir 2 para “Registrar movimiento”: tipo INGRESO monto 200000 categoría Ocio.

Registrar gasto: tipo GASTO monto 50000 categoría Ocio.

Elegir 4 “Ver resumen del mes”: verás ingresos, gastos y saldo.
