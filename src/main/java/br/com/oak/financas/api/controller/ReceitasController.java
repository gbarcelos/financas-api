package br.com.oak.financas.api.controller;

import br.com.oak.financas.api.controller.openapi.ReceitasControllerOpenApi;
import br.com.oak.financas.api.model.contract.response.ContractResponse;
import br.com.oak.financas.api.model.dto.ReceitaDto;
import br.com.oak.financas.api.model.input.ReceitaInput;
import br.com.oak.financas.api.service.ReceitaService;
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
@RequestMapping(path = "/v1/receitas", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReceitasController implements ReceitasControllerOpenApi {

  private final ReceitaService receitaService;

  @Override
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public ContractResponse<List<ReceitaDto>> listarReceitas(HttpServletRequest request) {

    return ContractResponse.<List<ReceitaDto>>builder()
        .path(request.getServletPath())
        .response(receitaService.listar())
        .build();
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ContractResponse<ReceitaDto> criarReceita(
      @RequestBody @Valid ReceitaInput receitaInput, HttpServletRequest request) {

    return ContractResponse.<ReceitaDto>builder()
        .path(request.getServletPath())
        .response(receitaService.inserir(receitaInput))
        .build();
  }

  @Override
  @PutMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void atualizarReceita(
      @PathVariable(value = "id") Long id,
      @RequestBody @Valid ReceitaInput receitaInput,
      HttpServletRequest request) {
    receitaService.atualizar(id, receitaInput);
  }

  @Override
  @GetMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ContractResponse<ReceitaDto> detalharReceita(
      @PathVariable(value = "id") Long id, HttpServletRequest request) {

    return ContractResponse.<ReceitaDto>builder()
        .path(request.getServletPath())
        .response(receitaService.detalhar(id))
        .build();
  }

  @Override
  @DeleteMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void excluirReceita(@PathVariable(value = "id") Long id, HttpServletRequest request) {
    receitaService.excluir(id);
  }
}
