package com.krixon.arch.authservice.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;

@Configuration
@EnableAuthorizationServer
class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter
{
    private final AuthenticationManager authenticationManager;

    private final AccessTokenConverter tokenConverter;

    @Autowired
    public AuthorizationServerConfiguration(
        @Qualifier("authenticationManagerBean") AuthenticationManager authenticationManager,
        AccessTokenConverter tokenConverter
    ) {
        this.authenticationManager = authenticationManager;
        this.tokenConverter = tokenConverter;
    }


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception
    {
        clients.inMemory()
            .withClient("acme")
            .secret("{noop}acmesecret")
            .authorizedGrantTypes("authorization_code", "refresh_token", "password", "client_credentials")
            .scopes("openid");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)
    {
        endpoints
            .authenticationManager(authenticationManager)
            .accessTokenConverter(tokenConverter);
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
