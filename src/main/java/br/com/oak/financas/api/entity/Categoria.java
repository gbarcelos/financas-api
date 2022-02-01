package br.com.oak.financas.api.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@Entity
@Table(name = "categoria")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Categoria {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String descricao;
}
