package br.com.oak.financas.api.service;

import br.com.oak.financas.api.entity.Lancamento;
import br.com.oak.financas.api.exception.BusinessException;
import br.com.oak.financas.api.exception.NotFoundException;
import br.com.oak.financas.api.model.ErrorCode;
import br.com.oak.financas.api.model.TipoLancamento;
import br.com.oak.financas.api.model.dto.DespesasPorCategoriaDto;
import br.com.oak.financas.api.model.dto.ResumoDto;
import br.com.oak.financas.api.repository.LancamentoRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LancamentoServiceImpl implements LancamentoService {

  public static final String MSG_LANCAMENTO_JA_EXISTE = "A %s já existe";

  private final LancamentoRepository lancamentoRepository;
  private final ModelMapper modelMapper;

  @Override
  public List<Lancamento> listarReceitas(String descricao) {
    return listar(TipoLancamento.RECEITA, descricao);
  }

  @Override
  public List<Lancamento> listarDespesas(String descricao) {
    return listar(TipoLancamento.DESPESA, descricao);
  }

  @Override
  public List<Lancamento> listarReceitasPorMes(Integer ano, Integer mes) {
    return lancamentoRepository.findByTipoAndAnoAndMes(TipoLancamento.RECEITA, ano, mes);
  }

  @Override
  public List<Lancamento> listarDespesasPorMes(Integer ano, Integer mes) {
    return lancamentoRepository.findByTipoAndAnoAndMes(TipoLancamento.DESPESA, ano, mes);
  }

  @Override
  public ResumoDto detalharResumoDoMes(Integer ano, Integer mes) {

    BigDecimal totalDasReceitas =
        getValorTotalLancamentosPorTipoAnoMes(TipoLancamento.RECEITA, ano, mes);

    BigDecimal totalDasDespesas =
        getValorTotalLancamentosPorTipoAnoMes(TipoLancamento.DESPESA, ano, mes);

    BigDecimal saldoFinal = totalDasReceitas.subtract(totalDasDespesas);

    List<DespesasPorCategoriaDto> despesasPorCategoria =
        lancamentoRepository.valorTotalLancamentosPorTipoAnoMesPorCategoria(
            TipoLancamento.DESPESA, ano, mes);

    return ResumoDto.builder()
        .totalDasReceitas(totalDasReceitas)
        .totalDasDespesas(totalDasDespesas)
        .saldoFinal(saldoFinal)
        .despesasPorCategoria(despesasPorCategoria)
        .build();
  }

  @Override
  public void inserir(Lancamento lancamento) {

    validarInclusao(lancamento);

    lancamentoRepository.save(lancamento);
  }

  @Override
  public void atualizar(Long id, Lancamento lancamento) {

    Lancamento lancamentoBanco = buscarPeloId(id);

    modelMapper.map(lancamento, lancamentoBanco);

    validarAlteracao(lancamentoBanco);

    lancamentoRepository.save(lancamentoBanco);
  }

  @Override
  public void excluir(Long id) {
    lancamentoRepository.delete(buscarPeloId(id));
  }

  @Override
  public Lancamento buscarPeloId(Long id) {
    return lancamentoRepository
        .findById(id)
        .orElseThrow(
            () ->
                new NotFoundException(
                    ErrorCode.RESOURCE_NOT_FOUND,
                    String.format("O registro com o id '%s' não existe", id)));
  }

  private List<Lancamento> listar(TipoLancamento tipo, String descricao) {

    if (StringUtils.isNotBlank(descricao)) {
      return lancamentoRepository.findByTipoAndDescricaoLike(tipo, "%" + descricao + "%");
    }
    return lancamentoRepository.findAllByTipo(tipo);
  }

  private void validarInclusao(Lancamento lancamento) {

    Optional<Lancamento> lancamentoOptional =
        lancamentoRepository.findByTipoAndDescricaoAndAnoAndMes(
            lancamento.getTipo(),
            lancamento.getDescricao(),
            lancamento.getData().getYear(),
            lancamento.getData().getMonthValue());

    validarJaExiste(lancamentoOptional);
  }

  private void validarAlteracao(Lancamento lancamento) {

    Optional<Lancamento> lancamentoOptional =
        lancamentoRepository.findByTipoAndDescricaoAndAnoAndMesAndDifferentId(
            lancamento.getTipo(),
            lancamento.getDescricao(),
            lancamento.getData().getYear(),
            lancamento.getData().getMonthValue(),
            lancamento.getId());

    validarJaExiste(lancamentoOptional);
  }

  private void validarJaExiste(Optional<Lancamento> lancamentoOptional) {

    if (lancamentoOptional.isPresent()) {

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

  private BigDecimal getValorTotalLancamentosPorTipoAnoMes(
      TipoLancamento tipo, Integer ano, Integer mes) {

    BigDecimal totalDasReceitas =
        lancamentoRepository.valorTotalLancamentosPorTipoAnoMes(tipo, ano, mes);

    if (Objects.nonNull(totalDasReceitas)) {
      return totalDasReceitas;
    }
    return BigDecimal.ZERO;
  }
}
