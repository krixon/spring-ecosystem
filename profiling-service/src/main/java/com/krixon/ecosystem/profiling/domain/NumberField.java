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
@JsonTypeName("number")
public class NumberField extends Field
{
    Double min;
    Double max;

    NumberField() {}

    public NumberField(String id, String panelId, String name)
    {
        this(id, panelId, name, null, null);
    }

    @JsonCreator
    public NumberField(
        @JsonProperty("id") String id,
        @JsonProperty("panelId") String panelId,
        @JsonProperty("name") String name,
        @JsonProperty("min") Double min,
        @JsonProperty("max") Double max)
    {
        super(id, panelId, name);

        this.min = min;
        this.max = max;
    }

    public Double getMin()
    {
        return min;
    }

    public Double getMax()
    {
        return max;
    }
}

