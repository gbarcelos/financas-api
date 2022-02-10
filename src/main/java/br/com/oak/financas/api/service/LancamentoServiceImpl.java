package br.com.oak.financas.api.service;

import br.com.oak.financas.api.entity.Lancamento;
import br.com.oak.financas.api.entity.Usuario;
import br.com.oak.financas.api.exception.BusinessException;
import br.com.oak.financas.api.exception.NotFoundException;
import br.com.oak.financas.api.model.ErrorCode;
import br.com.oak.financas.api.model.TipoLancamento;
import br.com.oak.financas.api.model.dto.DespesasPorCategoriaDto;
import br.com.oak.financas.api.model.dto.ResumoDto;
import br.com.oak.financas.api.repository.LancamentoRepository;
import br.com.oak.financas.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LancamentoServiceImpl implements LancamentoService {

  public static final String MSG_LANCAMENTO_JA_EXISTE = "A %s já existe";

  private final LancamentoRepository lancamentoRepository;
  private final UsuarioRepository usuarioRepository;
  private final ModelMapper modelMapper;

  @Override
  public List<Lancamento> listarReceitasDoUsuario(String guid, String descricao) {
    return listar(guid, TipoLancamento.RECEITA, descricao);
  }

  @Override
  public List<Lancamento> listarDespesasDoUsuario(String guid, String descricao) {
    return listar(guid, TipoLancamento.DESPESA, descricao);
  }

  @Override
  public List<Lancamento> buscarReceitasDoUsuarioNoAnoMes(String guid, Integer ano, Integer mes) {
    return lancamentoRepository.buscarLancamentosDoUsuarioNoAnoMes(
        guid, TipoLancamento.RECEITA, ano, mes);
  }

  @Override
  public List<Lancamento> buscarDespesasDoUsuarioNoAnoMes(String guid, Integer ano, Integer mes) {
    return lancamentoRepository.buscarLancamentosDoUsuarioNoAnoMes(
        guid, TipoLancamento.DESPESA, ano, mes);
  }

  @Override
  public ResumoDto detalharResumoDoMes(String guid, Integer ano, Integer mes) {

    BigDecimal totalReceitas =
        getTotalLancamentosDoUsuarioPorAnoMes(guid, TipoLancamento.RECEITA, ano, mes);

    BigDecimal totalDespesas =
        getTotalLancamentosDoUsuarioPorAnoMes(guid, TipoLancamento.DESPESA, ano, mes);

    BigDecimal saldo = totalReceitas.subtract(totalDespesas);

    List<DespesasPorCategoriaDto> despesasPorCategoria =
        lancamentoRepository.obterTotalLancamentosDoUsuarioPorAnoMesCategoria(
            guid, TipoLancamento.DESPESA, ano, mes);

    return ResumoDto.builder()
        .totalDasReceitas(totalReceitas)
        .totalDasDespesas(totalDespesas)
        .saldoFinal(saldo)
        .despesasPorCategoria(despesasPorCategoria)
        .build();
  }

  @Override
  public void inserir(String guid, Lancamento lancamento) {

    lancamento.setUsuario(buscarUsuarioPeloGuid(guid));

    validarInclusao(lancamento);

    lancamentoRepository.save(lancamento);
  }

  @Override
  public void atualizar(String guid, Long id, Lancamento lancamento) {

    Lancamento lancamentoBanco = buscarLancamentoDoUsuarioPorId(guid, id);

    modelMapper.map(lancamento, lancamentoBanco);

    validarAlteracao(lancamentoBanco);

    lancamentoRepository.save(lancamentoBanco);
  }

  @Override
  public void excluirLancamentoDoUsuarioPorId(String guid, Long id) {
    lancamentoRepository.delete(buscarLancamentoDoUsuarioPorId(guid, id));
  }

  @Override
  public Lancamento buscarLancamentoDoUsuarioPorId(String guid, Long id) {

    return lancamentoRepository
        .buscarLancamentoDoUsuarioPorId(guid, id)
        .orElseThrow(
            () ->
                new NotFoundException(
                    ErrorCode.RESOURCE_NOT_FOUND,
                    String.format("O registro com o id '%s' não existe", id)));
  }

  public Usuario buscarUsuarioPeloGuid(String guid) {
    return usuarioRepository
        .findByGuid(guid)
        .orElseThrow(
            () ->
                new NotFoundException(
                    ErrorCode.RESOURCE_NOT_FOUND,
                    String.format("O registro com o guid '%s' não existe", guid)));
  }

  private List<Lancamento> listar(String guid, TipoLancamento tipo, String descricao) {

    if (StringUtils.isNotBlank(descricao)) {
      return lancamentoRepository.findAllByUsuarioGuidAndTipoAndDescricaoLike(
          guid, tipo, "%" + descricao + "%");
    }
    return lancamentoRepository.findAllByUsuarioGuidAndTipo(guid, tipo);
  }

  private void validarInclusao(Lancamento lancamento) {

    Optional<Lancamento> lancamentoOptional =
        lancamentoRepository.buscarLancamentoDoUsuarioNoMesmoDia(
            lancamento.getUsuario().getId(),
            lancamento.getTipo(),
            lancamento.getDescricao(),
            lancamento.getData().getYear(),
            lancamento.getData().getMonthValue());

    validarJaExiste(lancamentoOptional);
  }

  private void validarAlteracao(Lancamento lancamento) {

    Optional<Lancamento> lancamentoOptional =
        lancamentoRepository.buscarLancamentoDoUsuarioNoMesmoDiaParaAlteracao(
            lancamento.getUsuario().getId(),
            lancamento.getTipo(),
            lancamento.getDescricao(),
            lancamento.getData().getYear(),
            lancamento.getData().getMonthValue(),
            lancamento.getId());

    validarJaExiste(lancamentoOptional);
  }

  private void validarJaExiste(Optional<Lancamento> lancamentoOptional) {

    if (lancamentoOptional.isPresent()) {

      log.warn(
          "O lancamento já existe. Encontrado o lançamento com o id {} com os mesmos dados.",
          lancamentoOptional.get().getId());
      if (TipoLancamento.RECEITA.equals(lancamentoOptional.get().getTipo())) {
        throw new BusinessException(
            ErrorCode.RECEITA_JA_EXISTE,
            String.format(MSG_LANCAMENTO_JA_EXISTE, TipoLancamento.RECEITA.name().toLowerCase()));
      }
      throw new BusinessException(
          ErrorCode.DESPESA_JA_EXISTE,
          String.format(MSG_LANCAMENTO_JA_EXISTE, TipoLancamento.DESPESA.name().toLowerCase()));
    }
  }

  private BigDecimal getTotalLancamentosDoUsuarioPorAnoMes(
      String guid, TipoLancamento tipo, Integer ano, Integer mes) {

    BigDecimal totalDasReceitas =
        lancamentoRepository.obterTotalLancamentosDoUsuarioPorAnoMes(guid, tipo, ano, mes);

    if (Objects.nonNull(totalDasReceitas)) {
      return totalDasReceitas;
    }
    return BigDecimal.ZERO;
  }
}
