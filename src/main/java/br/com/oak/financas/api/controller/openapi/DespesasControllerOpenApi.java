package br.com.oak.financas.api.controller.openapi;

import br.com.oak.financas.api.model.contract.response.ContractResponse;
import br.com.oak.financas.api.model.dto.DespesaDto;
import br.com.oak.financas.api.model.input.DespesaInput;
import io.swagger.annotations.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "Despesa")
public interface DespesasControllerOpenApi {

  @ApiOperation("Lista as despesas cadastradas no sistema")
  @ApiResponses({@ApiResponse(code = 200, message = "Despesas listadas com sucesso")})
  ContractResponse<List<DespesaDto>> listarDespesas(
      @ApiParam(name = "descricao", type = "String", value = "Descrição de uma despesa")
          String descricao,
      HttpServletRequest request);

  @ApiOperation("Lista as despesas cadastradas no sistema por ano e mês")
  @ApiResponses({@ApiResponse(code = 200, message = "Despesas listadas com sucesso")})
  ContractResponse<List<DespesaDto>> listarDespesasPorMes(
      Integer ano, Integer mes, HttpServletRequest request);

  @ApiOperation("Cria uma despesa no sistema")
  @ApiResponses({@ApiResponse(code = 201, message = "Despesa criada com sucesso")})
  ContractResponse<DespesaDto> criarDespesa(
      @ApiParam(
              name = "despesaInput",
              value = "A representação das informações para criação de uma despesa")
          DespesaInput despesaInput,
      HttpServletRequest request);

  @ApiOperation("Atualiza uma despesa no sistema")
  @ApiResponses({@ApiResponse(code = 204, message = "Despesa atualizada com sucesso")})
  void atualizarDespesa(
      Long id,
      @ApiParam(
              name = "despesaInput",
              value = "A representação das informações para atualização de uma despesa")
          DespesaInput despesaInput,
      HttpServletRequest request);

  @ApiOperation("Detalha uma despesa cadastrada no sistema")
  @ApiResponses({@ApiResponse(code = 200, message = "Despesa detalhada com sucesso")})
  ContractResponse<DespesaDto> detalharDespesas(Long id, HttpServletRequest request);

  @ApiOperation("Exclui uma despesa cadastrada no sistema")
  @ApiResponses({@ApiResponse(code = 204, message = "Despesa excluída com sucesso")})
  void excluirReceita(Long id, HttpServletRequest request);
}
