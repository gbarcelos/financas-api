# financas-api

Projeto de uma API para controle de orçamento familiar. A API deve permitir que uma pessoa cadastre suas receitas e despesas do mês, bem como gerar um relatório mensal.

| :placard: Vitrine.Dev |     |
| -------------  | --- |
| :sparkles: Nome        | **financas-api**
| :label: Tecnologias | Java, Spring Boot, Spring Data JPA, Spring Security OAuth, Maven
| :rocket: URL         | https://oak-financas-api.herokuapp.com
| :fire: Desafio     | https://www.alura.com.br/challenges/back-end

![thumbnail-financas-api](https://user-images.githubusercontent.com/13111308/153711634-b84b17dd-4541-428c-be15-0987dc480097.png?text=capa+do+meu+projeto#vitrinedev)

## 🔨 Funcionalidades do projeto

- `Funcionalidade 1` `Cadastro de despesas`: A API deve possuir um endpoint para o cadastro de despesas, sendo que ele deve aceitar requisições do tipo POST para a URI /despesas. Os dados da despesa(descrição, valor e data) devem ser enviados no corpo da requisição, no formato JSON.
- `Funcionalidade 2` `Cadastro de receitas`: A API deve possuir um endpoint para o cadastro de receitas, sendo que ele deve aceitar requisições do tipo POST para a URI /receitas. Os dados da receita(descrição, valor e data) devem ser enviados no corpo da requisição, no formato JSON.

## ✔️ Tecnologias utilizadas

<img alt="Java" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original-wordmark.svg" width="50" height="50"/> <img alt="IntelliJ" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/intellij/intellij-original.svg" width="50" height="50"/> <img alt="Spring" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original-wordmark.svg" width="50" height="50"/> <img alt="MySQL" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/mysql/mysql-original-wordmark.svg"  width="50" height="50"/> <img alt="Heroku" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/heroku/heroku-plain-wordmark.svg"  width="50" height="50"/>

- ``Java 8``
- ``InteliJ IDEA``
- ``Spring Boot, Spring Data JPA, Spring Security OAuth``
- ``MySQL``
- ``Maven``
- ``Flyway``
- ``Lombok``
- ``Bean Validation``
- ``OpenAPI (Swagger)``
- ``OAuth2 e JWT``

## ✔️ Técnicas utilizadas

- ``Criação e evolução do schema do banco de dados com Flyway``
- ``Utilização do Lombok nas classes para reduzir código boilerplate``
- ``Validações de entradas de dados na API com Bean Validation``
- ``Documentação com OpenAPI (Swagger)``
- ``Segurança com Spring Security, OAuth2 e JWT``
- ``Tratamento e modelagem de erros da API``
	- ``Modelagem de erros seguindo a RFC 7807 (Problem Details for HTTP APIs)``
- ``Boas práticas e modelagem avançada de APIs``
	- ``DTOs como alternativa ao uso de entidades como modelo de representação dos recursos``
	- ``ModelMapper para fazer Object Mapping e converter DTOs em entidades e vice-versa``
	- ``UUID para identificar recursos sensíveis``
- ``Testes``
	- ``Testes de unidade``
	- ``Testes de integração``
	- ``Testes e2e automatizados para a API. Cada funcionalidade está descrita nos arquivos de feature do Gherkin, que sabe interpretar a linguagem natural, e o Cucumber automatiza todos os critérios de aceite``

## :hammer_and_wrench: Deploy
O deploy da aplicação no heroku, podendo ser acessada por esse [link](https://oak-financas-api.herokuapp.com).

## 📁 Acesso ao projeto de testes e2e
Você pode acessar os arquivos do projeto de teste e2e clicando [aqui](https://github.com/gbarcelos/financas-api-e2e).
