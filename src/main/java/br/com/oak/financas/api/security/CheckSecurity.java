package br.com.oak.financas.api.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

public @interface CheckSecurity {

  public @interface Categorias {

    @PreAuthorize("@apiSecurity.podeConsultar()")
    @Retention(RUNTIME)
    @Target(METHOD)
    public @interface PodeConsultar {}

    @PreAuthorize("hasAuthority('SCOPE_WRITE') and hasAuthority('EDITAR_CATEGORIAS')")
    @Retention(RUNTIME)
    @Target(METHOD)
    public @interface PodeEditar {}
  }

  public @interface Receitas {

    @PreAuthorize("@apiSecurity.podeConsultar()")
    @Retention(RUNTIME)
    @Target(METHOD)
    public @interface PodeConsultar {}

    @PreAuthorize("@apiSecurity.podeEditar()")
    @Retention(RUNTIME)
    @Target(METHOD)
    public @interface PodeEditar {}
  }

  public @interface Despesas {

    @PreAuthorize("@apiSecurity.podeConsultar()")
    @Retention(RUNTIME)
    @Target(METHOD)
    public @interface PodeConsultar {}

    @PreAuthorize("@apiSecurity.podeEditar()")
    @Retention(RUNTIME)
    @Target(METHOD)
    public @interface PodeEditar {}
  }

  public @interface Resumo {

    @PreAuthorize("@apiSecurity.podeConsultar()")
    @Retention(RUNTIME)
    @Target(METHOD)
    public @interface PodeConsultar {}
  }
}
