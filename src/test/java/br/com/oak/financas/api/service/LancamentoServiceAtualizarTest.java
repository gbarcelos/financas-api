package br.com.oak.financas.api.service;

import br.com.oak.financas.api.entity.Categoria;
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

  public static final Long USUARIO_ID = 1L;
  public static final String GUID = "49E212211355438F88ACF6F859A769C0";
  public static final Long LANCAMENTO_ID = 1L;

  @Mock private LancamentoRepository lancamentoRepository;
  @Mock private UsuarioRepository usuarioRepository;

  @Captor private ArgumentCaptor<Lancamento> captor;

  private ModelMapper modelMapper;

  private LancamentoService lancamentoService;

  @BeforeEach
  public void beforeEach() {
    modelMapper = new ModelMapper();
    modelMapper
        .createTypeMap(Lancamento.class, Lancamento.class)
        .addMappings(
            mapper -> {
              mapper.skip(Lancamento::setId);
              mapper.skip(Lancamento::setUsuario);
            });

    lancamentoService =
        new LancamentoServiceImpl(lancamentoRepository, usuarioRepository, modelMapper);
  }

  @Test
  public void testAtualizar_cenarioDeSucesso() {
    // Arrange
    Lancamento lancamentoBanco =
        Lancamento.builder()
            .id(LANCAMENTO_ID)
            .usuario(Usuario.builder().id(USUARIO_ID).guid(GUID).build())
            .tipo(TipoLancamento.DESPESA)
            .descricao("descricao")
            .data(LocalDate.of(2022, 1, 1))
            .valor(new BigDecimal("100.0"))
            .categoria(Categoria.builder().id(1L).descricao("Outros").build())
            .build();

    when(lancamentoRepository.buscarLancamentoDoUsuarioPorId(GUID, LANCAMENTO_ID))
        .thenReturn(Optional.of(lancamentoBanco));

    Lancamento lancamentoAlterado =
        Lancamento.builder()
            .tipo(TipoLancamento.DESPESA)
            .descricao("descricao, alterado")
            .data(LocalDate.of(2022, 1, 1))
            .valor(new BigDecimal("100.0"))
            .categoria(Categoria.builder().id(1L).descricao("Outros").build())
            .build();

    when(lancamentoRepository.buscarLancamentoDoUsuarioNoMesmoDiaParaAlteracao(
            lancamentoBanco.getUsuario().getId(),
            lancamentoAlterado.getTipo(),
            lancamentoAlterado.getDescricao(),
            lancamentoAlterado.getData().getYear(),
            lancamentoAlterado.getData().getMonthValue(),
            lancamentoBanco.getId()))
        .thenReturn(Optional.empty());

    // Act
    lancamentoService.atualizar(GUID, LANCAMENTO_ID, lancamentoAlterado);

    // Assert
    verify(lancamentoRepository, times(1)).save(captor.capture());

    assertNotNull(captor.getValue().getId());
    assertEquals("descricao, alterado", captor.getValue().getDescricao());
    assertEquals(TipoLancamento.DESPESA, captor.getValue().getTipo());
  }

  @Test
  public void testAtualizar_quandoUsuarioNaoFoiEncontrado_entaoLancaNotFoundException() {
    // Arrange
    when(lancamentoRepository.buscarLancamentoDoUsuarioPorId(GUID, LANCAMENTO_ID))
        .thenReturn(Optional.empty());

    Lancamento lancamento =
        Lancamento.builder()
            .tipo(TipoLancamento.DESPESA)
            .descricao("descricao")
            .data(LocalDate.of(2022, 1, 1))
            .valor(new BigDecimal("100.0"))
            .build();

    // Act
    NotFoundException notFoundException =
        assertThrows(
            NotFoundException.class,
            () -> lancamentoService.atualizar(GUID, LANCAMENTO_ID, lancamento));

    // Assert
    assertEquals(ErrorCode.RESOURCE_NOT_FOUND, notFoundException.getErrorCode());
    assertEquals(
        String.format("O registro com o id '%s' não existe", LANCAMENTO_ID),
        notFoundException.getFriendlyMessage());
  }

  @Test
  public void testAtualizar_quandoDespesaJaExiste_entaoLancaBusinessException() {
    // Arrange
    Lancamento lancamentoBanco =
        Lancamento.builder()
            .id(LANCAMENTO_ID)
            .usuario(Usuario.builder().id(USUARIO_ID).guid(GUID).build())
            .tipo(TipoLancamento.DESPESA)
            .descricao("descricao")
            .data(LocalDate.of(2022, 1, 1))
            .valor(new BigDecimal("100.0"))
            .categoria(Categoria.builder().id(1L).descricao("Outros").build())
            .build();

    when(lancamentoRepository.buscarLancamentoDoUsuarioPorId(GUID, LANCAMENTO_ID))
        .thenReturn(Optional.of(lancamentoBanco));

    Lancamento lancamentoAlterado =
        Lancamento.builder()
            .tipo(TipoLancamento.DESPESA)
            .descricao("descricao, alterado")
            .data(LocalDate.of(2022, 1, 1))
            .valor(new BigDecimal("100.0"))
            .categoria(Categoria.builder().id(1L).descricao("Outros").build())
            .build();

    when(lancamentoRepository.buscarLancamentoDoUsuarioNoMesmoDiaParaAlteracao(
            lancamentoBanco.getUsuario().getId(),
            lancamentoAlterado.getTipo(),
            lancamentoAlterado.getDescricao(),
            lancamentoAlterado.getData().getYear(),
            lancamentoAlterado.getData().getMonthValue(),
            lancamentoBanco.getId()))
        .thenReturn(Optional.of(Lancamento.builder().build()));

    // Act
    BusinessException businessException =
        assertThrows(
            BusinessException.class,
            () -> lancamentoService.atualizar(GUID, LANCAMENTO_ID, lancamentoAlterado));

    // Assert
    assertEquals(ErrorCode.DESPESA_JA_EXISTE, businessException.getErrorCode());
    assertEquals(
        String.format("A %s já existe", TipoLancamento.DESPESA.name().toLowerCase()),
        businessException.getFriendlyMessage());
  }

  @Test
  public void testAtualizar_quandoReceitaJaExiste_entaoLancaBusinessException() {
    // Arrange
    Lancamento lancamentoBanco =
        Lancamento.builder()
            .id(LANCAMENTO_ID)
            .usuario(Usuario.builder().id(USUARIO_ID).guid(GUID).build())
            .tipo(TipoLancamento.RECEITA)
            .descricao("descricao")
            .data(LocalDate.of(2022, 1, 1))
            .valor(new BigDecimal("100.0"))
            .categoria(Categoria.builder().id(1L).descricao("Outros").build())
            .build();

    when(lancamentoRepository.buscarLancamentoDoUsuarioPorId(GUID, LANCAMENTO_ID))
        .thenReturn(Optional.of(lancamentoBanco));

    Lancamento lancamentoAlterado =
        Lancamento.builder()
            .tipo(TipoLancamento.RECEITA)
            .descricao("descricao, alterado")
            .data(LocalDate.of(2022, 1, 1))
            .valor(new BigDecimal("100.0"))
            .categoria(Categoria.builder().id(1L).descricao("Outros").build())
            .build();

    when(lancamentoRepository.buscarLancamentoDoUsuarioNoMesmoDiaParaAlteracao(
            lancamentoBanco.getUsuario().getId(),
            lancamentoAlterado.getTipo(),
            lancamentoAlterado.getDescricao(),
            lancamentoAlterado.getData().getYear(),
            lancamentoAlterado.getData().getMonthValue(),
            lancamentoBanco.getId()))
        .thenReturn(Optional.of(Lancamento.builder().tipo(TipoLancamento.RECEITA).build()));

    // Act
    BusinessException businessException =
        assertThrows(
            BusinessException.class,
            () -> lancamentoService.atualizar(GUID, LANCAMENTO_ID, lancamentoAlterado));

    // Assert
    assertEquals(ErrorCode.RECEITA_JA_EXISTE, businessException.getErrorCode());
    assertEquals(
        String.format("A %s já existe", TipoLancamento.RECEITA.name().toLowerCase()),
        businessException.getFriendlyMessage());
  }
}
