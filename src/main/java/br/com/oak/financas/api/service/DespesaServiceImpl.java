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
  public DespesaDto inserir(DespesaInput despesaInput) {

    Lancamento lancamento = despesaMapper.map(despesaInput);

    lancamentoService.inserir(lancamento);

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
