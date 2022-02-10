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
  public List<DespesaDto> listarDespesasDoUsuario(String guid, String descricao) {
    return despesaMapper.unmap(lancamentoService.listarDespesasDoUsuario(guid, descricao));
  }

  @Override
  public List<DespesaDto> buscarDespesasDoUsuarioNoAnoMes(String guid, Integer ano, Integer mes) {
    return despesaMapper.unmap(lancamentoService.buscarDespesasDoUsuarioNoAnoMes(guid, ano, mes));
  }

  @Override
  public DespesaDto inserir(String guid, DespesaInput despesaInput) {

    Lancamento lancamento = despesaMapper.map(despesaInput);

    lancamentoService.inserir(guid, lancamento);

    return despesaMapper.unmap(lancamento);
  }

  @Override
  public void atualizar(String guid, Long id, DespesaInput despesaInput) {
    lancamentoService.atualizar(guid, id, despesaMapper.map(despesaInput));
  }

  @Override
  public DespesaDto detalhar(String guid, Long id) {
    return despesaMapper.unmap(lancamentoService.buscarLancamentoDoUsuarioPorId(guid, id));
  }

  @Override
  public void excluir(String guid, Long id) {
    lancamentoService.excluirLancamentoDoUsuarioPorId(guid, id);
  }
}
