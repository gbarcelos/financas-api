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
  public List<ReceitaDto> listarReceitasDoUsuario(String guid, String descricao) {
    return receitaMapper.unmap(lancamentoService.listarReceitasDoUsuario(guid, descricao));
  }

  @Override
  public List<ReceitaDto> buscarReceitasDoUsuarioNoAnoMes(String guid, Integer ano, Integer mes) {
    return receitaMapper.unmap(lancamentoService.buscarReceitasDoUsuarioNoAnoMes(guid, ano, mes));
  }

  @Override
  public ReceitaDto inserir(String guid, ReceitaInput receitaInput) {

    Lancamento lancamento = receitaMapper.map(receitaInput);

    lancamentoService.inserir(guid, lancamento);

    return receitaMapper.unmap(lancamento);
  }

  @Override
  public void atualizar(String guid, Long id, ReceitaInput receitaInput) {
    lancamentoService.atualizar(guid, id, receitaMapper.map(receitaInput));
  }

  @Override
  public ReceitaDto detalhar(String guid, Long id) {
    return receitaMapper.unmap(lancamentoService.buscarLancamentoDoUsuarioPorId(guid, id));
  }

  @Override
  public void excluir(String guid, Long id) {
    lancamentoService.excluirLancamentoDoUsuarioPorId(guid, id);
  }
}
