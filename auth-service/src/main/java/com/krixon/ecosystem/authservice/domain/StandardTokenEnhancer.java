package com.krixon.ecosystem.authservice.domain;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

public class StandardTokenEnhancer implements TokenEnhancer
{
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication)
    {
        Map<String, Object> additionalInfo = new HashMap<>();

        Object principal = authentication.getPrincipal();

        if (principal instanceof User) {
            additionalInfo.put("username", ((User) principal).getUsername());
            additionalInfo.put("roles", ((User) principal).getAuthorities().stream().map(Object::toString));
        }

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

        return accessToken;
    }
}
