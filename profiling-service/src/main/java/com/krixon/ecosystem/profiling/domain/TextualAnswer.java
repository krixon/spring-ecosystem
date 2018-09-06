package com.krixon.ecosystem.profiling.domain;

import lombok.Getter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@DiscriminatorValue("textual")
public class TextualAnswer extends Answer
{
    @NotNull
    private String value;

    private TextualAnswer()
    {
        super();
    }

    private TextualAnswer(AnswerId id, @NotNull String value)
    {
        super(id);

        this.value = value;
    }

    public static TextualAnswer submit(AnswerId id, AnswerSubmission submission)
    {
        TextualAnswer instance = new TextualAnswer(id, extractSubmittedValue(submission));

        // TODO: Fire an event.

        return instance;
    }

    @Override
    public void resubmit(AnswerSubmission submission)
    {
        value = extractSubmittedValue(submission);
    }

    private static String extractSubmittedValue(AnswerSubmission submission)
    {
        return (String) submission.getValue();
    }
}
