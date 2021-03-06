package com.krixon.ecosystem.profiling;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krixon.ecosystem.profiling.domain.Field;
import com.krixon.ecosystem.profiling.domain.FieldRepository;
import lombok.NonNull;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.RequestDispatcher;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ApiDocumentation
{
    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
    
    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private WebApplicationContext context;
    
    private MockMvc mockMvc;
    
    @Before
    public void setUp()
    {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(documentationConfiguration(restDocumentation)
                .operationPreprocessors()
                    .withRequestDefaults(prettyPrint())
                    .withResponseDefaults(prettyPrint()))
            .build();

        fieldRepository.deleteAll();
    }

    @Test
    public void errorExample() throws Exception {
        mockMvc
            .perform(get("/error")
                .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 400)
                .requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "/fields")
                .requestAttr(RequestDispatcher.ERROR_MESSAGE, "The constraint 'http://localhost:8080/constraints/123' does not exist."))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("error", is("Bad Request")))
            .andExpect(jsonPath("timestamp", is(notNullValue())))
            .andExpect(jsonPath("status", is(400)))
            .andExpect(jsonPath("path", is(notNullValue())))
            .andDo(document("error-example",
                responseFields(
                    fieldWithPath("error").description("The HTTP error that occurred, e.g. `Bad Request`."),
                    fieldWithPath("message").description("A description of the cause of the error."),
                    fieldWithPath("path").description("The path to which the request was made."),
                    fieldWithPath("status").description("The HTTP status code, e.g. `400`."),
                    fieldWithPath("timestamp").description("The time, in milliseconds, at which the error occurred."))));
    }

    @Test
    public void indexExample() throws Exception
    {
        this.mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andDo(document("index-example",
                links(
                    linkWithRel("fields").description("The <<resources-fields,Fields resource>>."),
                    linkWithRel("answers").description("The <<resources-answers,Answers resource>> for the specified panel member.")),
                responseFields(
                    subsectionWithPath("_links").description("<<resources-index-links,Links>> to other resources"))));

    }

    @Test
    public void fieldListExample() throws Exception
    {
        createField("age", "Age");
        createField("name", "Name");

        mockMvc.perform(get("/fields"))
            .andExpect(status().isOk())
            .andDo(document("fields-list-example",
                links(
                    linkWithRel("self").description("Canonical link for this resource.")),
                responseFields(
                    subsectionWithPath("_embedded.fields").description("A collection of <<resources-field, Field resources>>."),
                    subsectionWithPath("page").description("<<overview-pagination,Pagination information>>."),
                    subsectionWithPath("_links").description("<<resources-tags-list-links, Links>> to other resources."))));
    }

    @Test
    public void fieldCreateExample() throws Exception
    {
        Map<String, String> field = createFieldData(
            "example-panel",
            "Date of Birth"
        );

        FieldDescriptor panelField = panelField();

        String fieldLocation = putField("example", field)
            .andDo(document("field-create-example",
                requestFields(
                    panelField
                        .description(panelField.getDescription() + "\n\nIf `panel` is null or omitted, a global field will be created.")
                        .optional(),
                    nameField())))
            .andReturn()
                .getResponse()
                .getHeader("Location");

        verifyFieldResource(fieldLocation, field);
    }

    @Test
    public void fieldGetExample() throws Exception
    {
        Map<String, String> field = createFieldData(
            "example-panel",
            "Date of Birth"
        );

        String fieldLocation = putField("example", field)
            .andReturn()
                .getResponse()
                .getHeader("Location");

        verifyFieldResource(fieldLocation, field)
            .andDo(print())
            .andDo(document("field-get-example",
                links(
                    linkWithRel("self").description("Canonical link for this <<resources-field,field>>.")),
                responseFields(
                    panelField(),
                    nameField(),
                    subsectionWithPath("_links").description("<<resources-field-links,Links>> to other resources."))));
    }

    @Test
    public void fieldUpdateExample() throws Exception
    {
        Map<String, String> field = createFieldData(
            "example-panel",
            "Date of Birth"
        );

        String fieldLocation = putField("example", field)
            .andReturn()
                .getResponse()
                .getHeader("Location");

        verifyFieldResource(fieldLocation, field);

        Map<String, String> fieldUpdate = new HashMap<>();
        fieldUpdate.put("name", "New Name");
        fieldUpdate.put("panel", "new-panel");

        putField("example", fieldUpdate)
            .andDo(document("field-update-example",
                requestFields(
                    nameField(),
                    panelField())));
    }

    private FieldDescriptor panelField()
    {
        return fieldWithPath("panel")
            .description("The unique identifier of the panel which owns the field.")
            .type(JsonFieldType.STRING);
    }

    private FieldDescriptor nameField()
    {
        return fieldWithPath("name")
            .description("A human-friendly name for the field.")
            .type(JsonFieldType.STRING);
    }

    private ResultActions putField(String id, final Map<String, String> field) throws Exception
    {
        return mockMvc
            .perform(
                put("/fields/" + id)
                    .contentType(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(field)))
            .andExpect(status().isCreated());
    }

    private ResultActions verifyFieldResource(final @NonNull String fieldLocation, final @NonNull Map<String, String> field) throws Exception
    {
        return mockMvc
            .perform(get(fieldLocation))
            .andExpect(status().isOk())
            .andExpect(jsonPath("panel", is(field.get("panel"))))
            .andExpect(jsonPath("name", is(field.get("name"))))
            .andExpect(jsonPath("_links.self.href", is(fieldLocation)));
    }

    private Map<String, String> createFieldData(final String panelId, final String name)
    {
        Map<String, String> field = new HashMap<>();

        field.put("panel", panelId);
        field.put("name", name);

        return field;
    }

    private void createField(String id, String name)
    {
        this.fieldRepository.save(Field.define(id, name, "demo"));
    }
}
