package br.com.oak.financas.api.security.authorizationserver;

import br.com.oak.financas.api.entity.Usuario;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class AuthUser extends User {

  private String guid;
  private String fullName;

  public AuthUser(Usuario usuario, Collection<? extends GrantedAuthority> authorities) {
    super(usuario.getEmail(), usuario.getSenha(), authorities);

    guid = usuario.getGuid();
    fullName = usuario.getNome();
  }
}
