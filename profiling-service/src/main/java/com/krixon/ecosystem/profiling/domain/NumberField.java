package com.krixon.ecosystem.profiling.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import javax.persistence.Entity;

@Entity
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonTypeName("number")
public class NumberField extends Field
{
    private Double min;
    private Double max;

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

        // Note that it's OK to register this event here even though this constructor is used for all deserialization.
        // This is because when using PUT or PATCH to update, values are mapped from the existing deserialized
        // instance (containing this registered event) to a new instance created without calling this constructor.
        // The end result is that the instance which is ultimately saved does not contain the event and so nothing
        // is fired. Even so, it might be that this is all too magic and plain Spring Data should be used with some
        // more manual boilerplate to give better control...
        registerEvent(new Defined(id, panelId, name, min, max));
    }

    public Double getMin()
    {
        return min;
    }

    public Double getMax()
    {
        return max;
    }

    @Value
    public static class Defined
    {
        private String id, panelId, name;
        private Double min, max;
    }
}

