
package com.accenture.qa.tests;
import com.accenture.pages.PracticeFormPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PracticeFormTest {

    @Test
    void devePreencherEnviarEFecharPracticeForm() {

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
            PracticeFormPage formulario =
                new PracticeFormPage(driver);

            // 1. Abre o formulário
            formulario.abrir();

            // 2. Preenche os campos
            formulario.preencherFormulario();

            // 3. Localiza o arquivo .txt dentro do projeto
            String caminhoArquivo = Path.of(
                "src",
                "test",
                "resources",
                "arquivo-upload.txt"
            ).toAbsolutePath().toString();

            // 4. Anexa o arquivo
            formulario.anexarArquivo(caminhoArquivo);

            // 5. Envia o formulário
            formulario.enviarFormulario();

            // 6. Valida o modal de confirmação
            assertTrue(
                formulario.modalDeConfirmacaoVisivel(),
                "O modal de confirmação deveria estar visível"
            );

            System.out.println(
                "Modal de confirmação exibido com sucesso."
            );

            // 7. Fecha o modal e verifica se desapareceu
            formulario.fecharModal();

        } finally {
            // Fecha o navegador mesmo se o teste falhar
            driver.quit();
        }
    }
}

