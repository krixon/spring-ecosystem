package com.krixon.ecosystem.profiling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableDiscoveryClient
@EnableResourceServer
public class ProfilingServiceApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(ProfilingServiceApplication.class, args);
    }
}
