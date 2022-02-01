package br.com.oak.financas.api.service;

import br.com.oak.financas.api.entity.Lancamento;
import br.com.oak.financas.api.exception.BusinessException;
import br.com.oak.financas.api.model.ErrorCode;
import br.com.oak.financas.api.model.TipoLancamento;
import br.com.oak.financas.api.repository.LancamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LancamentoServiceAtualizarTest {

  @Mock private LancamentoRepository lancamentoRepository;

  @Captor private ArgumentCaptor<Lancamento> captor;

  private ModelMapper modelMapper;

  private LancamentoService lancamentoService;

  @BeforeEach
  public void beforeEach() {
    modelMapper = new ModelMapper();
    modelMapper
        .createTypeMap(Lancamento.class, Lancamento.class)
        .addMappings(mapper -> mapper.skip(Lancamento::setId));

    lancamentoService = new LancamentoServiceImpl(lancamentoRepository, modelMapper);
  }

  @Test
  public void testAtualizar_cenarioDeSucesso() {
    // Arrange
    when(lancamentoRepository.findById(1L))
        .thenReturn(Optional.of(Lancamento.builder().id(1L).build()));

    when(lancamentoRepository.findByTipoAndDescricaoAndMesAndDifferentId(
            TipoLancamento.DESPESA, "descricao, alterado", 1, 1L))
        .thenReturn(Optional.empty());

    // Act
    lancamentoService.atualizar(
        1l,
        Lancamento.builder()
            .tipo(TipoLancamento.DESPESA)
            .descricao("descricao, alterado")
            .data(LocalDate.of(2022, 1, 1))
            .valor(new BigDecimal("100.0"))
            .build());

    // Assert
    verify(lancamentoRepository, times(1)).save(captor.capture());

    assertNotNull(captor.getValue().getId());
    assertEquals("descricao, alterado", captor.getValue().getDescricao());
    assertEquals(TipoLancamento.DESPESA, captor.getValue().getTipo());
  }

  @Test
  public void testAtualizar_quandoLancamentoJaExiste_entaoLancaBusinessException() {
    // Arrange
    when(lancamentoRepository.findById(1L))
        .thenReturn(Optional.of(Lancamento.builder().id(1L).build()));

    when(lancamentoRepository.findByTipoAndDescricaoAndMesAndDifferentId(
            TipoLancamento.DESPESA, "descricao, alterado", 1, 1L))
        .thenReturn(Optional.of(Lancamento.builder().build()));

    Lancamento lancamentoAlterado =
        Lancamento.builder()
            .tipo(TipoLancamento.DESPESA)
            .descricao("descricao, alterado")
            .data(LocalDate.of(2022, 1, 1))
            .valor(new BigDecimal("100.0"))
            .build();

    // Act
    BusinessException businessException =
        assertThrows(
            BusinessException.class, () -> lancamentoService.atualizar(1l, lancamentoAlterado));

    // Assert
    assertEquals(ErrorCode.DESPESA_JA_EXISTE, businessException.getErrorCode());
    assertEquals(
        String.format("A %s já existe", TipoLancamento.DESPESA.name().toLowerCase()),
        businessException.getFriendlyMessage());
  }
}
