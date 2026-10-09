
package com.accenture.qa.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AbrirSiteTest {

    @Test
    void deveAbrirSiteDemoQA() {

        WebDriverManager.chromedriver().setup();

        WebDriver driver = new ChromeDriver();

        try {
            driver.get("https://demoqa.com/");

            String titulo = driver.getTitle();

            System.out.println("Título da página: " + titulo);

            assertTrue(
    titulo.contains("demosite"),
    "O título da página deve conter demosite"
);

        } finally {
            driver.quit();
        }
    }
}