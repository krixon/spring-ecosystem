package com.krixon.ecosystem.profiling.api;

import brave.SpanCustomizer;
import com.krixon.ecosystem.profiling.dto.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/fields")
public class FieldController
{
    private final SpanCustomizer span;

    @Autowired
    public FieldController(SpanCustomizer span)
    {
        this.span = span;
    }

    @GetMapping("/{{panelId}}")
    @PreAuthorize("#oauth2.hasScope('panel:read')")
    public ArrayList<Field> getPanelList(@PathVariable("{panelId}") String panelId)
    {
        // TODO: Real persistence.

        ArrayList<Field> panels = new ArrayList<>();

        span.annotate("Fetch Field List Start");

        panels.add(new Field("1", "Name"));
        panels.add(new Field("2", "Address"));
        panels.add(new Field("3", "Age"));

        span.annotate("Fetch Field List Finish");

        return panels;
    }
}
