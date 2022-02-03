package br.com.oak.financas.api.controller;

import br.com.oak.financas.api.controller.openapi.DespesasControllerOpenApi;
import br.com.oak.financas.api.model.contract.response.ContractResponse;
import br.com.oak.financas.api.model.dto.DespesaDto;
import br.com.oak.financas.api.model.input.DespesaInput;
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

  @Override
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public ContractResponse<List<DespesaDto>> listarDespesas(
      @RequestParam(required = false) String descricao, HttpServletRequest request) {

    return ContractResponse.<List<DespesaDto>>builder()
        .path(request.getServletPath())
        .response(despesaService.listar(descricao))
        .build();
  }

  @Override
  @GetMapping("/{ano}/{mes}")
  @ResponseStatus(HttpStatus.OK)
  public ContractResponse<List<DespesaDto>> listarDespesasPorMes(
      @PathVariable Integer ano, @PathVariable Integer mes, HttpServletRequest request) {

    return ContractResponse.<List<DespesaDto>>builder()
        .path(request.getServletPath())
        .response(despesaService.listarDespesasPorMes(ano, mes))
        .build();
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ContractResponse<DespesaDto> criarDespesa(
      @RequestBody @Valid DespesaInput despesaInput, HttpServletRequest request) {

    return ContractResponse.<DespesaDto>builder()
        .path(request.getServletPath())
        .response(despesaService.inserir(despesaInput))
        .build();
  }

  @Override
  @PutMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void atualizarDespesa(
      @PathVariable(value = "id") Long id,
      @RequestBody @Valid DespesaInput despesaInput,
      HttpServletRequest request) {
    despesaService.atualizar(id, despesaInput);
  }

  @Override
  @GetMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ContractResponse<DespesaDto> detalharDespesas(
      @PathVariable(value = "id") Long id, HttpServletRequest request) {

    return ContractResponse.<DespesaDto>builder()
        .path(request.getServletPath())
        .response(despesaService.detalhar(id))
        .build();
  }

  @Override
  @DeleteMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void excluirReceita(@PathVariable(value = "id") Long id, HttpServletRequest request) {
    despesaService.excluir(id);
  }
}
