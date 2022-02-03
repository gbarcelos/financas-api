package br.com.oak.financas.api.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor()
@Builder
@ApiModel(description = "Encapsula as informações sobre o valor total de despesas por categoria")
public class DespesasPorCategoriaDto {

  @ApiModelProperty(example = "Outros", position = 1)
  private String categoria;

  @ApiModelProperty(example = "550.74", position = 2)
  private BigDecimal total;
}
