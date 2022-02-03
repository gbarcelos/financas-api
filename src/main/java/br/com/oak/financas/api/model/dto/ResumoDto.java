package br.com.oak.financas.api.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ApiModel(description = "Encapsula as informações de um resumo de despesas e receitas do mês")
public class ResumoDto {

  @ApiModelProperty(example = "3537.23", position = 1)
  private BigDecimal totalDasReceitas;

  @ApiModelProperty(example = "1574.74", position = 2)
  private BigDecimal totalDasDespesas;

  @ApiModelProperty(example = "1962.49", position = 3)
  private BigDecimal saldoFinal;

  private List<DespesasPorCategoriaDto> despesasPorCategoria;
}
