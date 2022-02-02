package br.com.oak.financas.api.service;

import br.com.oak.financas.api.entity.Lancamento;
import br.com.oak.financas.api.exception.BusinessException;
import br.com.oak.financas.api.exception.NotFoundException;
import br.com.oak.financas.api.model.ErrorCode;
import br.com.oak.financas.api.model.TipoLancamento;
import br.com.oak.financas.api.repository.LancamentoRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LancamentoServiceImpl implements LancamentoService {

  public static final String MSG_LANCAMENTO_JA_EXISTE = "A %s já existe";

  private final LancamentoRepository lancamentoRepository;
  private final ModelMapper modelMapper;

  @Override
  public List<Lancamento> listarReceitas(String descricao) {

    if (StringUtils.isNotBlank(descricao)) {
      return lancamentoRepository.findByTipoAndDescricaoLike(
          TipoLancamento.RECEITA, "%" + descricao + "%");
    }
    return lancamentoRepository.findAllByTipo(TipoLancamento.RECEITA);
  }

  @Override
  public List<Lancamento> listarDespesas(String descricao) {

    if (StringUtils.isNotBlank(descricao)) {
      return lancamentoRepository.findByTipoAndDescricaoLike(
          TipoLancamento.DESPESA, "%" + descricao + "%");
    }
    return lancamentoRepository.findAllByTipo(TipoLancamento.DESPESA);
  }

  @Override
  public List<Lancamento> listarReceitasPorMes(Integer ano, Integer mes) {
    return lancamentoRepository.findByTipoAndAnoAndMes(TipoLancamento.RECEITA, ano, mes);
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

  private void validarInclusao(Lancamento lancamento) {

    Optional<Lancamento> lancamentoOptional =
        lancamentoRepository.findByTipoAndDescricaoAndMes(
            lancamento.getTipo(), lancamento.getDescricao(), lancamento.getData().getMonthValue());

    validarJaExiste(lancamentoOptional);
  }

  private void validarAlteracao(Lancamento lancamento) {

    Optional<Lancamento> lancamentoOptional =
        lancamentoRepository.findByTipoAndDescricaoAndMesAndDifferentId(
            lancamento.getTipo(),
            lancamento.getDescricao(),
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
}
