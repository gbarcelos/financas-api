package br.com.oak.financas.api.service.mapper;

import br.com.oak.financas.api.entity.Lancamento;
import br.com.oak.financas.api.model.input.DespesaInput;
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
public class DespesaMapperTest {

  private DespesaMapper despesaMapper;

  @BeforeAll
  public void beforeAll() {
    despesaMapper = new DespesaMapper(new ModelMapper());
  }

  @Test
  public void deveMapear_quandoTodosOsCamposEstaoInformados() {

    DespesaInput source =
        DespesaInput.builder()
            .descricao("descricao")
            .data(LocalDate.of(2022, 1, 1))
            .valor(new BigDecimal("123.41"))
            .build();

    Lancamento result = despesaMapper.map(source);

    assertEquals(source.getDescricao(), result.getDescricao());
    assertEquals(source.getData(), result.getData());
    assertEquals(source.getValor(), result.getValor());
  }
}
