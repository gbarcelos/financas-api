package br.com.oak.financas.api.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(description = "Encapsula as informações de uma despesa")
public class DespesaDto extends LancamentoDto {

  @ApiModelProperty(example = "Outros", position = 5)
  private String categoria;
}
