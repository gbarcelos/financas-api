package br.com.oak.financas.api.entity;

import br.com.oak.financas.api.model.TipoLancamento;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@Entity
@Table(name = "lancamento")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Lancamento {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String descricao;

  private BigDecimal valor;

  private LocalDate data;

  @Enumerated(EnumType.STRING)
  private TipoLancamento tipo;
}
