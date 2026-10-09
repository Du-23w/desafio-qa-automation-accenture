# Desafio de QA Automation — Accenture

## Sobre o projeto

Projeto desenvolvido para o desafio técnico de QA Automation da Accenture, com foco em testes automatizados de API e interface web utilizando Java, Selenium WebDriver, Cucumber e Maven.

O ambiente utilizado para os testes é o [DemoQA](https://demoqa.com/).

## Tecnologias utilizadas

* Java 17
* Maven
* Selenium WebDriver
* Cucumber e Gherkin (BDD)
* JUnit
* Git e GitHub

## Cenários do desafio

### 1. Book Store API

* Criar usuário.
* Gerar token de acesso.
* Validar autorização.
* Listar livros disponíveis.
* Reservar dois livros.
* Consultar os dados do usuário e seus livros.

Documentação: [DemoQA Swagger](https://demoqa.com/swagger/)

### 2. Practice Form

* Preencher o formulário com dados de teste.
* Fazer upload de um arquivo `.txt`.
* Enviar o formulário.
* Validar e fechar o popup de confirmação.

### 3. Browser Windows

* Abrir uma nova janela.
* Validar a mensagem `This is a sample page`.
* Fechar a nova janela.

### 4. Web Tables

* Criar um registro.
* Editar o registro.
* Excluir o registro.

**Bônus:** criar 12 registros dinamicamente com Cucumber e excluir os registros criados.

### 5. Progress Bar

* Iniciar a barra de progresso.
* Interromper antes de ultrapassar 25%.
* Validar o percentual.
* Retomar até 100%.
* Resetar a barra.

## Pré-requisitos

* JDK 17
* Maven
* Google Chrome

Verifique as instalações com:

```bash
java -version
mvn -version
```

## Como executar os testes

Clone o repositório:

```bash
git clone https://github.com/Du-23w/desafio-qa-automation-accenture.git
cd desafio-qa-automation-accenture
```

Execute os testes configurados no Maven:

```bash
mvn clean test
```

Para executar classes específicas, se estiverem configuradas no Maven:

```bash
mvn clean test -Dtest=BookStoreApiTest
mvn clean test -Dtest=PracticeFormTest
mvn clean test -Dtest=BrowserWindowsTest
mvn clean test -Dtest=WebTablesTest
mvn clean test -Dtest=ProgressBarTest
```

## Arquivo de upload

O projeto inclui o arquivo `src/test/resources/arquivo-upload.txt` para o cenário de upload.

## Repositório

[GitHub — Desafio de QA Automation Accenture](https://github.com/Du-23w/desafio-qa-automation-accenture)

> Os cenários e comandos documentados devem ser validados no ambiente local. A presença de um teste no projeto não significa que ele já tenha sido executado com sucesso.
