package br.com.oak.financas.api.service;

import br.com.oak.financas.api.entity.Lancamento;
import br.com.oak.financas.api.exception.BusinessException;
import br.com.oak.financas.api.model.ErrorCode;
import br.com.oak.financas.api.model.TipoLancamento;
import br.com.oak.financas.api.repository.LancamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LancamentoServiceInserirTest {

  @Mock private LancamentoRepository lancamentoRepository;
  @Mock private ModelMapper modelMapper;

  private LancamentoService lancamentoService;

  @BeforeEach
  public void beforeEach() {
    lancamentoService = new LancamentoServiceImpl(lancamentoRepository, null, modelMapper);
  }

  @Test
  public void testInserir_cenarioDeSucesso() {
    // Arrange
    when(lancamentoRepository.buscarLancamentoNoMesmoDia(
            1L, TipoLancamento.DESPESA, "descricao", 2022, 1))
        .thenReturn(Optional.empty());

    // Act
    lancamentoService.inserir(
        null,
        Lancamento.builder()
            .tipo(TipoLancamento.DESPESA)
            .descricao("descricao")
            .data(LocalDate.of(2022, 1, 1))
            .valor(new BigDecimal("100.0"))
            .build());

    // Assert
    verify(lancamentoRepository, times(1)).save(any(Lancamento.class));
  }

  @Test
  public void testInserir_quandoLancamentoJaExiste_entaoLancaBusinessException() {
    // Arrange
    when(lancamentoRepository.buscarLancamentoNoMesmoDia(
            1L, TipoLancamento.DESPESA, "descricao", 2022, 1))
        .thenReturn(Optional.of(Lancamento.builder().build()));

    Lancamento lancamento =
        Lancamento.builder()
            .tipo(TipoLancamento.DESPESA)
            .descricao("descricao")
            .data(LocalDate.of(2022, 1, 1))
            .valor(new BigDecimal("100.0"))
            .build();

    // Act
    BusinessException businessException =
        assertThrows(BusinessException.class, () -> lancamentoService.inserir(null, lancamento));

    // Assert
    assertEquals(ErrorCode.DESPESA_JA_EXISTE, businessException.getErrorCode());
    assertEquals(
        String.format("A %s já existe", TipoLancamento.DESPESA.name().toLowerCase()),
        businessException.getFriendlyMessage());
  }
}
