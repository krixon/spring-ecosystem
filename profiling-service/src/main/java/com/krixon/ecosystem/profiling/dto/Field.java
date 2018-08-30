package com.krixon.ecosystem.profiling.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = DateTimeField.class, name = "datetime"),
    @JsonSubTypes.Type(value = NumberField.class, name = "number"),
})
abstract public class Field
{
    private final String id;
    private final String name;

    public Field(String id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }
}

