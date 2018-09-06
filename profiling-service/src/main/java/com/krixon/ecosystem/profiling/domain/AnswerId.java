package com.krixon.ecosystem.profiling.domain;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Value
@AllArgsConstructor
public class AnswerId implements Serializable
{
    private @NonFinal String memberId;

    private @NonFinal String fieldId;

    AnswerId()
    {
    }
}
