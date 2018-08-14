package com.krixon.arch.authservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.*;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.filter.ForwardedHeaderFilter;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.security.KeyPair;
import java.security.Principal;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class AuthServiceApplication implements WebMvcConfigurer
{
    private static final int SECURITY_CONFIG_DEFAULT_ORDER = 100;

    public static void main(String[] args)
    {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

    @Bean
    FilterRegistrationBean forwardedHeaderFilter()
    {
        FilterRegistrationBean<ForwardedHeaderFilter> bean = new FilterRegistrationBean<>();

        bean.setFilter(new ForwardedHeaderFilter());
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);

        return bean;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry)
    {
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/oauth/confirm_access").setViewName("authorize");
    }

    @GetMapping("/me")
    @ResponseBody
    public Principal get(Principal principal)
    {
        return principal;
    }


    @Configuration
    @Order(SECURITY_CONFIG_DEFAULT_ORDER)
    protected static class ActuatorConfiguration extends WebSecurityConfigurerAdapter
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


    @Configuration
    @EnableResourceServer
    @Order(SECURITY_CONFIG_DEFAULT_ORDER - 100)
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter
    {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                .antMatcher("/me")
                .authorizeRequests().anyRequest().authenticated();
        }
    }


    @Configuration
    @Order(SECURITY_CONFIG_DEFAULT_ORDER + 100)
    protected static class LoginConfiguration extends WebSecurityConfigurerAdapter
    {
        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception
        {
            return super.authenticationManagerBean();
        }

        @Override
        public void configure(HttpSecurity http) throws Exception
        {
            http
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/", "/webjars/**", "/error**").permitAll()
                .anyRequest().authenticated();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception
        {
            auth.inMemoryAuthentication()
                .withUser("user").password("{noop}user").roles("USER")
                .and()
                .withUser("admin").password("{noop}admin").roles("ADMIN");
        }
    }

    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter
    {
        @Autowired
        @Qualifier("authenticationManagerBean")
        private AuthenticationManager authenticationManager;

        @Bean
        public JwtAccessTokenConverter jwtAccessTokenConverter()
        {
            JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
            KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("keystore.jks"), "foobar".toCharArray()).getKeyPair("test");

            converter.setKeyPair(keyPair);

            return converter;
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
                .accessTokenConverter(jwtAccessTokenConverter());
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
}
