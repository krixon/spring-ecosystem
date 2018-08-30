package com.krixon.ecosystem.profiling.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonTypeName("datetime")
public class DateTimeField extends Field
{
    @JsonCreator
    public DateTimeField(@JsonProperty("id") String id, @JsonProperty("name") String name)
    {
        super(id, name);
    }
}
