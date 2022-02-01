package br.com.oak.financas.api.config;

import br.com.oak.financas.api.entity.Lancamento;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

  @Bean
  public ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();

    modelMapper
        .createTypeMap(Lancamento.class, Lancamento.class)
        .addMappings(mapper -> mapper.skip(Lancamento::setId));

    return modelMapper;
  }
}
