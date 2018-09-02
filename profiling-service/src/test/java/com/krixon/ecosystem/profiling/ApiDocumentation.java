package com.krixon.ecosystem.profiling;

import com.krixon.ecosystem.profiling.domain.Field;
import com.krixon.ecosystem.profiling.domain.FieldRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.RequestDispatcher;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
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
    private WebApplicationContext context;
    
    private MockMvc mockMvc;
    
    @Before
    public void setUp()
    {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(documentationConfiguration(restDocumentation))
            .build();
    }

    @Test
    public void errorExample() throws Exception {
        mockMvc
            .perform(get("/error")
                .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 400)
                .requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "/notes")
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
        this.fieldRepository.deleteAll();

        createField("age","demo", "Age");
        createField("name","demo", "Name");

        this.mockMvc.perform(get("/fields"))
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

    private void createField(String id, String panelId, String name) {
        this.fieldRepository.save(new Field(id, panelId, name));
    }
}
