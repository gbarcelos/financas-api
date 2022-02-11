package br.com.oak.financas.api.service;

import br.com.oak.financas.api.entity.Lancamento;
import br.com.oak.financas.api.entity.Usuario;
import br.com.oak.financas.api.exception.BusinessException;
import br.com.oak.financas.api.exception.NotFoundException;
import br.com.oak.financas.api.model.ErrorCode;
import br.com.oak.financas.api.model.TipoLancamento;
import br.com.oak.financas.api.repository.LancamentoRepository;
import br.com.oak.financas.api.repository.UsuarioRepository;
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

  public static final Long USUARIO_ID = 1L;
  public static final String GUID = "49E212211355438F88ACF6F859A769C0";

  @Mock private LancamentoRepository lancamentoRepository;
  @Mock private UsuarioRepository usuarioRepository;
  @Mock private ModelMapper modelMapper;

  private LancamentoService lancamentoService;

  @BeforeEach
  public void beforeEach() {
    lancamentoService =
        new LancamentoServiceImpl(lancamentoRepository, usuarioRepository, modelMapper);
  }

  @Test
  public void testInserir_cenarioDeSucesso() {
    // Arrange
    when(usuarioRepository.findByGuid(GUID))
        .thenReturn(Optional.of(Usuario.builder().id(USUARIO_ID).build()));

    when(lancamentoRepository.buscarLancamentoDoUsuarioNoMesmoDia(
            USUARIO_ID, TipoLancamento.DESPESA, "descricao", 2022, 1))
        .thenReturn(Optional.empty());

    // Act
    lancamentoService.inserir(
        GUID,
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
  public void testInserir_quandoUsuarioNaoFoiEncontrado_entaoLancaNotFoundException() {
    // Arrange
    when(usuarioRepository.findByGuid(GUID)).thenReturn(Optional.empty());

    Lancamento lancamento =
        Lancamento.builder()
            .tipo(TipoLancamento.DESPESA)
            .descricao("descricao")
            .data(LocalDate.of(2022, 1, 1))
            .valor(new BigDecimal("100.0"))
            .build();

    // Act
    NotFoundException notFoundException =
        assertThrows(NotFoundException.class, () -> lancamentoService.inserir(GUID, lancamento));

    // Assert
    assertEquals(ErrorCode.RESOURCE_NOT_FOUND, notFoundException.getErrorCode());
    assertEquals(
        String.format("O registro com o guid '%s' não existe", GUID),
        notFoundException.getFriendlyMessage());
  }

  @Test
  public void testInserir_quandoDespesaJaExiste_entaoLancaBusinessException() {
    // Arrange
    when(usuarioRepository.findByGuid(GUID))
        .thenReturn(Optional.of(Usuario.builder().id(USUARIO_ID).build()));

    when(lancamentoRepository.buscarLancamentoDoUsuarioNoMesmoDia(
            USUARIO_ID, TipoLancamento.DESPESA, "descricao", 2022, 1))
        .thenReturn(Optional.of(Lancamento.builder().tipo(TipoLancamento.DESPESA).build()));

    Lancamento lancamento =
        Lancamento.builder()
            .tipo(TipoLancamento.DESPESA)
            .descricao("descricao")
            .data(LocalDate.of(2022, 1, 1))
            .valor(new BigDecimal("100.0"))
            .build();

    // Act
    BusinessException businessException =
        assertThrows(BusinessException.class, () -> lancamentoService.inserir(GUID, lancamento));

    // Assert
    assertEquals(ErrorCode.DESPESA_JA_EXISTE, businessException.getErrorCode());
    assertEquals(
        String.format("A %s já existe", TipoLancamento.DESPESA.name().toLowerCase()),
        businessException.getFriendlyMessage());
  }

  @Test
  public void testInserir_quandoReceitaJaExiste_entaoLancaBusinessException() {
    // Arrange
    when(usuarioRepository.findByGuid(GUID))
        .thenReturn(Optional.of(Usuario.builder().id(USUARIO_ID).build()));

    when(lancamentoRepository.buscarLancamentoDoUsuarioNoMesmoDia(
            USUARIO_ID, TipoLancamento.RECEITA, "descricao", 2022, 1))
        .thenReturn(Optional.of(Lancamento.builder().tipo(TipoLancamento.RECEITA).build()));

    Lancamento lancamento =
        Lancamento.builder()
            .tipo(TipoLancamento.RECEITA)
            .descricao("descricao")
            .data(LocalDate.of(2022, 1, 1))
            .valor(new BigDecimal("100.0"))
            .build();

    // Act
    BusinessException businessException =
        assertThrows(BusinessException.class, () -> lancamentoService.inserir(GUID, lancamento));

    // Assert
    assertEquals(ErrorCode.RECEITA_JA_EXISTE, businessException.getErrorCode());
    assertEquals(
        String.format("A %s já existe", TipoLancamento.RECEITA.name().toLowerCase()),
        businessException.getFriendlyMessage());
  }
}
