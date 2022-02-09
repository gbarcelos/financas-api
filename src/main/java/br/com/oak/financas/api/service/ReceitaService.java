package br.com.oak.financas.api.service;

import br.com.oak.financas.api.model.dto.ReceitaDto;
import br.com.oak.financas.api.model.input.ReceitaInput;

import java.util.List;

public interface ReceitaService {

  List<ReceitaDto> listar(String descricao);

  List<ReceitaDto> listarReceitasPorMes(Integer ano, Integer mes);

  ReceitaDto inserir(String guid, ReceitaInput receitaInput);

  void atualizar(Long id, ReceitaInput receitaInput);

  ReceitaDto detalhar(Long id);

  void excluir(Long id);
}
