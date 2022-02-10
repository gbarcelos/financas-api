package br.com.oak.financas.api.security.authorizationserver;

import br.com.oak.financas.api.entity.Usuario;
import br.com.oak.financas.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JpaUserDetailsService implements UserDetailsService {

  private final UsuarioRepository usuarioRepository;

  @Transactional(readOnly = true)
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Usuario usuario =
        usuarioRepository
            .findByEmail(username)
            .orElseThrow(
                () -> new UsernameNotFoundException("Usuário não encontrado com e-mail informado"));

    return new AuthUser(usuario, getAuthorities(usuario));
  }

  private Collection<GrantedAuthority> getAuthorities(Usuario usuario) {
    return usuario.getGrupos().stream()
        .flatMap(grupo -> grupo.getPermissoes().stream())
        .map(permissao -> new SimpleGrantedAuthority(permissao.getNome().toUpperCase()))
        .collect(Collectors.toSet());
  }
}
