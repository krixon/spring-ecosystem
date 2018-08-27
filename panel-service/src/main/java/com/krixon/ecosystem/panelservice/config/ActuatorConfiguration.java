package com.krixon.ecosystem.panelservice.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(-50)
class ActuatorConfiguration extends WebSecurityConfigurerAdapter
{
    @Override
    public void configure(HttpSecurity http) throws Exception
    {
        http
            .requestMatcher(EndpointRequest.toAnyEndpoint())
            .authorizeRequests()
            .anyRequest().permitAll()
            .and()
            .csrf().disable();
    }
}
