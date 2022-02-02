package br.com.oak.financas.api.service;

import br.com.oak.financas.api.entity.Lancamento;

import java.util.List;

public interface LancamentoService {

  List<Lancamento> listarReceitas(String descricao);

  List<Lancamento> listarDespesas(String descricao);

  List<Lancamento> listarReceitasPorMes(Integer ano, Integer mes);

  List<Lancamento> listarDespesasPorMes(Integer ano, Integer mes);

  void inserir(Lancamento lancamento);

  void atualizar(Long id, Lancamento lancamento);

  void excluir(Long id);

  Lancamento buscarPeloId(Long id);
}
