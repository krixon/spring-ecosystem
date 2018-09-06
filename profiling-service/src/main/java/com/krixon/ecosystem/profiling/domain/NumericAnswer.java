package com.krixon.ecosystem.profiling.domain;

import lombok.Getter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@DiscriminatorValue("numeric")
public class NumericAnswer extends Answer
{
    @NotNull
    private Double value;

    private NumericAnswer()
    {
        super();
    }

    private NumericAnswer(AnswerId id, @NotNull Double value)
    {
        super(id);

        this.value = value;
    }

    public static NumericAnswer submit(AnswerId id, AnswerSubmission submission)
    {
        NumericAnswer instance = new NumericAnswer(id, extractSubmittedValue(submission));

        // TODO: Fire an event.

        return instance;
    }

    public void resubmit(AnswerSubmission submission)
    {
        value = extractSubmittedValue(submission);

        // TODO: Check if value actually changed and fire an event if so.
    }

    private static Double extractSubmittedValue(AnswerSubmission submission) throws IllegalArgumentException
    {
        Object value = submission.getValue();

        if (value instanceof Double) {
            return (Double) value;
        }

        if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
        }


        if (value instanceof String) {
            try {
                return Double.parseDouble((String) value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Cannot cast submitted String value to Double.", e);
            }
        }

        throw new IllegalArgumentException("Cannot cast submitted value to Double.");
    }
}
