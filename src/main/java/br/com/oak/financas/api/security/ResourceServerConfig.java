package br.com.oak.financas.api.security;

import br.com.oak.financas.api.config.FinancasApiProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import javax.crypto.spec.SecretKeySpec;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {

  @Autowired private FinancasApiProperties financasApiProperties;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.formLogin()
        .and()
        .authorizeRequests()
        .antMatchers("/oauth/**")
        .authenticated()
        .and()
        .csrf()
        .disable()
        .cors()
        .and()
        .oauth2ResourceServer()
        .jwt()
        .jwtAuthenticationConverter(jwtAuthenticationConverter());
  }

  private JwtAuthenticationConverter jwtAuthenticationConverter() {

    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
        jwt -> {
          List<String> authorities = jwt.getClaimAsStringList("authorities");

          if (authorities == null) {
            authorities = Collections.emptyList();
          }

          JwtGrantedAuthoritiesConverter scopesAuthoritiesConverter =
              new JwtGrantedAuthoritiesConverter();

          Collection<GrantedAuthority> grantedAuthorities = scopesAuthoritiesConverter.convert(jwt);

          grantedAuthorities.addAll(
              authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

          return grantedAuthorities;
        });

    return jwtAuthenticationConverter;
  }

  @Bean
  public JwtDecoder jwtDecoder() {
    SecretKeySpec secretKeySpec =
        new SecretKeySpec(financasApiProperties.getJwtKey().getBytes(), "HmacSHA256");
    return NimbusJwtDecoder.withSecretKey(secretKeySpec).build();
  }

  /** Utilizado pelo authorization server */
  @Bean
  @Override
  protected AuthenticationManager authenticationManager() throws Exception {
    return super.authenticationManager();
  }
}
