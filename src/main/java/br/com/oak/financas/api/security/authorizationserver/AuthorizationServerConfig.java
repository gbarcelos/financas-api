package br.com.oak.financas.api.security.authorizationserver;

import br.com.oak.financas.api.config.FinancasApiProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

  @Autowired private AuthenticationManager authenticationManager;

  @Autowired private UserDetailsService userDetailsService;

  @Autowired private DataSource dataSource;

  @Autowired private FinancasApiProperties financasApiProperties;

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.jdbc(dataSource);
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    security.checkTokenAccess("permitAll()");
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

    TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
    enhancerChain.setTokenEnhancers(
        Arrays.asList(new JwtCustomClaimsTokenEnhancer(), jwtAccessTokenConverter()));

    endpoints
        .authenticationManager(authenticationManager)
        .userDetailsService(userDetailsService)
        .reuseRefreshTokens(false)
        .accessTokenConverter(jwtAccessTokenConverter())
        .tokenEnhancer(enhancerChain)
        .approvalStore(approvalStore(endpoints.getTokenStore()))
        .tokenGranter(tokenGranter(endpoints));
  }

  @Bean
  public JwtAccessTokenConverter jwtAccessTokenConverter() {

    JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
    jwtAccessTokenConverter.setSigningKey(financasApiProperties.getJwtKey());

    return jwtAccessTokenConverter;
  }

  private ApprovalStore approvalStore(TokenStore tokenStore) {
    TokenApprovalStore approvalStore = new TokenApprovalStore();
    approvalStore.setTokenStore(tokenStore);

    return approvalStore;
  }

  private TokenGranter tokenGranter(AuthorizationServerEndpointsConfigurer endpoints) {

    PkceAuthorizationCodeTokenGranter pkceAuthorizationCodeTokenGranter =
        new PkceAuthorizationCodeTokenGranter(
            endpoints.getTokenServices(),
            endpoints.getAuthorizationCodeServices(),
            endpoints.getClientDetailsService(),
            endpoints.getOAuth2RequestFactory());

    List<TokenGranter> granters =
        Arrays.asList(pkceAuthorizationCodeTokenGranter, endpoints.getTokenGranter());

    return new CompositeTokenGranter(granters);
  }
}
