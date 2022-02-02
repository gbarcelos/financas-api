package br.com.oak.financas.api.service;

import br.com.oak.financas.api.model.dto.ReceitaDto;
import br.com.oak.financas.api.model.input.ReceitaInput;

import java.util.List;

public interface ReceitaService {

  List<ReceitaDto> listar(String descricao);

  ReceitaDto inserir(ReceitaInput receitaInput);

  void atualizar(Long id, ReceitaInput receitaInput);

  ReceitaDto detalhar(Long id);

  void excluir(Long id);
}
