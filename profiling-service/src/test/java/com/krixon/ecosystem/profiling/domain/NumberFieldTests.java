package com.krixon.ecosystem.profiling.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        NumberField field = new NumberField("fieldId", "panelId", "fieldName", null, null);

        String result = new ObjectMapper().writeValueAsString(field);

        assertThat(result, containsString("type"));
        assertThat(result, containsString("number"));
        assertThat(result, containsString("fieldId"));
        assertThat(result, containsString("panelId"));
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
