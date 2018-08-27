package com.krixon.ecosystem.authservice.configuration;

import com.krixon.ecosystem.authservice.domain.StandardTokenEnhancer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
class TokenConfiguration
{
    @Bean
    public JwtAccessTokenConverter accessTokenConverter()
    {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

        converter.setSigningKey("123");

        return converter;
    }

    @Bean
    public TokenStore tokenStore()
    {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices()
    {
        DefaultTokenServices services = new DefaultTokenServices();

        services.setTokenStore(tokenStore());
        services.setSupportRefreshToken(true);

        return services;
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new StandardTokenEnhancer();
    }
}
