package br.com.oak.financas.api.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import static br.com.oak.financas.api.util.Constants.GUID;

@Component
public class ApiSecurity {

  public Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  public String getGuid() {
    Jwt jwt = (Jwt) getAuthentication().getPrincipal();

    return jwt.getClaim(GUID);
  }

  public boolean hasAuthority(String authorityName) {
    return getAuthentication().getAuthorities().stream()
        .anyMatch(authority -> authority.getAuthority().equals(authorityName));
  }

  public boolean isAutenticado() {
    return getAuthentication().isAuthenticated();
  }

  public boolean temEscopoEscrita() {
    return hasAuthority("SCOPE_WRITE");
  }

  public boolean temEscopoLeitura() {
    return hasAuthority("SCOPE_READ");
  }

  public boolean podeConsultar() {
    return isAutenticado() && temEscopoLeitura();
  }

  public boolean podeEditar() {
    return isAutenticado() && temEscopoEscrita();
  }
}
