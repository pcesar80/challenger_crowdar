package Utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Evidencias {

    private static final String BASE_PATH_OK = "evidencias OK";
    private static final String BASE_PATH_NOK = "evidencias NOK";

    public static void capturarPantalla(WebDriver driver, String nombreEscenario) {

        try {
            // 🔍 Determinar tipo (OK / NOK)
            String basePath;

            if (nombreEscenario.startsWith("OK")) {
                basePath = BASE_PATH_OK;
            } else if (nombreEscenario.startsWith("NOK")) {
                basePath = BASE_PATH_NOK;
            } else {
                basePath = "evidencias"; // fallback por si no viene con prefijo
            }

            // 📅 Carpeta por fecha
            String fecha = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            Path ruta = Paths.get(basePath, fecha);

            // Crear carpeta si no existe
            if (!Files.exists(ruta)) {
                Files.createDirectories(ruta);
            }

            // ⏱ Timestamp
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("HHmmss"));

            String nombreArchivo = nombreEscenario + "_" + timestamp + ".png";
            Path destino = ruta.resolve(nombreArchivo);

            // 📸 Captura
            File src = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.FILE);

            Files.copy(src.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("📸 Evidencia capturada: " + destino.toAbsolutePath());

        } catch (IOException e) {
            System.out.println("⚠️ No se pudo guardar la evidencia: " + e.getMessage());
        }
    }
}