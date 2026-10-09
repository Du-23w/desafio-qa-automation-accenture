
package com.accenture.tests.steps;

import io.cucumber.java.After;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.E;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.UUID;

public class PracticeFormSteps {

    private WebDriver driver;
    private WebDriverWait wait;

    @Dado("que acesso o Practice Form")
    public void acessarPracticeForm() {
        WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        driver.manage().window().maximize();
        driver.get("https://demoqa.com/automation-practice-form");

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("firstName")
        ));
    }

    @Quando("preencho o formulário com dados aleatórios")
    public void preencherFormulario() {
        String identificador = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 6);

        preencher(By.id("firstName"), "Teste" + identificador);
        preencher(By.id("lastName"), "QA" + identificador);
        preencher(
                By.id("userEmail"),
                "qa" + identificador + "@example.com"
        );

        // Gênero
        clicarJavaScript(
                By.cssSelector("label[for='gender-radio-1']")
        );

        preencher(By.id("userNumber"), "11987654321");

        // Data de nascimento
        clicarJavaScript(By.id("dateOfBirthInput"));

        Select mes = new Select(
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.className("react-datepicker__month-select")
                ))
        );
        mes.selectByVisibleText("December");

        Select ano = new Select(
                driver.findElement(
                        By.className("react-datepicker__year-select")
                )
        );
        ano.selectByVisibleText("1998");

        clicarJavaScript(By.cssSelector(
                ".react-datepicker__day--015" +
                ":not(.react-datepicker__day--outside-month)"
        ));

        // Matéria
        WebElement campoMateria = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.id("subjectsInput")
                )
        );
        campoMateria.sendKeys("Maths");
        campoMateria.sendKeys(Keys.ENTER);

        // Hobby
        clicarJavaScript(
                By.cssSelector("label[for='hobbies-checkbox-1']")
        );

        // Endereço
        preencher(
                By.id("currentAddress"),
                "Endereco de teste QA, Sao Paulo"
        );

        // Estado
        selecionarReactSelect("state", "NCR");

        // Cidade
        selecionarReactSelect("city", "Delhi");
    }

    private void selecionarReactSelect(
            String idControle,
            String valor
    ) {
        // Localiza o input interno do componente React Select.
        By seletorInput = By.cssSelector(
                "#" + idControle + " input"
        );

        WebElement campo = wait.until(
                ExpectedConditions.presenceOfElementLocated(seletorInput)
        );

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});",
                campo
        );

        // O input interno pode não aceitar interação direta em algumas
        // versões do componente; primeiro abre o controle visual.
        WebElement controle = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector(
                                "#" + idControle + " [class$='-control']"
                        )
                )
        );

        clicarJavaScript(controle);

        campo = wait.until(
                ExpectedConditions.presenceOfElementLocated(seletorInput)
        );

        campo.sendKeys(valor);

        // Aguarda a opção visível pelo texto, sem IDs numéricos fixos.
        By seletorOpcao = By.xpath(
                "//div[contains(@class,'option') and " +
                "normalize-space(.)='" + valor + "']"
        );

        WebElement opcao = wait.until(
                ExpectedConditions.elementToBeClickable(seletorOpcao)
        );

        clicarJavaScript(opcao);

        // Confirma que o controle mostra a opção escolhida.
        wait.until(d ->
                d.findElement(By.id(idControle))
                        .getText()
                        .contains(valor)
        );
    }

    @E("anexo um arquivo de texto")
    public void anexarArquivo() {
        Path arquivo = Paths.get(
                "src",
                "test",
                "resources",
                "uploads",
                "arquivo-upload.txt"
        ).toAbsolutePath().normalize();

        Assertions.assertTrue(
                Files.isRegularFile(arquivo),
                "Arquivo de upload não encontrado: " + arquivo
        );

        driver.findElement(By.id("uploadPicture"))
                .sendKeys(arquivo.toString());
    }

    @E("envio o formulário")
    public void enviarFormulario() {
        WebElement botaoEnviar = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.id("submit")
                )
        );

        clicarJavaScript(botaoEnviar);
    }

    @Então("o popup de confirmação deve ser exibido")
    public void validarPopup() {
        WebElement titulo = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("example-modal-sizes-title-lg")
                )
        );

        Assertions.assertEquals(
                "Thanks for submitting the form",
                titulo.getText().trim(),
                "O título do popup está incorreto."
        );

        WebElement tabela = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(".table-responsive")
                )
        );

        Assertions.assertTrue(
                tabela.isDisplayed(),
                "Os dados enviados não foram exibidos no popup."
        );
    }

    @E("fecho o popup de confirmação")
    public void fecharPopup() {
        WebElement botaoFechar = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.id("closeLargeModal")
                )
        );

        clicarJavaScript(botaoFechar);
    }

    @After
    public void encerrarNavegador() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    private void preencher(By localizador, String valor) {
        WebElement campo = wait.until(
                ExpectedConditions.visibilityOfElementLocated(localizador)
        );

        campo.clear();
        campo.sendKeys(valor);
    }

    private void clicarJavaScript(By localizador) {
        WebElement elemento = wait.until(
                ExpectedConditions.presenceOfElementLocated(localizador)
        );

        clicarJavaScript(elemento);
    }

    private void clicarJavaScript(WebElement elemento) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});",
                elemento
        );

        wait.until(d -> elemento.isDisplayed());

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].click();",
                elemento
        );
    }
}