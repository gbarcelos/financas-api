package br.com.oak.financas.api.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class LancamentoDto {

  @ApiModelProperty(example = "452163", position = 1)
  private Long id;

  @ApiModelProperty(example = "Salário", position = 2)
  private String descricao;

  @ApiModelProperty(example = "3537.23", position = 3)
  private BigDecimal valor;

  @ApiModelProperty(example = "01/01/2022", position = 4)
  private LocalDate data;
}
