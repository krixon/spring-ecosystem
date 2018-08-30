package com.krixon.ecosystem.profiling.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonTypeName("number")
public class NumberField extends Field
{
    Double min;
    Double max;

    public NumberField(String id, String name)
    {
        super(id, name);
    }

    @JsonCreator
    public NumberField(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("min") Double min,
        @JsonProperty("max") Double max)
    {
        super(id, name);

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

