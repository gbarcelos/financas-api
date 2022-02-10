package br.com.oak.financas.api.controller;

import br.com.oak.financas.api.controller.openapi.ResumoControllerOpenApi;
import br.com.oak.financas.api.model.contract.response.ContractResponse;
import br.com.oak.financas.api.model.dto.ResumoDto;
import br.com.oak.financas.api.security.ApiSecurity;
import br.com.oak.financas.api.security.CheckSecurity;
import br.com.oak.financas.api.service.LancamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(path = "/v1/resumo", produces = MediaType.APPLICATION_JSON_VALUE)
public class ResumoController implements ResumoControllerOpenApi {

  private final LancamentoService lancamentoService;
  private final ApiSecurity apiSecurity;

  @CheckSecurity.Resumo.PodeConsultar
  @Override
  @GetMapping("/{ano}/{mes}")
  @ResponseStatus(HttpStatus.OK)
  public ContractResponse<ResumoDto> detalharResumoDoMes(
      @PathVariable Integer ano, @PathVariable Integer mes, HttpServletRequest request) {

    return ContractResponse.<ResumoDto>builder()
        .path(request.getServletPath())
        .response(lancamentoService.detalharResumoDoMes(apiSecurity.getUsuarioGuid(), ano, mes))
        .build();
  }
}
