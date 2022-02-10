package br.com.oak.financas.api.service;

import br.com.oak.financas.api.model.dto.ReceitaDto;
import br.com.oak.financas.api.model.input.ReceitaInput;

import java.util.List;

public interface ReceitaService {

  List<ReceitaDto> listarReceitasDoUsuario(String guid, String descricao);

  List<ReceitaDto> buscarReceitasDoUsuarioNoAnoMes(String guid, Integer ano, Integer mes);

  ReceitaDto inserir(String guid, ReceitaInput receitaInput);

  void atualizar(String guid, Long id, ReceitaInput receitaInput);

  ReceitaDto detalhar(String guid, Long id);

  void excluir(String guid, Long id);
}
