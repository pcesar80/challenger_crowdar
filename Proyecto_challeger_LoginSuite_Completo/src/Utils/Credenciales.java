package Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class Credenciales {

    public static Map<String, String> obtenerCredenciales() {

        Map<String, String> datos = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader("datos/credenciales.txt"))) {

            String linea;

            while ((linea = br.readLine()) != null) {

                String[] partes = linea.split("=");

                if (partes.length == 2) {
                    datos.put(partes[0].trim(), partes[1].trim());
                }
            }

        } catch (Exception e) {
            System.out.println("⚠️ Error leyendo credenciales: " + e.getMessage());
        }

        return datos;
    }
}
