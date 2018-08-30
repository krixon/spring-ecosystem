package com.krixon.ecosystem.profiling.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Type;

@Configuration
public class RestConfiguration extends RepositoryRestConfigurerAdapter
{
    private final EntityManager entityManager;

    @Autowired
    public RestConfiguration(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        // Ensure ID properties are always exposed as part of the resource properties.
        // By default they are excluded on the basis that the hypermedia links present in the resources mean
        // that an ID isn't ever needed as a property. If that proves to be true then this class can be removed.
        config.exposeIdsFor(
            entityManager
                .getMetamodel()
                .getEntities()
                .stream()
                .map(Type::getJavaType).toArray(Class[]::new)
        );
    }
}
