package br.com.oak.financas.api.model.contract.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ErrorContractResponse {

  private ErrorResponse error;

  @ApiModelProperty(
      value = "String",
      example = "Tue Jan 12 16:36:32 EST 2021",
      notes = "Request Timestamp in format EEE MMM dd HH:mm:ss z yyyy")
  private String timestamp;

  private String path;
}
