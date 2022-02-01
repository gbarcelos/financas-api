package br.com.oak.financas.api.service.mapper;

import br.com.oak.financas.api.entity.Categoria;
import br.com.oak.financas.api.entity.Lancamento;
import br.com.oak.financas.api.model.input.DespesaInput;
import br.com.oak.financas.api.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DespesaMapperTest {

  @Mock private CategoriaRepository categoriaRepository;

  private DespesaMapper despesaMapper;

  @BeforeEach
  public void beforeEach() {
    despesaMapper = new DespesaMapper(new ModelMapper(), categoriaRepository);
  }

  @Test
  public void deveMapear_quandoTodosOsCamposEstaoInformados() {

    when(categoriaRepository.findById(1L))
        .thenReturn(Optional.of(Categoria.builder().id(1L).descricao("Outros").build()));

    DespesaInput source =
        DespesaInput.builder()
            .descricao("descricao")
            .data(LocalDate.of(2022, 1, 1))
            .valor(new BigDecimal("123.41"))
            .categoriaId(1L)
            .build();

    Lancamento result = despesaMapper.map(source);

    assertEquals(source.getDescricao(), result.getDescricao());
    assertEquals(source.getData(), result.getData());
    assertEquals(source.getValor(), result.getValor());
    assertNotNull(result.getCategoria());
    assertEquals(source.getCategoriaId(), result.getCategoria().getId());
    assertNotNull(result.getCategoria().getDescricao());
  }

  @Test
  public void deveMapear_quandoTodosOsCamposObrigatoriosEstaoInformados() {

    when(categoriaRepository.findById(1L))
        .thenReturn(Optional.of(Categoria.builder().id(1L).descricao("Outros").build()));

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
    assertNotNull(result.getCategoria());
    assertNotNull(result.getCategoria().getDescricao());
  }
}
