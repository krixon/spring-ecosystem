package com.krixon.ecosystem.profiling.domain;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;

@Entity
@Inheritance
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Getter
@ToString
public abstract class Answer extends AbstractAggregateRoot implements Identifiable<AnswerId>
{
    @EmbeddedId
    private AnswerId id;

    protected Answer() {}

    protected Answer(AnswerId id)
    {
        this.id = id;
    }

    abstract public void resubmit(AnswerSubmission submission);
    abstract public Object getValue();

    public String getMemberId()
    {
        return id.getMemberId();
    }

    public String getFieldId()
    {
        return id.getFieldId();
    }
}
