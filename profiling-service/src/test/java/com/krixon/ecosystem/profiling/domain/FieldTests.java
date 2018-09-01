package com.krixon.ecosystem.profiling.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;


public class FieldTests
{
    @Test
    public void whenSerializingPolymorphic_thenCorrect()
    throws JsonProcessingException
    {
        Field field = new Field("fieldId", "panelId", "fieldName");

        String result = new ObjectMapper().writeValueAsString(field);

        assertThat(result, containsString("fieldId"));
        assertThat(result, containsString("panelId"));
        assertThat(result, containsString("fieldName"));
    }

    @Test
    public void whenDeserializingPolymorphic_thenCorrect()
    throws IOException
    {
        String json = "{\"id\":\"fieldId\",\"name\":\"fieldName\"}";

        Field field = new ObjectMapper()
            .readerFor(Field.class)
            .readValue(json);

        Assert.assertEquals("fieldId", field.getId());
        Assert.assertEquals("panelId", field.getPanelId());
        Assert.assertEquals("fieldName", field.getName());
        Assert.assertEquals(Field.class, field.getClass());
    }
}
