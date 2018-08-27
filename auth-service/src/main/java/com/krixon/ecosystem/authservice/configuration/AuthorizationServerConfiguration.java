package com.krixon.ecosystem.authservice.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter
{
    private final AuthenticationManager authenticationManager;

    private final JwtAccessTokenConverter tokenConverter;

    private final TokenEnhancer tokenEnhancer;

    private final TokenStore tokenStore;

    @Autowired
    public AuthorizationServerConfiguration(
        @Qualifier("authenticationManagerBean") AuthenticationManager authenticationManager,
        @Qualifier("accessTokenConverter") JwtAccessTokenConverter tokenConverter,
        TokenEnhancer tokenEnhancer,
        TokenStore tokenStore
    ) {
        this.authenticationManager = authenticationManager;
        this.tokenConverter = tokenConverter;
        this.tokenEnhancer = tokenEnhancer;
        this.tokenStore = tokenStore;
    }


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception
    {
        clients.inMemory()
            .withClient("acme")
            .secret("{noop}acmesecret")
            .authorizedGrantTypes("authorization_code", "refresh_token", "password", "client_credentials")
            .redirectUris("http://localhost:8764/")
            .scopes("openid", "panel:read", "panel:write");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)
    {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();

        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer, tokenConverter));

        endpoints
            .tokenStore(tokenStore)
            .authenticationManager(authenticationManager)
            .tokenEnhancer(tokenEnhancerChain);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer)
    {
        oauthServer
            .tokenKeyAccess("permitAll()")
            .checkTokenAccess("isAuthenticated()")
            .allowFormAuthenticationForClients();
    }
}
