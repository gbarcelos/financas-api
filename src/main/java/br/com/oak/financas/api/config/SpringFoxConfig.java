package br.com.oak.financas.api.config;

import br.com.oak.financas.api.model.contract.response.ErrorResponse;
import br.com.oak.financas.api.model.dto.LancamentoDto;
import br.com.oak.financas.api.model.input.LancamentoInput;
import com.fasterxml.classmate.TypeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UriComponentsBuilder;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
public class SpringFoxConfig implements WebMvcConfigurer {

  private static final String SECURITY_CONTEXT_NAME = "LancamentosApi";

  @Bean
  public Docket apiDocket() {

    TypeResolver typeResolver = new TypeResolver();

    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
        .paths(PathSelectors.any())
        .build()
        .useDefaultResponseMessages(false)
        .globalResponseMessage(RequestMethod.POST, globalPostPutResponseMessages())
        .ignoredParameterTypes(UriComponentsBuilder.class)
        .ignoredParameterTypes(HttpServletRequest.class)
        .ignoredParameterTypes(LancamentoInput.class)
        .ignoredParameterTypes(LancamentoDto.class)
        .additionalModels(typeResolver.resolve(ErrorResponse.class))
        .securitySchemes(Arrays.asList(securityScheme()))
        .securityContexts(Arrays.asList(securityContext()))
        .apiInfo(apiInfo())
        .tags(
            new Tag("Consultas", "Agrupa as consultas da API"),
            new Tag("Receita", "Gerencia as receitas"),
            new Tag("Despesa", "Gerencia as despesas"));
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
        .addResourceHandler("swagger-ui.html")
        .addResourceLocations("classpath:/META-INF/resources/");

    registry
        .addResourceHandler("/webjars/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/");
  }

  private List<ResponseMessage> globalPostPutResponseMessages() {
    return Arrays.asList(
        new ResponseMessageBuilder()
            .code(HttpStatus.BAD_REQUEST.value())
            .message("Requisição inválida (erro do cliente)")
            .responseModel(new ModelRef("ErrorResponse"))
            .build(),
        new ResponseMessageBuilder()
            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .message("Erro interno no servidor")
            .responseModel(new ModelRef("ErrorResponse"))
            .build(),
        new ResponseMessageBuilder()
            .code(HttpStatus.NOT_ACCEPTABLE.value())
            .message("Recurso não possui representação que poderia ser aceita pelo consumidor")
            .build(),
        new ResponseMessageBuilder()
            .code(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
            .message("Requisição recusada porque o corpo está em um formato não suportado")
            .responseModel(new ModelRef("ErrorResponse"))
            .build());
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("Finanças API")
        .description("API para controle de orçamento doméstico")
        .version("1")
        .build();
  }

  private SecurityScheme securityScheme() {
    return new OAuthBuilder()
        .name(SECURITY_CONTEXT_NAME)
        .grantTypes(grantTypes())
        .scopes(scopes())
        .build();
  }

  private SecurityContext securityContext() {

    SecurityReference securityReference =
        SecurityReference.builder()
            .reference(SECURITY_CONTEXT_NAME)
            .scopes(scopes().toArray(new AuthorizationScope[0]))
            .build();

    return SecurityContext.builder()
        .securityReferences(Arrays.asList(securityReference))
        .forPaths(PathSelectors.any())
        .build();
  }

  private List<GrantType> grantTypes() {
    return Arrays.asList(new ResourceOwnerPasswordCredentialsGrant("/oauth/token"));
  }

  private List<AuthorizationScope> scopes() {
    return Arrays.asList(
        new AuthorizationScope("READ", "Acesso de leitura"),
        new AuthorizationScope("WRITE", "Acesso de escrita"));
  }
}
