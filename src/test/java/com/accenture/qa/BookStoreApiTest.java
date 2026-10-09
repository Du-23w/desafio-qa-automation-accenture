
package com.accenture.qa;

import com.accenture.qa.services.BookStoreService;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookStoreApiTest {

    private final BookStoreService service = new BookStoreService();

    @Test
    void fluxoApi() {

        // Dados do usuário
        String username = "wellingtonqa" + System.currentTimeMillis();
        String password = "Teste@123456";

        // 1 - Criar usuário
        Response responseCriacao =
                service.criarUsuario(username, password);

        responseCriacao.then()
                .statusCode(201)
                .body("userID", notNullValue());

        String userId =
                responseCriacao.jsonPath().getString("userID");

        System.out.println("Usuário criado com sucesso!");
        System.out.println("Username: " + username);
        System.out.println("User ID: " + userId);

        // 2 - Gerar token
        Response responseToken =
                service.gerarToken(username, password);

        responseToken.then()
                .statusCode(200)
                .body("token", notNullValue());

        String token =
                responseToken.jsonPath().getString("token");

        System.out.println("Token gerado com sucesso!");

        // 3 - Verificar se o usuário está autorizado
        Response responseAutorizacao =
                service.verificarAutorizacao(username, password);

        responseAutorizacao.then()
                .statusCode(200);

        assertEquals(
                "true",
                responseAutorizacao.asString().trim()
        );

        System.out.println("Usuário autorizado com sucesso!");

        // 4 - Listar livros disponíveis
        Response responseBooks =
                service.listarLivros();

        responseBooks.then()
                .statusCode(200);

        System.out.println("Livros disponíveis:");

        responseBooks.jsonPath()
                .getList("books.title")
                .forEach(System.out::println);

        // Selecionar dois livros automaticamente
        String isbn1 =
                responseBooks.jsonPath().getString("books[0].isbn");

        String isbn2 =
                responseBooks.jsonPath().getString("books[1].isbn");

        assertEquals(false,
                isbn1 == null || isbn2 == null,
                "A API deve retornar pelo menos dois livros com ISBN.");

        System.out.println("ISBN 1: " + isbn1);
        System.out.println("ISBN 2: " + isbn2);

        // 5 - Reservar dois livros
        Response responseReserva =
                service.reservarLivros(
                        userId,
                        token,
                        isbn1,
                        isbn2
                );

        responseReserva.then()
                .statusCode(201);

        System.out.println("Dois livros reservados com sucesso!");

        // 6 - Consultar os dados do usuário
        Response responseUser =
                service.consultarUsuario(userId, token);

        responseUser.then()
                .statusCode(200);

        System.out.println("Usuário consultado com sucesso!");

        System.out.println("Livros reservados:");

        responseUser.jsonPath()
                .getList("books.title")
                .forEach(System.out::println);

        // Validar os livros reservados
        String isbnReservado1 =
                responseUser.jsonPath().getString("books[0].isbn");

        String isbnReservado2 =
                responseUser.jsonPath().getString("books[1].isbn");

        assertEquals(isbn1, isbnReservado1);
        assertEquals(isbn2, isbnReservado2);

        System.out.println(
                "Os dois livros foram validados com sucesso!"
        );
    }
}