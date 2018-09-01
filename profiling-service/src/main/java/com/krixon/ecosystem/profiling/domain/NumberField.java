package com.krixon.ecosystem.profiling.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import javax.persistence.Entity;

@Entity
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class NumberField extends Field
{
    private Double min;
    private Double max;

    NumberField() {}

    NumberField(String id, String panelId, String name, Double min, Double max)
    {
        super(id, panelId, name);

        this.min = min;
        this.max = max;
    }

    public static NumberField define(String id, String panelId, String name, Double min, Double max)
    {
        NumberField instance = new NumberField(id, panelId, name, min, max);

        instance.registerEvent(new Defined(id, panelId, name, min, max));

        return instance;
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

