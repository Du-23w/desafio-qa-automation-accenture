# language: pt

Funcionalidade: Janelas do navegador

  Cenário: Abrir e validar uma nova janela
    Dado que acesso a página Browser Windows
    Quando clico no botão New Window
    Então a nova janela deve exibir a mensagem "This is a sample page"
    E fecho a nova janela e retorno à janela original
