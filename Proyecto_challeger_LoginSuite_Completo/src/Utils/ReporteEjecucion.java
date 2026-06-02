package Utils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReporteEjecucion {

    private static List<String> errores = new ArrayList<>();
    private static List<String> modulos = new ArrayList<>();

    private static int ok = 0;

    // ===============================
    // REGISTROS
    // ===============================
    public static void registrarOK(String modulo) {
        ok++;
        modulos.add("✔ " + modulo + " → OK");
    }

    public static void registrarError(String modulo, String mensaje) {
        errores.add("❌ " + modulo + " → " + mensaje);
        modulos.add("❌ " + modulo + " → ERROR");
    }

    // ===============================
    // REPORTE FINAL
    // ===============================
    public static void generarReporteFinal() {

        System.out.println("\n📄 Generando reporte final...\n");

        String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());

        System.out.println("✔ Total OK: " + ok);
        System.out.println("❌ Total errores: " + errores.size());

        try {

            File carpeta = new File("reportes");

            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            String nombreArchivo =
                    new SimpleDateFormat("yyyyMMdd_HHmmss")
                            .format(new Date());

            File archivo =
                    new File(carpeta,
                            "Reporte_" + nombreArchivo + ".txt");

            BufferedWriter writer =
                    new BufferedWriter(
                            new FileWriter(archivo));

            // ===============================
            // CABECERA
            // ===============================
            writer.write("📊 RESULTADO DE EJECUCIÓN QA\n");
            writer.write("====================================\n");
            writer.write("🕒 Fecha: " + fecha + "\n\n");

            // ===============================
            // RESUMEN
            // ===============================
            writer.write("📌 RESUMEN\n");
            writer.write("------------------------------------\n");
            writer.write("✔ Total OK: " + ok + "\n");
            writer.write("❌ Total errores: " + errores.size() + "\n\n");

            // ===============================
            // RESULTADO POR MÓDULO
            // ===============================
            writer.write("🧩 RESULTADO POR MÓDULO\n");
            writer.write("------------------------------------\n");

            for (String m : modulos) {
                writer.write(m + "\n");
            }

            writer.write("\n");

            // ===============================
            // DETALLE DE ERRORES
            // ===============================
            writer.write("❌ DETALLE DE ERRORES\n");
            writer.write("------------------------------------\n");

            if (errores.isEmpty()) {

                writer.write("Sin errores\n");

            } else {

                for (String e : errores) {
                    writer.write(e + "\n");
                }
            }

            writer.close();

            System.out.println(
                    "\n📁 Reporte generado en: "
                            + archivo.getAbsolutePath());

        } catch (Exception e) {

            System.out.println(
                    "❌ Error al generar archivo de reporte");

            e.printStackTrace();
        }

        System.out.println("\n📄 Fin del reporte\n");
    }
}