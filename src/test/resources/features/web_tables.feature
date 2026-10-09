# language: pt

Funcionalidade: Cadastro dinâmico na tabela Web Tables

  Cenário: Cadastrar e validar 12 registros
    Dado que acesso a página Web Tables
    Quando cadastro 12 registros com dados dinâmicos
    Então os 12 registros cadastrados devem estar visíveis
