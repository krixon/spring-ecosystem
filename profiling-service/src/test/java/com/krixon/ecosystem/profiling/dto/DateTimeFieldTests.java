package com.krixon.ecosystem.profiling.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;


public class DateTimeFieldTests
{
    @Test
    public void whenSerializingPolymorphic_thenCorrect()
    throws JsonProcessingException
    {
        DateTimeField field = new DateTimeField("fieldId", "fieldName");

        String result = new ObjectMapper().writeValueAsString(field);

        assertThat(result, containsString("type"));
        assertThat(result, containsString("datetime"));
        assertThat(result, containsString("fieldId"));
        assertThat(result, containsString("fieldName"));
    }

    @Test
    public void whenDeserializingPolymorphic_thenCorrect()
    throws IOException
    {
        String json = "{\"id\":\"fieldId\",\"name\":\"fieldName\",\"type\":\"datetime\"}";

        DateTimeField field = new ObjectMapper()
            .readerFor(Field.class)
            .readValue(json);

        Assert.assertEquals("fieldName", field.getName());
        Assert.assertEquals("fieldId", field.getId());
        Assert.assertEquals(DateTimeField.class, field.getClass());
    }
}
