
package com.accenture.tests.steps;

import io.cucumber.java.After;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WebTablesSteps {

    private WebDriver driver;
    private WebDriverWait wait;

    private final List<String> nomesCadastrados = new ArrayList<>();

    private static final By BOTAO_ADICIONAR =
            By.id("addNewRecordButton");

    private static final By FORMULARIO =
            By.id("userForm");

    private static final By BOTAO_SUBMIT =
            By.id("submit");

    private static final By CABECALHO_NOME =
            By.xpath("//*[normalize-space(.)='First Name']");

    private static final By SELETOR_QUANTIDADE =
            By.cssSelector(
                    "select[aria-label='rows per page'], " +
                    ".-pageSizeOptions select, " +
                    ".pagination select, " +
                    ".select-wrap select"
            );

    @Dado("que acesso a página Web Tables")
    public void acessarPaginaWebTables() {
        WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        driver.manage().window().maximize();
        driver.get("https://demoqa.com/webtables");

        try {
            wait.until(ExpectedConditions.elementToBeClickable(
                    BOTAO_ADICIONAR
            ));

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    CABECALHO_NOME
            ));

            configurarQuantidadeDeLinhas();

            System.out.println("Web Tables carregada.");

        } catch (TimeoutException e) {
            String corpo = driver.findElement(By.tagName("body")).getText();

            Assertions.fail(
                    "Falha ao carregar ou configurar Web Tables."
                    + "\nURL: " + driver.getCurrentUrl()
                    + "\nTítulo: " + driver.getTitle()
                    + "\nConteúdo: "
                    + corpo.substring(0, Math.min(corpo.length(), 1500)),
                    e
            );
        }
    }

    private void configurarQuantidadeDeLinhas() {
        List<WebElement> seletores =
                driver.findElements(SELETOR_QUANTIDADE);

        if (seletores.isEmpty()) {
            throw new AssertionError(
                    "Não foi encontrado o seletor de quantidade de linhas."
            );
        }

        Select select = new Select(seletores.get(0));

        boolean possui20 = select.getOptions()
                .stream()
                .anyMatch(opcao ->
                        "20".equals(opcao.getAttribute("value"))
                );

        if (!possui20) {
            throw new AssertionError(
                    "A opção de 20 linhas não está disponível."
            );
        }

        select.selectByValue("20");

        wait.until(d -> {
            List<WebElement> atuais =
                    d.findElements(SELETOR_QUANTIDADE);

            if (atuais.isEmpty()) {
                return false;
            }

            return "20".equals(
                    new Select(atuais.get(0))
                            .getFirstSelectedOption()
                            .getAttribute("value")
            );
        });

        System.out.println("Tabela configurada para 20 linhas.");
    }

    @Quando("cadastro um registro com dados dinâmicos")
    public void cadastrarUmRegistro() {
        nomesCadastrados.clear();

        String id = identificadorUnico();
        String nome = "Teste" + id;

        abrirFormulario();

        preencherFormulario(
                nome, "QA" + id, "qa" + id + "@example.com",
                "30", "5000", "QA Automation"
        );

        enviarFormulario();
        aguardarRegistro(nome);

        nomesCadastrados.add(nome);

        System.out.println("Registro cadastrado: " + nome);
    }

    @Então("o registro cadastrado deve estar visível")
    public void validarRegistroCadastrado() {
        Assertions.assertTrue(
                aguardarRegistro(nomesCadastrados.get(0)).isDisplayed(),
                "O registro cadastrado não foi encontrado."
        );
    }

    @Quando("edito o registro cadastrado")
    public void editarRegistroCadastrado() {
        String nomeOriginal = nomesCadastrados.get(0);
        WebElement linha = aguardarRegistro(nomeOriginal);

        clicarElemento(linha.findElement(
                By.cssSelector("[title='Edit']")
        ));

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                FORMULARIO
        ));

        String nomeAtualizado = nomeOriginal + "Editado";

        preencherFormulario(
                nomeAtualizado, "QAAutomation",
                "edit" + identificadorUnico() + "@example.com",
                "31", "6000", "Testes Automatizados"
        );

        enviarFormulario();
        aguardarRegistro(nomeAtualizado);

        nomesCadastrados.set(0, nomeAtualizado);

        System.out.println("Registro atualizado: " + nomeAtualizado);
    }

    @Então("o registro deve exibir os dados atualizados")
    public void validarRegistroAtualizado() {
        Assertions.assertTrue(
                aguardarRegistro(nomesCadastrados.get(0)).isDisplayed(),
                "O registro editado não foi encontrado."
        );
    }

    @Quando("excluo o registro cadastrado")
    public void excluirRegistroCadastrado() {
        excluirRegistro(nomesCadastrados.get(0));
    }

    @Então("o registro não deve mais estar visível")
    public void validarRegistroExcluido() {
        String nome = nomesCadastrados.get(0);

        wait.until(d -> !registroExiste(nome));

        Assertions.assertFalse(
                registroExiste(nome),
                "O registro ainda aparece na tabela."
        );
    }

    @Quando("cadastro 12 registros com dados dinâmicos")
    public void cadastrarDozeRegistros() {
        nomesCadastrados.clear();

        for (int i = 1; i <= 12; i++) {
            String id = identificadorUnico();
            String nome = "Teste" + i + id;

            abrirFormulario();

            preencherFormulario(
                    nome,
                    "QA" + id,
                    "qa" + id + "@example.com",
                    String.valueOf(20 + i),
                    String.valueOf(3000 + i * 100),
                    "QA Automation"
            );

            enviarFormulario();
            aguardarRegistro(nome);

            nomesCadastrados.add(nome);

            System.out.println(
                    "Cadastro confirmado " + i + "/12: " + nome
            );
        }
    }

    @Então("os 12 registros cadastrados devem estar visíveis")
    public void validarDozeRegistros() {
        Assertions.assertEquals(
                12,
                nomesCadastrados.size(),
                "A quantidade de registros cadastrados está incorreta."
        );

        List<String> faltantes = new ArrayList<>();

        for (String nome : nomesCadastrados) {
            if (!registroExiste(nome)) {
                faltantes.add(nome);
            }
        }

        Assertions.assertTrue(
                faltantes.isEmpty(),
                "Registros não encontrados na tabela: " + faltantes
        );

        System.out.println("SUCESSO: os 12 registros estão visíveis.");
    }

    @Quando("excluo os 12 registros cadastrados")
    public void excluirDozeRegistros() {
        for (String nome : new ArrayList<>(nomesCadastrados)) {
            excluirRegistro(nome);
            System.out.println("Registro excluído: " + nome);
        }
    }

    @Então("nenhum dos 12 registros deve estar visível")
    public void validarDozeRegistrosExcluidos() {
        List<String> restantes = new ArrayList<>();

        for (String nome : nomesCadastrados) {
            if (registroExiste(nome)) {
                restantes.add(nome);
            }
        }

        Assertions.assertTrue(
                restantes.isEmpty(),
                "Ainda existem registros na tabela: " + restantes
        );

        System.out.println("SUCESSO: todos os registros foram excluídos.");
    }

    private void abrirFormulario() {
        wait.until(ExpectedConditions.elementToBeClickable(
                BOTAO_ADICIONAR
        )).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                FORMULARIO
        ));
    }

    private void preencherFormulario(
            String nome,
            String sobrenome,
            String email,
            String idade,
            String salario,
            String departamento
    ) {
        preencherCampo("firstName", nome);
        preencherCampo("lastName", sobrenome);
        preencherCampo("userEmail", email);
        preencherCampo("age", idade);
        preencherCampo("salary", salario);
        preencherCampo("department", departamento);
    }

    private void preencherCampo(String id, String valor) {
        WebElement campo = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id(id)
                )
        );

        campo.clear();
        campo.sendKeys(valor);
    }

    private void enviarFormulario() {
        WebElement botao = wait.until(
                ExpectedConditions.elementToBeClickable(BOTAO_SUBMIT)
        );

        botao.click();

        try {
            wait.until(
                    ExpectedConditions.invisibilityOfElementLocated(
                            FORMULARIO
                    )
            );
        } catch (TimeoutException e) {
            System.err.println("ERRO: formulário não fechou após o envio.");
            System.err.println("URL: " + driver.getCurrentUrl());

            List<WebElement> formularios =
                    driver.findElements(FORMULARIO);

            if (!formularios.isEmpty()) {
                WebElement formulario = formularios.get(0);

                System.err.println(
                        "Conteúdo do formulário: " + formulario.getText()
                );

                List<WebElement> camposInvalidos =
                        formulario.findElements(
                                By.cssSelector("input:invalid")
                        );

                for (WebElement campo : camposInvalidos) {
                    System.err.println(
                            "Campo inválido: " + campo.getAttribute("id")
                            + " | Valor: " + campo.getAttribute("value")
                            + " | Motivo: "
                            + campo.getAttribute("validationMessage")
                    );
                }
            }

            throw new AssertionError(
                    "O formulário não foi fechado após clicar em Submit.",
                    e
            );
        }
    }

    private By localizarRegistro(String nome) {
        return By.xpath(
                "//div[contains(concat(' ', normalize-space(@class), ' '), " +
                "' rt-tr-group ')][.//*[normalize-space(text())='" +
                nome + "']]"
        );
    }

    private WebElement aguardarRegistro(String nome) {
        try {
            return wait.until(d -> {
                List<WebElement> linhas =
                        d.findElements(
                                By.cssSelector(".rt-tr-group")
                        );

                for (WebElement linha : linhas) {
                    if (linha.getText().contains(nome)) {
                        return linha;
                    }
                }

                return null;
            });

        } catch (TimeoutException e) {
            System.err.println(
                    "\n========== DIAGNÓSTICO WEB TABLES =========="
            );
            System.err.println("Registro esperado: " + nome);
            System.err.println("URL atual: " + driver.getCurrentUrl());

            List<WebElement> formularios =
                    driver.findElements(FORMULARIO);

            System.err.println(
                    "Formulário encontrado: " + !formularios.isEmpty()
            );

            if (!formularios.isEmpty()) {
                WebElement formulario = formularios.get(0);

                System.err.println(
                        "Formulário visível: " + formulario.isDisplayed()
                );
                System.err.println(
                        "Conteúdo do formulário: " + formulario.getText()
                );
            }

            List<WebElement> botoesAdicionar =
                    driver.findElements(BOTAO_ADICIONAR);

            System.err.println(
                    "Botão Add encontrado: " + !botoesAdicionar.isEmpty()
            );

            if (!botoesAdicionar.isEmpty()) {
                System.err.println(
                        "Botão Add visível: " +
                        botoesAdicionar.get(0).isDisplayed()
                );
            }

            List<WebElement> linhas =
                    driver.findElements(
                            By.cssSelector(".rt-tr-group")
                    );

            System.err.println(
                    "Total de linhas localizadas: " + linhas.size()
            );

            for (WebElement linha : linhas) {
                String texto = linha.getText().trim();

                if (!texto.isEmpty()) {
                    System.err.println("Linha: " + texto);
                }
            }

            
System.err.println("Título da página: " + driver.getTitle());

Object diagnostico = ((JavascriptExecutor) driver).executeScript(
        "return JSON.stringify({"
        + "body: document.body.innerText.substring(0, 3000),"
        + "tabelas: document.querySelectorAll('.rt-table').length,"
        + "linhas: document.querySelectorAll('.rt-tr-group').length,"
        + "conteudoTabela: Array.from("
        + "document.querySelectorAll('.rt-table')"
        + ").map(t => t.outerHTML.substring(0, 2000))"
        + "});"
);

System.err.println("Diagnóstico DOM: " + diagnostico);

            System.err.println("Texto da tabela:");

            List<WebElement> tabelas =
                    driver.findElements(
                            By.cssSelector(".rt-table")
                    );

            for (WebElement tabela : tabelas) {
                System.err.println(tabela.getText());
            }

            System.err.println(
                    "========== FIM DO DIAGNÓSTICO ==========\n"
            );

            throw new AssertionError(
                    "O registro '" + nome +
                    "' não foi localizado após o envio do formulário.",
                    e
            );
        }
    }

    private boolean registroExiste(String nome) {
        return !driver.findElements(
                localizarRegistro(nome)
        ).isEmpty();
    }

    private List<WebElement> localizarLinhasVisiveis(WebDriver d) {
        return d.findElements(
                By.cssSelector(".rt-tr-group")
        );
    }

    private void excluirRegistro(String nome) {
        WebElement linha = aguardarRegistro(nome);

        clicarElemento(linha.findElement(
                By.cssSelector("[title='Delete']")
        ));

        wait.until(d -> !registroExiste(nome));
    }

    private void clicarElemento(WebElement elemento) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});",
                elemento
        );

        wait.until(ExpectedConditions.visibilityOf(elemento));

        try {
            wait.until(ExpectedConditions.elementToBeClickable(
                    elemento
            )).click();

        } catch (
                org.openqa.selenium.ElementClickInterceptedException e
        ) {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].click();",
                    elemento
            );
        }
    }

    private String identificadorUnico() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8);
    }

    @After
    public void finalizar() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}