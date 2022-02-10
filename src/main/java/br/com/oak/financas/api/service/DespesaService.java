package br.com.oak.financas.api.service;

import br.com.oak.financas.api.model.dto.DespesaDto;
import br.com.oak.financas.api.model.input.DespesaInput;

import java.util.List;

public interface DespesaService {

  List<DespesaDto> listarDespesasDoUsuario(String guid, String descricao);

  List<DespesaDto> buscarDespesasDoUsuarioNoAnoMes(String guid, Integer ano, Integer mes);

  DespesaDto inserir(String guid, DespesaInput despesaInput);

  void atualizar(String guid, Long id, DespesaInput despesaInput);

  DespesaDto detalhar(String guid, Long id);

  void excluir(String guid, Long id);
}
