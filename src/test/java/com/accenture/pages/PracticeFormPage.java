
package com.accenture.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;

public class PracticeFormPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public PracticeFormPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(
            driver, Duration.ofSeconds(15)
        );
    }

    public void abrir() {
        driver.get("https://demoqa.com/automation-practice-form");

        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.id("firstName")
        ));
    }

    public void preencherFormulario() {

        driver.findElement(By.id("firstName")).sendKeys("Lucas");
        driver.findElement(By.id("lastName")).sendKeys("Silva");
        driver.findElement(By.id("userEmail"))
            .sendKeys("lucas.silva@example.com");

        driver.findElement(
            By.cssSelector("label[for='gender-radio-1']")
        ).click();

        driver.findElement(By.id("userNumber"))
            .sendKeys("1198765432");

        // Data de nascimento: 15/01/2000
        driver.findElement(By.id("dateOfBirthInput")).click();

        driver.findElement(By.cssSelector(
            ".react-datepicker__month-select option[value='0']"
        )).click();

        driver.findElement(By.cssSelector(
            ".react-datepicker__year-select option[value='2000']"
        )).click();

        driver.findElement(By.cssSelector(
            ".react-datepicker__day--015:not(.react-datepicker__day--outside-month)"
        )).click();

        // Matéria
        WebElement campoMateria =
            driver.findElement(By.id("subjectsInput"));

        campoMateria.sendKeys("Maths");
        campoMateria.sendKeys(Keys.ENTER);

        // Hobby
        WebElement hobby = wait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.id("hobbies-checkbox-1")
            )
        );

        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].click();", hobby
        );

        // Endereço
        driver.findElement(By.id("currentAddress"))
            .sendKeys("Rua de Teste, 100");

        // Estado
        WebElement buscaEstado = wait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.id("react-select-3-input")
            )
        );

        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center'});",
            buscaEstado
        );

        buscaEstado.sendKeys("NCR");

        WebElement opcaoEstado = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.id("react-select-3-option-0")
            )
        );

        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].click();", opcaoEstado
        );

        // Cidade
        WebElement buscaCidade = wait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.id("react-select-4-input")
            )
        );

        buscaCidade.sendKeys("Delhi");

        WebElement opcaoCidade = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.id("react-select-4-option-0")
            )
        );

        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].click();", opcaoCidade
        );
    }

    public void anexarArquivo(String caminhoArquivo) {

        File arquivo = new File(caminhoArquivo);

        if (!arquivo.exists() || !arquivo.isFile()) {
            throw new IllegalArgumentException(
                "Arquivo não encontrado: "
                    + arquivo.getAbsolutePath()
            );
        }

        WebElement inputArquivo = wait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.id("uploadPicture")
            )
        );

        inputArquivo.sendKeys(arquivo.getAbsolutePath());

        wait.until(driverAtual -> {
            String valor = driverAtual
                .findElement(By.id("uploadPicture"))
                .getAttribute("value");

            return valor != null
                && valor.endsWith(arquivo.getName());
        });

        System.out.println(
            "Arquivo anexado: " + arquivo.getName()
        );
    }

    public void enviarFormulario() {

        WebElement botaoEnviar = wait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.id("submit")
            )
        );

        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center'});",
            botaoEnviar
        );

        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].click();", botaoEnviar
        );
    }

    public boolean modalDeConfirmacaoVisivel() {
        return wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.id("example-modal-sizes-title-lg")
            )
        ).isDisplayed();
    }

    








public void fecharModal() {

    // Tenta fechar o modal usando a tecla ESC,
    // como um usuário faria ao interagir com a janela.
    driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);

    // Aguarda até que o modal realmente desapareça.
    wait.until(
        ExpectedConditions.invisibilityOfElementLocated(
            By.cssSelector(".modal.show")
        )
    );

    System.out.println("Modal fechado com sucesso.");
}


}
















