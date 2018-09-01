package com.krixon.ecosystem.profiling.write.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonTypeName("number")
public class DefineNumberField extends DefineField
{
    private Double min, max;

    @JsonCreator
    public DefineNumberField(
        @JsonProperty("id") String id,
        @JsonProperty("panelId") String panelId,
        @JsonProperty("name") String name,
        @JsonProperty("min") Double min,
        @JsonProperty("max") Double max
    ) {
        super(id, panelId, name);

        this.min = min;
        this.max = max;
    }
}
