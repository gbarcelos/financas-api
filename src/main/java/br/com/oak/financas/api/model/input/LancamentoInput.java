package br.com.oak.financas.api.model.input;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LancamentoInput {

  @ApiModelProperty(example = "3537.23", required = true, position = 2)
  @NotNull
  private BigDecimal valor;

  @ApiModelProperty(example = "01/01/2022", required = true, position = 3)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
  @NotNull
  private LocalDate data;
}
