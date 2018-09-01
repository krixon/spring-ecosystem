package com.krixon.ecosystem.profiling.domain;

import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.validation.constraints.NotNull;

@Entity
@Inheritance
public abstract class Field extends AbstractAggregateRoot
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
