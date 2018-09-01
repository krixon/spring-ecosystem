package com.krixon.ecosystem.profiling.domain;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.stereotype.Component;

@RepositoryRestResource(path = "fields", collectionResourceRel = "fields", itemResourceRel = "field")
public interface FieldRepository extends PagingAndSortingRepository<Field, String>
{
    @Component
    @RepositoryEventHandler
    class FieldEventHandler
    {
        @HandleBeforeCreate
        public void handleBeforeCreate(Field field)
        {
            field.markDefined();
        }
    }
}
