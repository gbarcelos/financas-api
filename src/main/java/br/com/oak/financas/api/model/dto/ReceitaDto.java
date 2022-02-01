package br.com.oak.financas.api.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(description = "Encapsula as informações de uma receita")
public class ReceitaDto extends LancamentoDto {}
