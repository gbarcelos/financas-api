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
@ApiModel(description = "Encapsula as informações para criação de uma receita")
public class ReceitaInput extends LancamentoInput {

  @ApiModelProperty(example = "Salário", required = true, position = 1)
  @NotBlank
  private String descricao;
}
