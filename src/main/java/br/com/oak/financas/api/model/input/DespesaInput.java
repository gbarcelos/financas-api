package br.com.oak.financas.api.model.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ApiModel(description = "Encapsula as informações para criação de uma despesa")
public class DespesaInput extends LancamentoInput {

  @ApiModelProperty(example = "Combustível", required = true, position = 1)
  @NotBlank
  private String descricao;

  @ApiModelProperty(example = "1", position = 4)
  private Long categoriaId;
}
