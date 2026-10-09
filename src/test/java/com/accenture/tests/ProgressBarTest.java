
package com.accenture.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProgressBarTest {

    private WebDriver driver;
    private WebDriverWait wait;

    private final By barra = By.id("progressBar");
    private final By botaoStartStop = By.id("startStopButton");
    private final By botaoReset = By.id("resetButton");

    @BeforeEach
    public void configurar() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @Test
    public void deveControlarEResetarProgressBar() {

        driver.get("https://demoqa.com/progress-bar");

        // 1. Inicia a barra
        wait.until(d -> d.findElement(botaoStartStop).isDisplayed());
        driver.findElement(botaoStartStop).click();

        // 2. Aguarda chegar a pelo menos 20%
        wait.until(d -> {
            String valor = d.findElement(barra)
                    .getText()
                    .replace("%", "")
                    .trim();

            return !valor.isEmpty()
                    && Integer.parseInt(valor) >= 20;
        });

        // 3. Para a barra
        driver.findElement(botaoStartStop).click();

        int percentualParado = Integer.parseInt(
                driver.findElement(barra)
                        .getText()
                        .replace("%", "")
                        .trim()
        );

        System.out.println(
                "Percentual após parar: " + percentualParado + "%"
        );

        assertTrue(
                percentualParado <= 25,
                "A barra deveria parar em 25% ou menos."
        );

        // 4. Retoma a barra
        driver.findElement(botaoStartStop).click();

        // 5. Aguarda atingir 100%
        wait.until(d ->
                d.findElement(barra).getText().trim().equals("100%")
        );

        assertEquals(
                "100%",
                driver.findElement(barra).getText().trim()
        );

        System.out.println("Barra chegou a 100%.");

        // 6. Aguarda o botão Reset aparecer e clica nele
        WebElement reset = wait.until(d -> {
            ListHelper resultado = new ListHelper(
                    d.findElements(botaoReset)
            );
            return resultado.firstDisplayed();
        });

        reset.click();

        // 7. Confirma que a barra voltou a zero
        wait.until(d ->
                d.findElement(barra).getText().trim().equals("0%")
        );

        assertEquals(
                "0%",
                driver.findElement(barra).getText().trim()
        );

        System.out.println("Barra resetada para 0%.");
    }

    private static class ListHelper {
        private final java.util.List<WebElement> elementos;

        private ListHelper(java.util.List<WebElement> elementos) {
            this.elementos = elementos;
        }

        private WebElement firstDisplayed() {
            for (WebElement elemento : elementos) {
                if (elemento.isDisplayed()) {
                    return elemento;
                }
            }
            return null;
        }
    }

    @AfterEach
    public void finalizar() {
        if (driver != null) {
            driver.quit();
        }
    }
}