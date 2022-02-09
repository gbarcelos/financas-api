package br.com.oak.financas.api.service;

import br.com.oak.financas.api.model.dto.DespesaDto;
import br.com.oak.financas.api.model.input.DespesaInput;

import java.util.List;

public interface DespesaService {

  List<DespesaDto> listar(String descricao);

  List<DespesaDto> listarDespesasPorMes(Integer ano, Integer mes);

  DespesaDto inserir(String guid, DespesaInput despesaInput);

  void atualizar(Long id, DespesaInput despesaInput);

  DespesaDto detalhar(Long id);

  void excluir(Long id);
}
