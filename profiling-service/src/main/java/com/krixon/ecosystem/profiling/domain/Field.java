package com.krixon.ecosystem.profiling.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.validation.constraints.NotNull;

@Entity
@Inheritance
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = DateTimeField.class, name = "datetime"),
    @JsonSubTypes.Type(value = NumberField.class, name = "number"),
})
@RestResource(path = "fields", rel = "fields")
public abstract class Field
{
    @Id
    private String id;

    @NotNull
    private String panelId;

    @NotNull
    private String name;

    protected Field() {}

    public Field(String id, String panelId, String name)
    {
        this.id = id;
        this.panelId = panelId;
        this.name = name;
    }

    public String getId()
    {
        return id;
    }

    public String getPanelId()
    {
        return panelId;
    }

    public String getName()
    {
        return name;
    }
}
