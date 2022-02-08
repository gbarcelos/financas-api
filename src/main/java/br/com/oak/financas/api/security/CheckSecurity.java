package br.com.oak.financas.api.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

public @interface CheckSecurity {

  public @interface Categorias {

    @PreAuthorize("hasAuthority('SCOPE_WRITE') and hasAuthority('EDITAR_CATEGORIAS')")
    @Retention(RUNTIME)
    @Target(METHOD)
    public @interface PodeEditar {}

    @PreAuthorize("@apiSecurity.podeConsultarCategorias()")
    @Retention(RUNTIME)
    @Target(METHOD)
    public @interface PodeConsultar {}
  }
}
