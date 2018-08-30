package com.krixon.ecosystem.profiling.dto;

import com.krixon.ecosystem.profiling.domain.Field;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.hateoas.core.EvoInflectorRelProvider;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class FieldRelProvider extends EvoInflectorRelProvider
{
    @Override
    public String getCollectionResourceRelFor(final Class<?> type)
    {
        return super.getCollectionResourceRelFor(Field.class);
    }

    @Override
    public String getItemResourceRelFor(final Class<?> type)
    {
        return super.getItemResourceRelFor(Field.class);
    }

    @Override
    public boolean supports(final Class<?> delimiter)
    {
        return Field.class.isAssignableFrom(delimiter);
    }
}
