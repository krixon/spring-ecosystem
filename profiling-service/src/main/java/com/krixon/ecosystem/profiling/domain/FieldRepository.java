package com.krixon.ecosystem.profiling.domain;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "fields", path = "fields")
public interface FieldRepository extends PagingAndSortingRepository<Field, String>
{
    List<Field> findByName(@Param("name") String name);
}
