package com.krixon.ecosystem.profiling.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.hateoas.Identifiable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Getter
public class Field extends AbstractAggregateRoot implements Identifiable<String>
{
    @Id
    private String id;

    @NotNull
    private String name;

    @NotNull
    private AnswerType answerType;

    private String panelId;


    private Field() {}

    private Field(@NonNull String id, @NonNull String name, @NonNull AnswerType answerType, String panelId)
    {
        this.id = id;
        this.name = name;
        this.answerType = answerType;
        this.panelId = panelId;
    }

    public static Field define(String id, String name)
    {
        return define(id, name, AnswerType.TEXTUAL);
    }

    public static Field define(String id, String name, String panelId)
    {
        return define(id, name, AnswerType.TEXTUAL, panelId);
    }

    public static Field define(String id, String name, AnswerType answerType)
    {
        return define(id, name, answerType, null);
    }

    public static Field define(String id, String name, AnswerType answerType, String panelId)
    {
        Field instance = new Field(id, name, answerType, panelId);

        instance.registerEvent(new Defined(id, name, panelId));

        return instance;
    }

    public void promoteToGlobal()
    {
        this.panelId = null;

        // TODO: Event.
    }

    public void transferToPanel(@NonNull String panelId)
    {
        this.panelId = panelId;

        // TODO: Event.
    }

    public void rename(@NonNull String name)
    {
        this.name = name;

        // TODO: Event.
    }

    public enum AnswerType {
        NUMERIC, TEXTUAL,
    }

    @Value
    private static class Defined
    {
        private @NonNull String id, name;
        private String panelId;

    }
}
