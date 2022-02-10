package br.com.oak.financas.api.config;

import br.com.oak.financas.api.entity.Lancamento;
import br.com.oak.financas.api.model.dto.DespesaDto;
import br.com.oak.financas.api.model.input.DespesaInput;
import br.com.oak.financas.api.model.input.ReceitaInput;
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
        .addMappings(
            mapper -> {
              mapper.skip(Lancamento::setId);
              mapper.skip(Lancamento::setUsuario);
            });

    modelMapper
        .createTypeMap(ReceitaInput.class, Lancamento.class)
        .addMappings(mapper -> mapper.skip(Lancamento::setId));

    modelMapper
        .createTypeMap(DespesaInput.class, Lancamento.class)
        .addMappings(mapper -> mapper.skip(Lancamento::setId));

    modelMapper
        .createTypeMap(Lancamento.class, DespesaDto.class)
        .addMappings(
            mapper ->
                mapper.map(src -> src.getCategoria().getDescricao(), DespesaDto::setCategoria));

    return modelMapper;
  }
}
