package com.krixon.arch.authservice.configuration;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.DefaultUserInfoRestTemplateFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateCustomizer;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import java.util.List;

@Configuration
@EnableResourceServer
@Order(-100)
class ResourceServerConfiguration extends ResourceServerConfigurerAdapter
{
    private final ResourceServerTokenServices tokenServices;

    @Autowired
    ResourceServerConfiguration(ResourceServerTokenServices tokenServices)
    {
        this.tokenServices = tokenServices;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .antMatcher("/userinfo")
            .authorizeRequests().anyRequest().authenticated();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources)
    {
        resources.tokenServices(tokenServices);
    }

    @Bean
    public UserInfoRestTemplateFactory userInfoRestTemplateFactory(
        ObjectProvider<List<UserInfoRestTemplateCustomizer>> customizers,
        ObjectProvider<OAuth2ProtectedResourceDetails> details,
        ObjectProvider<OAuth2ClientContext> oauth2ClientContext)
    {
        return new DefaultUserInfoRestTemplateFactory(customizers, details, oauth2ClientContext);
    }
}
