package com.accenture.tests.steps;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.E;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class BookStoreApiSteps {


private static final String BASE_URL = "https://demoqa.com";

private String username;
private String password;
private String userId;
private String token;
private List<String> isbnSelecionados = new ArrayList<>();

@Dado("que crio um novo usuário na API")
public void criarUsuario() {
    username = "qa_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    password = "Qa@" + UUID.randomUUID().toString().substring(0, 8) + "9A";

    Response response =
        given()
            .baseUri(BASE_URL)
            .contentType("application/json")
            .body(Map.of(
                "userName", username,
                "password", password
            ))
        .when()
            .post("/Account/v1/User");

    assertEquals(201, response.statusCode(),
        "Não foi possível criar o usuário. Resposta: " + response.asString());

    userId = response.jsonPath().getString("userID");

    assertNotNull(userId, "A API não retornou o userID.");
    System.out.println("Usuário criado: " + username);
    System.out.println("ID do usuário: " + userId);
}

@Quando("gero o token de acesso")
public void gerarToken() {
    Response response =
        given()
            .baseUri(BASE_URL)
            .contentType("application/json")
            .body(Map.of(
                "userName", username,
                "password", password
            ))
        .when()
            .post("/Account/v1/GenerateToken");

    assertEquals(200, response.statusCode(),
        "Falha ao gerar token. Resposta: " + response.asString());

    token = response.jsonPath().getString("token");

    assertNotNull(token, "A API não retornou um token.");
    assertFalse(token.isBlank(), "O token retornado está vazio.");

    System.out.println("Token gerado com sucesso.");
}

@Então("o usuário deve estar autorizado")
public void validarAutorizacao() {
    Response response =
        given()
            .baseUri(BASE_URL)
            .contentType("application/json")
            .body(Map.of(
                "userName", username,
                "password", password
            ))
        .when()
            .post("/Account/v1/Authorized");

    assertEquals(200, response.statusCode(),
        "Falha ao verificar autorização. Resposta: " + response.asString());

    assertEquals(Boolean.TRUE, response.as(Boolean.class),
        "A API não confirmou a autorização do usuário.");

    System.out.println("Usuário autorizado com sucesso.");
}

@Quando("consulto a lista de livros disponíveis")
public void consultarLivros() {
    Response response =
        given()
            .baseUri(BASE_URL)
            .accept("application/json")
        .when()
            .get("/BookStore/v1/Books");

    assertEquals(200, response.statusCode(),
        "Falha ao consultar livros. Resposta: " + response.asString());

    List<Map<String, Object>> livros =
        response.jsonPath().getList("books");

    assertNotNull(livros, "A resposta não contém a lista de livros.");
    assertTrue(livros.size() >= 2,
        "A API precisa disponibilizar pelo menos dois livros.");

    for (Map<String, Object> livro : livros) {
        Object isbn = livro.get("isbn");
        if (isbn != null && !isbn.toString().isBlank()) {
            isbnSelecionados.add(isbn.toString());
        }
        if (isbnSelecionados.size() == 2) {
            break;
        }
    }

    assertEquals(2, isbnSelecionados.size(),
        "Não foi possível selecionar dois ISBNs válidos.");

    System.out.println("ISBNs selecionados: " + isbnSelecionados);
}

@Quando("seleciono dois livros para reservar")
public void reservarDoisLivros() {
    List<Map<String, String>> colecaoIsbns = new ArrayList<>();

    for (String isbn : isbnSelecionados) {
        colecaoIsbns.add(Map.of("isbn", isbn));
    }

    Response response =
        given()
            .baseUri(BASE_URL)
            .contentType("application/json")
            .header("Authorization", "Bearer " + token)
            .body(Map.of(
                "userId", userId,
                "collectionOfIsbns", colecaoIsbns
            ))
        .when()
            .post("/BookStore/v1/Books");

    assertEquals(201, response.statusCode(),
        "Falha ao associar os livros ao usuário. Resposta: "
            + response.asString());

    System.out.println("Dois livros associados à conta.");
}

@Então("os dois livros devem ser associados ao usuário")
public void validarLivrosAssociados() {
    consultarEValidarLivros();
}

@Então("os detalhes do usuário devem apresentar os livros reservados")
public void validarDetalhesUsuario() {
    consultarEValidarLivros();
    System.out.println("Fluxo da API concluído com sucesso!");
}

private void consultarEValidarLivros() {
    Response response =
        given()
            .baseUri(BASE_URL)
            .header("Authorization", "Bearer " + token)
            .accept("application/json")
        .when()
            .get("/Account/v1/User/{userID}", userId);

    assertEquals(200, response.statusCode(),
        "Falha ao consultar os detalhes do usuário. Resposta: "
            + response.asString());

    List<Map<String, Object>> livrosDoUsuario =
        response.jsonPath().getList("books");

    assertNotNull(livrosDoUsuario,
        "A resposta não contém a lista de livros do usuário.");

    List<String> isbnsEncontrados = new ArrayList<>();

    for (Map<String, Object> livro : livrosDoUsuario) {
        Object isbn = livro.get("isbn");
        if (isbn != null) {
            isbnsEncontrados.add(isbn.toString());
        }
    }

    for (String isbn : isbnSelecionados) {
        assertTrue(isbnsEncontrados.contains(isbn),
            "O livro com ISBN " + isbn
                + " não foi encontrado na conta do usuário.");
    }

    assertTrue(isbnsEncontrados.containsAll(isbnSelecionados),
        "Nem todos os livros selecionados foram encontrados.");

    System.out.println("Livros encontrados na conta: " + isbnsEncontrados);
}


}
