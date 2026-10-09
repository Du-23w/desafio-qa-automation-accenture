package com.accenture.qa.services;

import com.accenture.qa.config.ApiConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BookStoreService {

    // Criar usuário
    public Response criarUsuario(String username, String password) {

        String body = """
                {
                    "userName": "%s",
                    "password": "%s"
                }
                """.formatted(username, password);

        return given()
                .contentType(ContentType.JSON)
                .body(body)
        .when()
                .post(ApiConfig.BASE_URL + ApiConfig.CREATE_USER);
    }

    // Gerar token
    public Response gerarToken(String username, String password) {

        String body = """
                {
                    "userName": "%s",
                    "password": "%s"
                }
                """.formatted(username, password);

        return given()
                .contentType(ContentType.JSON)
                .body(body)
        .when()
                .post(ApiConfig.BASE_URL + ApiConfig.GENERATE_TOKEN);
    }

    // Verificar autorização
    public Response verificarAutorizacao(String username, String password) {

        String body = """
                {
                    "userName": "%s",
                    "password": "%s"
                }
                """.formatted(username, password);

        return given()
                .contentType(ContentType.JSON)
                .body(body)
        .when()
                .post(ApiConfig.BASE_URL + ApiConfig.AUTHORIZED);
    }

    // Listar livros
    public Response listarLivros() {

        return given()
                .contentType(ContentType.JSON)
        .when()
                .get(ApiConfig.BASE_URL + ApiConfig.BOOKS);
    }

    // Reservar livros
    public Response reservarLivros(
            String userId,
            String token,
            String isbn1,
            String isbn2) {

        String body = """
                {
                    "userId": "%s",
                    "collectionOfIsbns": [
                        {
                            "isbn": "%s"
                        },
                        {
                            "isbn": "%s"
                        }
                    ]
                }
                """.formatted(userId, isbn1, isbn2);

        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(body)
        .when()
                .post(ApiConfig.BASE_URL + ApiConfig.BOOKS);
    }

    // Consultar usuário
    public Response consultarUsuario(String userId, String token) {

        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
        .when()
                .get(ApiConfig.BASE_URL + ApiConfig.USER + userId);
    }
}
