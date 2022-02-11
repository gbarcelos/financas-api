package br.com.oak.financas.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Validated
@Component
@ConfigurationProperties("financasapi")
public class FinancasApiProperties {
  @NotBlank private String jwtKey;
}
