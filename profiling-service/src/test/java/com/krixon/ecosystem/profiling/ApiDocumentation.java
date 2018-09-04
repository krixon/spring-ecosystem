package com.krixon.ecosystem.profiling;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krixon.ecosystem.profiling.domain.Field;
import com.krixon.ecosystem.profiling.domain.FieldRepository;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
    public void indexExample() throws Exception {
        this.mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andDo(document("index-example",
                links(
                    linkWithRel("fields").description("The <<resources-fields,Fields resource>>."),
                    linkWithRel("profile").description("The ALPS profile for the service.")),
                responseFields(
                    subsectionWithPath("_links").description("<<resources-index-links,Links>> to other resources"))));

    }

    @Test
    public void fieldListExample() throws Exception {
        fieldRepository.deleteAll();

        createField("age","demo", "Age");
        createField("name","demo", "Name");

        mockMvc.perform(get("/fields"))
            .andExpect(status().isOk())
            .andDo(document("fields-list-example",
                links(
                    linkWithRel("self").description("Canonical link for this resource."),
                    linkWithRel("profile").description("The ALPS profile for this resource.")),
                responseFields(
                    subsectionWithPath("_embedded.fields").description("A collection of <<resources-field, Field resources>>."),
                    subsectionWithPath("page").description("<<overview-pagination,Pagination information>>."),
                    subsectionWithPath("_links").description("<<resources-tags-list-links, Links>> to other resources."))));
    }

    @Test
    public void fieldCreateExample() throws Exception
    {
        Map<String, String> field = createFieldData(
            "ebcc68c0-7c0e-45fc-a0cd-b7c631f45dd4",
            "ff1cc98a-1e03-48b3-8f21-f89be1918f16",
            "Date of Birth"
        );

        String fieldLocation = postField(field)
            .andDo(document("fields-create-example",
                requestFields(
                    idField(),
                    panelIdField(),
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
            "ebcc68c0-7c0e-45fc-a0cd-b7c631f45dd4",
            "ff1cc98a-1e03-48b3-8f21-f89be1918f16",
            "Date of Birth"
        );

        String fieldLocation = postField(field)
            .andReturn()
                .getResponse()
                .getHeader("Location");

        verifyFieldResource(fieldLocation, field)
            .andDo(print())
            .andDo(document("field-get-example",
                links(
                    linkWithRel("self").description("Canonical link for this <<resources-field,field>>."),
                    linkWithRel("field").description("This <<resources-field,field>>.")),
                responseFields(
                    idField(),
                    panelIdField(),
                    nameField(),
                    subsectionWithPath("_links").description("<<resources-field-links,Links>> to other resources."))));
    }

    @Test
    public void fieldUpdateExample() throws Exception {

        Map<String, String> field = createFieldData(
            "ebcc68c0-7c0e-45fc-a0cd-b7c631f45dd4",
            "ff1cc98a-1e03-48b3-8f21-f89be1918f16",
            "Date of Birth"
        );

        String fieldLocation = postField(field)
            .andReturn()
                .getResponse()
                .getHeader("Location");

        verifyFieldResource(fieldLocation, field);

        Map<String, String> fieldUpdate = new HashMap<>();
        fieldUpdate.put("name", "New Name");

        this.mockMvc.perform(
            patch(fieldLocation)
                .contentType(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(fieldUpdate)))
            .andExpect(status().isNoContent())
            .andDo(document("field-update-example",
                requestFields(
                    nameField().optional())));
    }

    private FieldDescriptor idField()
    {
        return fieldWithPath("id")
            .description("The unique identifier of the field.")
            .type(JsonFieldType.STRING);
    }

    private FieldDescriptor panelIdField()
    {
        return fieldWithPath("panelId")
            .description("The unique identifier of the panel which owns the field.")
            .type(JsonFieldType.STRING);
    }

    private FieldDescriptor nameField()
    {
        return fieldWithPath("name")
            .description("A human-friendly name for the field.")
            .type(JsonFieldType.STRING);
    }

    private ResultActions postField(final Map<String, String> field) throws Exception
    {
        return mockMvc
            .perform(
                post("/fields")
                    .contentType(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(field)))
            .andExpect(status().isCreated());
    }

    private ResultActions verifyFieldResource(final String fieldLocation, final Map<String, String> field) throws Exception
    {
        return mockMvc
            .perform(get(fieldLocation))
            .andExpect(status().isOk())
            .andExpect(jsonPath("id", is(field.get("id"))))
            .andExpect(jsonPath("panelId", is(field.get("panelId"))))
            .andExpect(jsonPath("name", is(field.get("name"))))
            .andExpect(jsonPath("_links.self.href", is(fieldLocation)))
            .andExpect(jsonPath("_links.field.href", is(fieldLocation)));
    }

    private Map<String, String> createFieldData(final String id, final String panelId, final String name)
    {
        Map<String, String> field = new HashMap<>();

        field.put("id", id);
        field.put("panelId", panelId);
        field.put("name", name);

        return field;
    }

    private void createField(String id, String panelId, String name)
    {
        this.fieldRepository.save(new Field(id, panelId, name));
    }
}
