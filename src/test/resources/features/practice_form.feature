# language: pt

Funcionalidade: Formulário de prática

  Cenário: Preencher e enviar o formulário com dados dinâmicos
    Dado que acesso o Practice Form
    Quando preencho o formulário com dados aleatórios
    E anexo um arquivo de texto
    E envio o formulário
    Então o popup de confirmação deve ser exibido
    E fecho o popup de confirmação
