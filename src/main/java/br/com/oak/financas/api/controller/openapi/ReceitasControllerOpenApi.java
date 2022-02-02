package br.com.oak.financas.api.controller.openapi;

import br.com.oak.financas.api.model.contract.response.ContractResponse;
import br.com.oak.financas.api.model.dto.ReceitaDto;
import br.com.oak.financas.api.model.input.ReceitaInput;
import io.swagger.annotations.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "Receita")
public interface ReceitasControllerOpenApi {

  @ApiOperation("Lista as receitas cadastradas no sistema")
  @ApiResponses({@ApiResponse(code = 200, message = "Receitas listadas com sucesso")})
  ContractResponse<List<ReceitaDto>> listarReceitas(
      @ApiParam(name = "descricao", type = "String", value = "Descrição de uma receita")
          String descricao,
      HttpServletRequest request);

  @ApiOperation("Cria uma receita no sistema")
  @ApiResponses({@ApiResponse(code = 201, message = "Receita criada com sucesso")})
  ContractResponse<ReceitaDto> criarReceita(
      @ApiParam(
              name = "receitaInput",
              value = "A representação das informações para criação de uma receita")
          ReceitaInput receitaInput,
      HttpServletRequest request);

  @ApiOperation("Atualiza uma receita no sistema")
  @ApiResponses({@ApiResponse(code = 204, message = "Receita atualizada com sucesso")})
  void atualizarReceita(
      Long id,
      @ApiParam(
              name = "receitaInput",
              value = "A representação das informações para atualização de uma receita")
          ReceitaInput receitaInput,
      HttpServletRequest request);

  @ApiOperation("Detalha uma receita cadastrada no sistema")
  @ApiResponses({@ApiResponse(code = 200, message = "Receita detalhada com sucesso")})
  ContractResponse<ReceitaDto> detalharReceita(Long id, HttpServletRequest request);

  @ApiOperation("Exclui uma receita cadastrada no sistema")
  @ApiResponses({@ApiResponse(code = 204, message = "Receita excluída com sucesso")})
  void excluirReceita(Long id, HttpServletRequest request);
}
