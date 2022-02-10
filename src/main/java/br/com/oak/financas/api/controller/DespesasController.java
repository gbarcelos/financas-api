package br.com.oak.financas.api.controller;

import br.com.oak.financas.api.controller.openapi.DespesasControllerOpenApi;
import br.com.oak.financas.api.model.contract.response.ContractResponse;
import br.com.oak.financas.api.model.dto.DespesaDto;
import br.com.oak.financas.api.model.input.DespesaInput;
import br.com.oak.financas.api.security.ApiSecurity;
import br.com.oak.financas.api.security.CheckSecurity;
import br.com.oak.financas.api.service.DespesaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(path = "/v1/despesas", produces = MediaType.APPLICATION_JSON_VALUE)
public class DespesasController implements DespesasControllerOpenApi {

  private final DespesaService despesaService;
  private final ApiSecurity apiSecurity;

  @CheckSecurity.Despesas.PodeConsultar
  @Override
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public ContractResponse<List<DespesaDto>> listarDespesas(
      @RequestParam(required = false) String descricao, HttpServletRequest request) {

    return ContractResponse.<List<DespesaDto>>builder()
        .path(request.getServletPath())
        .response(despesaService.listarDespesasDoUsuario(apiSecurity.getUsuarioGuid(), descricao))
        .build();
  }

  @CheckSecurity.Despesas.PodeConsultar
  @Override
  @GetMapping("/{ano}/{mes}")
  @ResponseStatus(HttpStatus.OK)
  public ContractResponse<List<DespesaDto>> buscarDespesasNoAnoMes(
      @PathVariable Integer ano, @PathVariable Integer mes, HttpServletRequest request) {

    return ContractResponse.<List<DespesaDto>>builder()
        .path(request.getServletPath())
        .response(
            despesaService.buscarDespesasDoUsuarioNoAnoMes(apiSecurity.getUsuarioGuid(), ano, mes))
        .build();
  }

  @CheckSecurity.Despesas.PodeEditar
  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ContractResponse<DespesaDto> criarDespesa(
      @RequestBody @Valid DespesaInput despesaInput, HttpServletRequest request) {

    return ContractResponse.<DespesaDto>builder()
        .path(request.getServletPath())
        .response(despesaService.inserir(apiSecurity.getUsuarioGuid(), despesaInput))
        .build();
  }

  @CheckSecurity.Despesas.PodeEditar
  @Override
  @PutMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void atualizarDespesa(
      @PathVariable(value = "id") Long id,
      @RequestBody @Valid DespesaInput despesaInput,
      HttpServletRequest request) {
    despesaService.atualizar(apiSecurity.getUsuarioGuid(), id, despesaInput);
  }

  @CheckSecurity.Despesas.PodeConsultar
  @Override
  @GetMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ContractResponse<DespesaDto> detalharDespesa(
      @PathVariable(value = "id") Long id, HttpServletRequest request) {

    return ContractResponse.<DespesaDto>builder()
        .path(request.getServletPath())
        .response(despesaService.detalhar(apiSecurity.getUsuarioGuid(), id))
        .build();
  }

  @CheckSecurity.Despesas.PodeEditar
  @Override
  @DeleteMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void excluirDespesa(@PathVariable(value = "id") Long id, HttpServletRequest request) {
    despesaService.excluir(apiSecurity.getUsuarioGuid(), id);
  }
}
