package seccion;

import Utils.ReporteEjecucion;
import org.openqa.selenium.WebDriver;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MercadoLibreAPI implements ModuloEjecucion {

    @Override
    public String getNombre() {
        return "MercadoLibre API";
    }

    @Override
    public int ejecutar(WebDriver driver) {

        int errores = 0;

        try {

            System.out.println("=================================");
            System.out.println("Ejecutando prueba API MercadoLibre");
            System.out.println("=================================");

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://www.mercadolibre.com.ar/menu/departments"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            // Validar status HTTP
            if (response.statusCode() != 200) {

                errores++;

                ReporteEjecucion.registrarError(
                        "MercadoLibre API",
                        "Status Code obtenido: " + response.statusCode()
                );

                System.out.println(
                        "❌ Status Code inválido: " + response.statusCode()
                );

                return errores;
            }

            String body = response.body();

            if (body == null || body.isEmpty()) {

                errores++;

                ReporteEjecucion.registrarError(
                        "MercadoLibre API",
                        "Respuesta vacía"
                );

                System.out.println("❌ Respuesta vacía");

                return errores;
            }

            // Contar departamentos
            Pattern pattern = Pattern.compile("\"name\"");
            Matcher matcher = pattern.matcher(body);

            int cantidadDepartamentos = 0;

            while (matcher.find()) {
                cantidadDepartamentos++;
            }

            System.out.println(
                    "📦 Cantidad de departamentos encontrados: "
                            + cantidadDepartamentos
            );

            if (cantidadDepartamentos > 0) {

                ReporteEjecucion.registrarOK(
                        "MercadoLibre API (" +
                                cantidadDepartamentos +
                                " departamentos)"
                );

                System.out.println(
                        "✅ Validación exitosa"
                );

            } else {

                errores++;

                ReporteEjecucion.registrarError(
                        "MercadoLibre API",
                        "No se encontraron departamentos"
                );

                System.out.println(
                        "❌ No se encontraron departamentos"
                );
            }

        } catch (Exception e) {

            errores++;

            ReporteEjecucion.registrarError(
                    "MercadoLibre API",
                    e.getMessage()
            );

            System.out.println(
                    "❌ Error en MercadoLibre API: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }

        return errores;
    }
}