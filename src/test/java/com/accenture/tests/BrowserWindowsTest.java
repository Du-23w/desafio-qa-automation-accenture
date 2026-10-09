
package com.accenture.qa.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BrowserWindowsTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void configurar() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void deveAbrirNovaJanelaValidarTextoEFechar() {
        driver.get("https://demoqa.com/browser-windows");

        String janelaOriginal = driver.getWindowHandle();

        driver.findElement(By.id("windowButton")).click();

        wait.until(d -> d.getWindowHandles().size() == 2);

        String novaJanela = driver.getWindowHandles()
                .stream()
                .filter(handle -> !handle.equals(janelaOriginal))
                .findFirst()
                .orElseThrow(() ->
                        new AssertionError("A nova janela não foi aberta."));

        driver.switchTo().window(novaJanela);

        String texto = wait.until(d ->
                d.findElement(By.id("sampleHeading")).getText());

        assertEquals("This is a sample page", texto);
        System.out.println("Texto da nova janela validado com sucesso.");

        driver.close();

        driver.switchTo().window(janelaOriginal);

        assertTrue(driver.getWindowHandle().equals(janelaOriginal),
                "O navegador não retornou à janela original.");

        System.out.println("Nova janela fechada e janela original recuperada.");
    }

    @AfterEach
    public void finalizar() {
        if (driver != null) {
            driver.quit();
        }
    }
}