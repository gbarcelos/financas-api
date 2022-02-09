package br.com.oak.financas.api.service;

import br.com.oak.financas.api.entity.Lancamento;
import br.com.oak.financas.api.model.dto.ReceitaDto;
import br.com.oak.financas.api.model.input.ReceitaInput;
import br.com.oak.financas.api.service.mapper.ReceitaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReceitaServiceImpl implements ReceitaService {

  private final LancamentoService lancamentoService;
  private final ReceitaMapper receitaMapper;

  @Override
  public List<ReceitaDto> listar(String descricao) {
    return receitaMapper.unmap(lancamentoService.listarReceitas(descricao));
  }

  @Override
  public List<ReceitaDto> listarReceitasPorMes(Integer ano, Integer mes) {
    return receitaMapper.unmap(lancamentoService.listarReceitasPorMes(ano, mes));
  }

  @Override
  public ReceitaDto inserir(String guid, ReceitaInput receitaInput) {

    Lancamento lancamento = receitaMapper.map(receitaInput);

    lancamentoService.inserir(guid, lancamento);

    return receitaMapper.unmap(lancamento);
  }

  @Override
  public void atualizar(Long id, ReceitaInput receitaInput) {
    lancamentoService.atualizar(id, receitaMapper.map(receitaInput));
  }

  @Override
  public ReceitaDto detalhar(Long id) {
    return receitaMapper.unmap(lancamentoService.buscarPeloId(id));
  }

  @Override
  public void excluir(Long id) {
    lancamentoService.excluir(id);
  }
}
