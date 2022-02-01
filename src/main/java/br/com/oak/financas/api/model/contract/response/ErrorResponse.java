package br.com.oak.financas.api.model.contract.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Getter
@Builder
@ApiModel("ErrorResponse")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {
  @ApiModelProperty(example = "400", position = 1)
  private Integer status;

  @ApiModelProperty(example = "BAD_REQUEST", position = 2)
  private String type;

  @ApiModelProperty(example = "INVALID_PARAMETER", position = 3)
  private String errorCode;

  @ApiModelProperty(example = "Dados inválidos", position = 4)
  private String detail;

  @ApiModelProperty(position = 5)
  private List<ObjectError> objectErrors;
}
