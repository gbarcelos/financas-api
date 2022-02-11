package br.com.oak.financas.api.service.mapper;

import br.com.oak.financas.api.entity.Lancamento;
import br.com.oak.financas.api.model.input.ReceitaInput;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReceitaMapperTest {

  private ReceitaMapper receitaMapper;

  @BeforeAll
  public void beforeAll() {
    receitaMapper = new ReceitaMapper(new ModelMapper());
  }

  @Test
  public void deveMapear_quandoTodosOsCamposObrigatoriosEstaoInformados() {

    ReceitaInput source =
        ReceitaInput.builder()
            .descricao("descricao")
            .data(LocalDate.of(2022, 1, 1))
            .valor(new BigDecimal("123.41"))
            .build();

    Lancamento result = receitaMapper.map(source);

    assertEquals(source.getDescricao(), result.getDescricao());
    assertEquals(source.getData(), result.getData());
    assertEquals(source.getValor(), result.getValor());
  }
}
