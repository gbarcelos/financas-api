package br.com.oak.financas.api.controller;

import br.com.oak.financas.api.controller.openapi.ReceitasControllerOpenApi;
import br.com.oak.financas.api.model.contract.response.ContractResponse;
import br.com.oak.financas.api.model.dto.ReceitaDto;
import br.com.oak.financas.api.model.input.ReceitaInput;
import br.com.oak.financas.api.security.ApiSecurity;
import br.com.oak.financas.api.security.CheckSecurity;
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
  private final ApiSecurity apiSecurity;

  @CheckSecurity.Receitas.PodeConsultar
  @Override
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public ContractResponse<List<ReceitaDto>> listarReceitas(
      @RequestParam(required = false) String descricao, HttpServletRequest request) {

    return ContractResponse.<List<ReceitaDto>>builder()
        .path(request.getServletPath())
        .response(receitaService.listarReceitasDoUsuario(apiSecurity.getUsuarioGuid(), descricao))
        .build();
  }

  @CheckSecurity.Receitas.PodeConsultar
  @Override
  @GetMapping("/{ano}/{mes}")
  @ResponseStatus(HttpStatus.OK)
  public ContractResponse<List<ReceitaDto>> buscarReceitasNoAnoMes(
      @PathVariable Integer ano, @PathVariable Integer mes, HttpServletRequest request) {

    return ContractResponse.<List<ReceitaDto>>builder()
        .path(request.getServletPath())
        .response(
            receitaService.buscarReceitasDoUsuarioNoAnoMes(apiSecurity.getUsuarioGuid(), ano, mes))
        .build();
  }

  @CheckSecurity.Receitas.PodeEditar
  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ContractResponse<ReceitaDto> criarReceita(
      @RequestBody @Valid ReceitaInput receitaInput, HttpServletRequest request) {

    return ContractResponse.<ReceitaDto>builder()
        .path(request.getServletPath())
        .response(receitaService.inserir(apiSecurity.getUsuarioGuid(), receitaInput))
        .build();
  }

  @CheckSecurity.Receitas.PodeEditar
  @Override
  @PutMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void atualizarReceita(
      @PathVariable(value = "id") Long id,
      @RequestBody @Valid ReceitaInput receitaInput,
      HttpServletRequest request) {
    receitaService.atualizar(apiSecurity.getUsuarioGuid(), id, receitaInput);
  }

  @CheckSecurity.Receitas.PodeConsultar
  @Override
  @GetMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ContractResponse<ReceitaDto> detalharReceita(
      @PathVariable(value = "id") Long id, HttpServletRequest request) {

    return ContractResponse.<ReceitaDto>builder()
        .path(request.getServletPath())
        .response(receitaService.detalhar(apiSecurity.getUsuarioGuid(), id))
        .build();
  }

  @CheckSecurity.Receitas.PodeEditar
  @Override
  @DeleteMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void excluirReceita(@PathVariable(value = "id") Long id, HttpServletRequest request) {
    receitaService.excluir(apiSecurity.getUsuarioGuid(), id);
  }
}
