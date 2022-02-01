package br.com.oak.financas.api.model.contract.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@ApiModel("ObjectError")
public class ObjectError {

  @ApiModelProperty(example = "descricao", position = 1)
  private String name;

  @ApiModelProperty(example = "A descricao é obrigatória", position = 2)
  private String userMessage;
}
