package com.krixon.ecosystem.profiling.web.field;

import com.krixon.ecosystem.profiling.domain.Field;
import lombok.NonNull;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public class FieldResourceAssembler implements ResourceAssembler<Field, FieldResource>
{
    private final EntityLinks entityLinks;

    public FieldResourceAssembler(EntityLinks entityLinks)
    {
        this.entityLinks = entityLinks;
    }

    @Override
    public FieldResource toResource(@NonNull Field entity)
    {
        FieldResource resource = new FieldResource(entity.getName(), entity.getPanelId());

        resource.add(entityLinks.linkToSingleResource(Field.class, entity.getId()).withSelfRel());

        return resource;
    }
}
