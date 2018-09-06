package com.krixon.ecosystem.profiling.web.field;

import lombok.*;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Relation(value = "field", collectionRelation = "fields")
class FieldResource extends ResourceSupport
{
    @NonNull String name;
    String panel;
}
