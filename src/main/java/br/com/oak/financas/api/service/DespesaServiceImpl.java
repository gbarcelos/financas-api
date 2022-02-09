package br.com.oak.financas.api.service;

import br.com.oak.financas.api.entity.Lancamento;
import br.com.oak.financas.api.model.dto.DespesaDto;
import br.com.oak.financas.api.model.input.DespesaInput;
import br.com.oak.financas.api.service.mapper.DespesaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DespesaServiceImpl implements DespesaService {

  private final LancamentoService lancamentoService;
  private final DespesaMapper despesaMapper;

  @Override
  public List<DespesaDto> listar(String descricao) {
    return despesaMapper.unmap(lancamentoService.listarDespesas(descricao));
  }

  @Override
  public List<DespesaDto> listarDespesasPorMes(Integer ano, Integer mes) {
    return despesaMapper.unmap(lancamentoService.listarDespesasPorMes(ano, mes));
  }

  @Override
  public DespesaDto inserir(String guid, DespesaInput despesaInput) {

    Lancamento lancamento = despesaMapper.map(despesaInput);

    lancamentoService.inserir(guid, lancamento);

    return despesaMapper.unmap(lancamento);
  }

  @Override
  public void atualizar(Long id, DespesaInput despesaInput) {
    lancamentoService.atualizar(id, despesaMapper.map(despesaInput));
  }

  @Override
  public DespesaDto detalhar(Long id) {
    return despesaMapper.unmap(lancamentoService.buscarPeloId(id));
  }

  @Override
  public void excluir(Long id) {
    lancamentoService.excluir(id);
  }
}
