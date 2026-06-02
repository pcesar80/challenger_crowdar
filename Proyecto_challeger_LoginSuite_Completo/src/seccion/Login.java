
package seccion;

import Utils.Evidencias;
import Utils.ReporteEjecucion;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.nio.file.*;
import java.time.Duration;
import java.util.List;

public class Login implements ModuloEjecucion {

    private boolean lockedUserValidado = false;

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

            for (String linea : usuarios) {

                if (linea.trim().isEmpty())
                    continue;

                String[] datos = linea.split(";");

                String usuario = datos[0].trim();
                String password = datos[1].trim();

                // Reiniciar estado para cada usuario
                lockedUserValidado = false;

                System.out.println("\n=================================");
                System.out.println("Probando usuario: " + usuario);
                System.out.println("=================================");

                driver.get("https://www.saucedemo.com/");

                boolean loginOk = ejecutarLogin(
                        driver,
                        usuario,
                        password
                );

                if (loginOk) {

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

                        ReporteEjecucion.registrarError(
                                "Producto - " + usuario,
                                "Errores al agregar productos"
                        );
                    }

                    logout(driver);

                } else {

                    if (usuario.equals("locked_out_user")) {

                        if (lockedUserValidado) {

                            ReporteEjecucion.registrarOK(
                                    "Login - " + usuario +
                                            " (Usuario bloqueado validado)"
                            );

                        } else {

                            errores++;

                            ReporteEjecucion.registrarError(
                                    "Login - " + usuario,
                                    "Mensaje de usuario bloqueado incorrecto"
                            );
                        }

                    } else {

                        errores++;

                        ReporteEjecucion.registrarError(
                                "Login - " + usuario,
                                "Login inválido"
                        );
                    }
                }
            }


        } catch (Exception e) {

            errores++;
            e.printStackTrace();
        }

        return errores;
    }

    private boolean ejecutarLogin(
            WebDriver driver,
            String usuario,
            String password) {

        try {

            WebDriverWait wait =
                    new WebDriverWait(driver, Duration.ofSeconds(3));

            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.id("user-name")
                    )
            ).sendKeys(usuario);

            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.id("password")
                    )
            ).sendKeys(password);

            wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.id("login-button")
                    )
            ).click();

            try {

                WebElement error =
                        new WebDriverWait(driver, Duration.ofSeconds(3))
                                .until(
                                        ExpectedConditions.visibilityOfElementLocated(
                                                By.cssSelector("h3[data-test='error']")
                                        )
                                );

                String mensajeError = error.getText();

                String mensajeEsperado =
                        Files.readAllLines(
                                        Paths.get("datos/datos_pruebas.txt"))
                                .stream()
                                .filter(l -> l.startsWith("mensaje_locked_user="))
                                .findFirst()
                                .orElse("")
                                .replace("mensaje_locked_user=", "")
                                .trim();

                if (usuario.equals("locked_out_user")
                        && mensajeError.equals(mensajeEsperado)) {

                    lockedUserValidado = true;

                    System.out.println(
                            "✅ Usuario bloqueado validado correctamente"
                    );

                    return false;
                }

                lockedUserValidado = false;

                Evidencias.capturarPantalla(
                        driver,
                        "Login_ERROR_" + usuario
                );

                System.out.println(
                        "❌ " + mensajeError
                );

                return false;

            } catch(TimeoutException e){

                return true;
            }

        } catch(Exception e){

            System.out.println("=================================");
            System.out.println("EXCEPCION EN LOGIN");
            System.out.println("Usuario: " + usuario);
            System.out.println("Tipo: " + e.getClass().getName());
            System.out.println("Mensaje: " + e.getMessage());
            System.out.println("=================================");

            e.printStackTrace();

            Evidencias.capturarPantalla(driver,
                    "Login_EXCEPTION_" + usuario);

            return false;
        }
    }

    private void logout(WebDriver driver) {

        try {

            WebDriverWait wait =
                    new WebDriverWait(driver, Duration.ofSeconds(3));

            wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.id("react-burger-menu-btn")
                    )
            ).click();

            wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.id("logout_sidebar_link")
                    )
            ).click();

        } catch (Exception e) {

            System.out.println(
                    "⚠️ No se pudo realizar logout"
            );
        }
    }
}