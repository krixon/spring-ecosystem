package com.krixon.ecosystem.profiling.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;

@Entity
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonTypeName("datetime")
public class DateTimeField extends Field
{
    DateTimeField() {}

    @JsonCreator
    public DateTimeField(
        @JsonProperty("id") String id,
        @JsonProperty("panelId") String panelId,
        @JsonProperty("name") String name
    ) {
        super(id, panelId, name);
    }
}
