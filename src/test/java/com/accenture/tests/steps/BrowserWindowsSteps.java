
package com.accenture.tests.steps;

import io.cucumber.java.After;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.E;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.Set;

public class BrowserWindowsSteps {

    private WebDriver driver;
    private WebDriverWait wait;
    private String janelaOriginal;

    @Dado("que acesso a página Browser Windows")
    public void acessarBrowserWindows() {
        WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        driver.manage().window().maximize();
        driver.get("https://demoqa.com/browser-windows");

        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("windowButton")
        ));
    }

    @Quando("clico no botão New Window")
    public void clicarNewWindow() {
        janelaOriginal = driver.getWindowHandle();

        WebElement botao = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.id("windowButton")
                )
        );

        botao.click();

        wait.until(d -> d.getWindowHandles().size() > 1);
    }

    @Então("a nova janela deve exibir a mensagem {string}")
    public void validarNovaJanela(String mensagemEsperada) {
        Set<String> janelas = driver.getWindowHandles();

        for (String janela : janelas) {
            if (!janela.equals(janelaOriginal)) {
                driver.switchTo().window(janela);
                break;
            }
        }

        WebElement texto = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("sampleHeading")
                )
        );

        Assertions.assertEquals(
                mensagemEsperada,
                texto.getText().trim(),
                "A mensagem da nova janela não corresponde ao esperado."
        );
    }

    @E("fecho a nova janela e retorno à janela original")
    public void fecharNovaJanela() {
        driver.close();
        driver.switchTo().window(janelaOriginal);

        Assertions.assertTrue(
                driver.getCurrentUrl().contains("browser-windows"),
                "O navegador não retornou à janela original."
        );
    }

    @After
    public void encerrarNavegador() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
