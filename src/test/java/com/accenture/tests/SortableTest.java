
package com.accenture.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SortableTest {

    private WebDriver driver;
    private WebDriverWait wait;

    private final By abaLista = By.id("demo-tab-list");

    private final By seletorItens =
            By.cssSelector("#demo-tabpane-list .list-group-item");

    private final List<String> ordemCrescente = List.of(
            "One", "Two", "Three", "Four", "Five", "Six"
    );

    @BeforeEach
    public void configurar() {
        WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver();
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @Test
    public void deveManterElementosEmOrdemCrescente() {

        driver.get("https://demoqa.com/sortable");

        wait.until(d -> d.findElement(abaLista).isDisplayed());
        driver.findElement(abaLista).click();

        wait.until(d -> d.findElements(seletorItens).size() == 6);

        List<String> ordemInicial = obterOrdem();

        System.out.println("Ordem inicial: " + ordemInicial);

        assertEquals(
                ordemCrescente,
                ordemInicial,
                "A lista deveria começar em ordem crescente."
        );

        // Move o último elemento para a primeira posição.
        List<WebElement> itens = driver.findElements(seletorItens);

        WebElement ultimo = itens.get(5);
        WebElement primeiro = itens.get(0);

        new Actions(driver)
                .clickAndHold(ultimo)
                .pause(Duration.ofMillis(300))
                .moveToElement(primeiro)
                .pause(Duration.ofMillis(300))
                .release()
                .perform();

        // Confirma que o arraste alterou a lista.
        wait.until(d -> !obterOrdem().equals(ordemInicial));

        List<String> ordemAposArraste = obterOrdem();

        System.out.println("Ordem após arraste: " + ordemAposArraste);

        // Agora verifica a ordem real, sem presumir que o arraste
        // manteve ou restaurou a lista automaticamente.
        assertEquals(
                6,
                ordemAposArraste.size(),
                "A lista deve continuar contendo seis elementos."
        );

        // Restaura a ordem, movendo cada elemento para sua posição.
        for (int i = 0; i < ordemCrescente.size(); i++) {

            List<WebElement> itensAtuais =
                    driver.findElements(seletorItens);

            String esperado = ordemCrescente.get(i);
            String textoNaPosicao = itensAtuais.get(i).getText();

            if (!textoNaPosicao.equals(esperado)) {

                WebElement elementoMover = itensAtuais.stream()
                        .filter(e -> e.getText().equals(esperado))
                        .findFirst()
                        .orElseThrow();

                WebElement destino = itensAtuais.get(i);

                new Actions(driver)
                        .clickAndHold(elementoMover)
                        .pause(Duration.ofMillis(300))
                        .moveToElement(destino)
                        .pause(Duration.ofMillis(300))
                        .release()
                        .perform();
            }
        }

        // A lista só será aprovada se a ordem final estiver correta.
        wait.until(d -> obterOrdem().equals(ordemCrescente));

        assertEquals(
                ordemCrescente,
                obterOrdem(),
                "A lista deve terminar na ordem crescente esperada."
        );

        System.out.println("Ordem crescente validada com sucesso!");
    }

    private List<String> obterOrdem() {
        return driver.findElements(seletorItens)
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    @AfterEach
    public void finalizar() {
        if (driver != null) {
            driver.quit();
        }
    }
}