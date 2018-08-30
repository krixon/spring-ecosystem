package com.krixon.ecosystem.profiling.api;

import com.krixon.ecosystem.profiling.dto.Field;
import com.krixon.ecosystem.profiling.dto.NumberField;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/{panelId}/fields")
public class Fields
{
    @PostMapping()
    @PreAuthorize("#oauth2.hasScope('panel:write')")
    public ResponseEntity<?> define(@PathVariable String panelId, @RequestBody Field field)
    throws URISyntaxException
    {
        // TODO: Persistence.

        Resource<Field> resource = new Resource<>(
            field,
            linkTo(methodOn(Fields.class).one(panelId, field.getId())).withSelfRel()
        );

        return ResponseEntity
            .created(new URI(resource.getId().expand().getHref()))
            .body(resource);
    }

    @GetMapping("/{fieldId}")
    public ResponseEntity<?> one(@PathVariable String panelId, @PathVariable String fieldId)
    {
        Field field = new NumberField("age", "Age", 0.0, 100.0);

        // TODO: This is duplicated code, refactor into a FieldResourceBuilder.
        Resource<Field> resource = new Resource<>(
            field,
            linkTo(methodOn(Fields.class).one(panelId, fieldId)).withSelfRel()
        );

        return ResponseEntity.ok().body(resource);
    }
}
