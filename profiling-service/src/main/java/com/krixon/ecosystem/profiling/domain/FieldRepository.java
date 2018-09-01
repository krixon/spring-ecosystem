package com.krixon.ecosystem.profiling.domain;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FieldRepository extends PagingAndSortingRepository<Field, String>
{
    List<Field> findByName(@Param("name") String name);
}
