package br.com.oak.financas.api.controller.openapi;

import br.com.oak.financas.api.model.contract.response.ContractResponse;
import br.com.oak.financas.api.model.dto.ResumoDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "Consultas")
public interface ResumoControllerOpenApi {

  @ApiOperation("Detalha o resumo de um determinado ano e mês")
  @ApiResponses({@ApiResponse(code = 200, message = "Resumo obtido com sucesso")})
  ContractResponse<ResumoDto> detalharResumoDoMes(
      Integer ano, Integer mes, HttpServletRequest request);
}
