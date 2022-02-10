package br.com.oak.financas.api.service;

import br.com.oak.financas.api.entity.Lancamento;
import br.com.oak.financas.api.model.dto.ResumoDto;

import java.util.List;

public interface LancamentoService {

  List<Lancamento> listarReceitasDoUsuario(String guid, String descricao);

  List<Lancamento> listarDespesasDoUsuario(String guid, String descricao);

  List<Lancamento> buscarReceitasDoUsuarioNoAnoMes(String guid, Integer ano, Integer mes);

  List<Lancamento> buscarDespesasDoUsuarioNoAnoMes(String guid, Integer ano, Integer mes);

  ResumoDto detalharResumoDoMes(String guid, Integer ano, Integer mes);

  void inserir(String guid, Lancamento lancamento);

  void atualizar(String guid, Long id, Lancamento lancamento);

  void excluirLancamentoDoUsuarioPorId(String guid, Long id);

  Lancamento buscarLancamentoDoUsuarioPorId(String guid, Long id);
}
