package com.krixon.ecosystem.profiling.domain;

import lombok.Getter;
import lombok.Value;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Getter
class Field extends AbstractAggregateRoot
{
    @Id
    private String id;

    @NotNull
    private String panelId;

    @NotNull
    private String name;

    private Field() {}

    Field(String id, String panelId, String name)
    {
        this.id = id;
        this.panelId = panelId;
        this.name = name;
    }

    void markDefined()
    {
        registerEvent(new Defined(id, panelId, name));
    }

    @Value
    private static class Defined
    {
        private String id, panelId, name;
    }
}
