package com.krixon.ecosystem.panelservice.api;

import brave.SpanCustomizer;
import com.krixon.ecosystem.panelservice.dto.Panel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/panels")
public class Panels
{
    private final SpanCustomizer span;

    @Autowired
    public Panels(SpanCustomizer span)
    {
        this.span = span;
    }

    @GetMapping("")
    @PreAuthorize("#oauth2.hasScope('panel:read')")
    public ArrayList<Panel> getPanelList()
    {
        // TODO: Real persistence.

        ArrayList<Panel> panels = new ArrayList<>();

        span.annotate("Fetch Panel List Start");

        panels.add(new Panel("1", "David Lister Appreciation Society"));
        panels.add(new Panel("2", "Beef and Dairy Network"));
        panels.add(new Panel("3", "Some Panel Name"));

        span.annotate("Fetch Panel List Finish");

        return panels;
    }
}
