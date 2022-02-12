# Resumo do projeto
Projeto de uma API para controle de orçamento familiar. A API deve permitir que uma pessoa cadastre suas receitas e despesas do mês, bem como gerar um relatório mensal.

## 🔨 Funcionalidades do projeto

- `Funcionalidade 1` `Cadastro de despesas`: A API deve possuir um endpoint para o cadastro de despesas, sendo que ele deve aceitar requisições do tipo POST para a URI /despesas. Os dados da despesa(descrição, valor e data) devem ser enviados no corpo da requisição, no formato JSON.
- `Funcionalidade 2` `Cadastro de receitas`: A API deve possuir um endpoint para o cadastro de receitas, sendo que ele deve aceitar requisições do tipo POST para a URI /receitas. Os dados da receita(descrição, valor e data) devem ser enviados no corpo da requisição, no formato JSON.

## ✔️ Tecnologias utilizadas

- ``Java 8``
- ``InteliJ IDEA``
- ``Maven``
- ``Spring Boot``
- ``Spring Data JPA``
- ``Spring Security OAuth``
- ``Flyway``
- ``Lombok``
- ``Bean Validation``
- ``OpenAPI (Swagger)``
- ``OAuth2 e JWT``

## ✔️ Técnicas utilizadas

- ``Criação e evolução do schema do banco de dados com Flyway.``
- ``Utilização do Lombok nas classes para reduzir código boilerplate.``
- ``Validações de entradas de dados na API com Bean Validation``
- ``Documentação com OpenAPI (Swagger)``
- ``Segurança com Spring Security, OAuth2 e JWT``
- ``Tratamento e modelagem de erros da API``
	- ``Modelagem de erros seguindo a RFC 7807 (Problem Details for HTTP APIs).``
- ``Boas práticas e modelagem avançada de APIs``
	- ``DTOs como alternativa ao uso de entidades como modelo de representação dos recursos.``
	- ``ModelMapper para fazer Object Mapping e converter DTOs em entidades e vice-versa.``
	- ``UUID para identificar recursos sensíveis``
- ``Testes``
	- ``Testes de unidade na camada de serviço``
	- ``Testes e2e automatizados para a API. Cada funcionalidade e seus critérios de aceite estão descritos nos arquivos de feature do Gherkin, que sabe interpretar a linguagem natural e o Cucumber foi utilizado para automatizar todos os critérios de aceite.``

## 📁 Acesso ao projeto
Você pode acessar os arquivos do projeto clicando [aqui](https://github.com/gbarcelos/financas-api/tree/main/src/main/java/br/com/oak/financas/api).

## 📁 Acesso ao projeto de testes e2e
Você pode acessar os arquivos do projeto de teste e2e clicando [aqui](https://github.com/gbarcelos/financas-api-e2e).
