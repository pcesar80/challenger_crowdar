package seccion;

import Utils.Evidencias;
import Utils.ReporteEjecucion;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.nio.file.*;
import java.time.Duration;
import java.util.List;

public class Login implements ModuloEjecucion {

    @Override
    public String getNombre() {
        return "Login Suite";
    }

    @Override
    public int ejecutar(WebDriver driver) {

        int errores = 0;

        try {

            List<String> usuarios =
                    Files.readAllLines(Paths.get("datos/usuarios_login.txt"));

            // Mensaje esperado de usuario bloqueado (externalizado)
            String mensajeLocked =
                    Files.readAllLines(Paths.get("datos/datos_pruebas.txt"))
                            .stream()
                            .filter(l -> l.startsWith("mensaje_locked_user="))
                            .findFirst()
                            .orElse("")
                            .replace("mensaje_locked_user=", "")
                            .trim();

            for (String linea : usuarios) {

                if (linea.trim().isEmpty())
                    continue;

                String[] datos = linea.split(";");

                String usuario = datos[0].trim();
                String password = datos[1].trim();

                System.out.println("\n=================================");
                System.out.println("Probando usuario: " + usuario);
                System.out.println("=================================");

                driver.get("https://www.saucedemo.com/");

                String resultadoLogin = ejecutarLogin(
                        driver,
                        usuario,
                        password,
                        mensajeLocked
                );

                switch (resultadoLogin) {

                    case "OK":

                        ReporteEjecucion.registrarOK(
                                "Login - " + usuario
                        );

                        Producto producto = new Producto();
                        int errProd = producto.ejecutar(driver);

                        if (errProd == 0) {

                            ReporteEjecucion.registrarOK(
                                    "Producto - " + usuario
                            );

                        } else {

                            errores++;
                            ReporteEjecucion.registrarError(
                                    "Producto - " + usuario,
                                    "Errores al agregar productos"
                            );
                        }

                        logout(driver);
                        break;

                    case "LOCKED":

                        ReporteEjecucion.registrarOK(
                                "Login - " + usuario +
                                        " (Usuario bloqueado validado)"
                        );
                        break;

                    case "INVALID":

                        errores++;
                        ReporteEjecucion.registrarError(
                                "Login - " + usuario,
                                "Credenciales inválidas"
                        );
                        break;

                    default:

                        errores++;
                        ReporteEjecucion.registrarError(
                                "Login - " + usuario,
                                "Resultado de login desconocido"
                        );
                        break;
                }
            }

        } catch (Exception e) {

            errores++;
            e.printStackTrace();
        }

        return errores;
    }

    private String ejecutarLogin(
            WebDriver driver,
            String usuario,
            String password,
            String mensajeLockedEsperado) {

        try {

            WebDriverWait wait =
                    new WebDriverWait(driver, Duration.ofSeconds(5));

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")))
                    .sendKeys(usuario);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")))
                    .sendKeys(password);

            wait.until(ExpectedConditions.elementToBeClickable(By.id("login-button")))
                    .click();

            try {

                WebElement error =
                        new WebDriverWait(driver, Duration.ofSeconds(3))
                                .until(ExpectedConditions.visibilityOfElementLocated(
                                        By.cssSelector("h3[data-test='error']")
                                ));

                String mensajeError = error.getText();

                // 🔴 Usuario bloqueado
                if (mensajeError.equals(mensajeLockedEsperado)
                        && usuario.equals("locked_out_user")) {

                    System.out.println("✅ Usuario bloqueado validado correctamente");
                    return "LOCKED";
                }

                // 🔴 Credenciales inválidas
                Evidencias.capturarPantalla(driver, "Login_ERROR_" + usuario);

                System.out.println("❌ Error login: " + mensajeError);

                return "INVALID";

            } catch (TimeoutException e) {

                // Si NO hay error → login exitoso
                return "OK";
            }

        } catch (Exception e) {

            System.out.println("=================================");
            System.out.println("EXCEPCION EN LOGIN");
            System.out.println("Usuario: " + usuario);
            System.out.println("Tipo: " + e.getClass().getName());
            System.out.println("Mensaje: " + e.getMessage());
            System.out.println("=================================");

            Evidencias.capturarPantalla(driver,
                    "Login_EXCEPTION_" + usuario);

            return "INVALID";
        }
    }

    private void logout(WebDriver driver) {

        try {

            WebDriverWait wait =
                    new WebDriverWait(driver, Duration.ofSeconds(3));

            wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("react-burger-menu-btn")
            )).click();

            wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("logout_sidebar_link")
            )).click();

        } catch (Exception e) {

            System.out.println("⚠️ No se pudo realizar logout");
        }
    }
}
