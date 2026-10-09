
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

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.JavascriptExecutor;

public class WebTablesTest {

    private WebDriver driver;
    private WebDriverWait wait;

    private final By botaoAdd = By.id("addNewRecordButton");

    @BeforeEach
    public void configurar() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @Test
    public void deveCriarEditarEExcluirRegistro() {
        driver.get("https://demoqa.com/webtables");

        wait.until(d -> d.findElement(botaoAdd).isDisplayed());

        // 1. Criar registro
        driver.findElement(botaoAdd).click();

        wait.until(d -> d.findElement(By.id("firstName")).isDisplayed());

        driver.findElement(By.id("firstName")).sendKeys("Lucas");
        driver.findElement(By.id("lastName")).sendKeys("Silva");
        driver.findElement(By.id("userEmail"))
                .sendKeys("lucas.silva.qa@example.com");
        driver.findElement(By.id("age")).sendKeys("30");
        driver.findElement(By.id("salary")).sendKeys("5000");
        driver.findElement(By.id("department")).sendKeys("QA");

        driver.findElement(By.id("submit")).click();

        By registroLucas = By.xpath("//*[normalize-space()='Lucas']");

        wait.until(d -> !d.findElements(registroLucas).isEmpty());

        assertTrue(!driver.findElements(registroLucas).isEmpty(),
                "O registro Lucas deveria ter sido criado.");

        System.out.println("Registro criado com sucesso.");

        // 2. Identificar o botão Edit relacionado ao registro
        WebElement nomeLucas = driver.findElement(registroLucas);

        WebElement botaoEditar = nomeLucas.findElement(
                By.xpath("./following::span[@title='Edit'][1]")
        );

        ((JavascriptExecutor) driver).executeScript(
        "arguments[0].scrollIntoView({block: 'center'});",
        botaoEditar
);

wait.until(d -> botaoEditar.isDisplayed());

((JavascriptExecutor) driver).executeScript(
        "window.scrollTo(0, 0);"
);

((JavascriptExecutor) driver).executeScript(
        "arguments[0].click();",
        botaoEditar
);

        wait.until(d -> d.findElement(By.id("firstName")).isDisplayed());

        WebElement campoNome = driver.findElement(By.id("firstName"));
        campoNome.clear();
        campoNome.sendKeys("Lucas QA");

        driver.findElement(By.id("submit")).click();

        By registroEditado = By.xpath(
                "//*[normalize-space()='Lucas QA']"
        );

        wait.until(d -> !d.findElements(registroEditado).isEmpty());

        assertTrue(!driver.findElements(registroEditado).isEmpty(),
                "O registro deveria ter sido editado.");

        System.out.println("Registro editado com sucesso.");

        // 3. Excluir registro
        WebElement nomeLucasQA = driver.findElement(registroEditado);

        WebElement botaoExcluir = nomeLucasQA.findElement(
                By.xpath("./following::span[@title='Delete'][1]")
        );

        ((JavascriptExecutor) driver).executeScript(
        "arguments[0].scrollIntoView({block: 'center'});",
        botaoExcluir
);

wait.until(d -> botaoExcluir.isDisplayed());

((JavascriptExecutor) driver).executeScript(
        "window.scrollTo(0, 0);"
);

((JavascriptExecutor) driver).executeScript(
        "arguments[0].click();",
        botaoExcluir
);

        wait.until(d -> d.findElements(registroEditado).isEmpty());

        assertTrue(driver.findElements(registroEditado).isEmpty(),
                "O registro deveria ter sido excluído.");

        System.out.println("Registro excluído com sucesso.");
    }

    @AfterEach
    public void finalizar() {
        if (driver != null) {
            driver.quit();
        }
    }
}