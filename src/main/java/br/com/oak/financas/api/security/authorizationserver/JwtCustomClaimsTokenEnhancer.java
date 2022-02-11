package br.com.oak.financas.api.security.authorizationserver;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;

import static br.com.oak.financas.api.util.Constants.GUID;

public class JwtCustomClaimsTokenEnhancer implements TokenEnhancer {

  @Override
  public OAuth2AccessToken enhance(
      OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

    if (authentication.getPrincipal() instanceof AuthUser) {

      AuthUser authUser = (AuthUser) authentication.getPrincipal();

      HashMap<String, Object> info = new HashMap<>();
      info.put("nome_completo", authUser.getFullName());
      info.put(GUID, authUser.getGuid());

      DefaultOAuth2AccessToken oAuth2AccessToken = (DefaultOAuth2AccessToken) accessToken;
      oAuth2AccessToken.setAdditionalInformation(info);
    }
    return accessToken;
  }
}
