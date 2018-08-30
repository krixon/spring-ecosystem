package com.krixon.ecosystem.panels.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krixon.ecosystem.profiling.dto.Field;
import com.krixon.ecosystem.profiling.dto.NumberField;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;


public class NumberFieldTests
{
    @Test
    public void whenSerializingPolymorphic_thenCorrect()
    throws JsonProcessingException
    {
        NumberField field = new NumberField("fieldId", "fieldName");

        String result = new ObjectMapper().writeValueAsString(field);

        assertThat(result, containsString("type"));
        assertThat(result, containsString("number"));
        assertThat(result, containsString("fieldId"));
        assertThat(result, containsString("fieldName"));
    }

    @Test
    public void whenDeserializingPolymorphic_thenCorrect()
    throws IOException
    {
        String json = "{\"id\":\"fieldId\",\"name\":\"fieldName\",\"type\":\"number\"}";

        NumberField field = new ObjectMapper()
            .readerFor(Field.class)
            .readValue(json);

        Assert.assertEquals("fieldName", field.getName());
        Assert.assertEquals("fieldId", field.getId());
        Assert.assertEquals(NumberField.class, field.getClass());
    }
}
