# language: pt

Funcionalidade: API da BookStore

  Como usuário da BookStore
  Quero criar uma conta e reservar livros
  Para validar o fluxo completo da API

  Cenário: Criar usuário, autorizar e reservar dois livros
    Dado que crio um novo usuário na API
    Quando gero o token de acesso
    Então o usuário deve estar autorizado
    Quando consulto a lista de livros disponíveis
    E seleciono dois livros para reservar
    Então os dois livros devem ser associados ao usuário
    E os detalhes do usuário devem apresentar os livros reservados
