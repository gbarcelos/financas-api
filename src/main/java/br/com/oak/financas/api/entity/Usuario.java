package br.com.oak.financas.api.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario {

  @EqualsAndHashCode.Include
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String guid;

  private String nome;

  private String email;

  private String senha;

  @ManyToMany
  @JoinTable(
      name = "usuario_grupo",
      joinColumns = @JoinColumn(name = "usuario_id"),
      inverseJoinColumns = @JoinColumn(name = "grupo_id"))
  private Set<Grupo> grupos = new HashSet<>();
}
