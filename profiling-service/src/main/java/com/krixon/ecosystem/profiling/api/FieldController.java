package com.krixon.ecosystem.profiling.api;

import com.krixon.ecosystem.profiling.dto.Field;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/fields/{panelId}")
public class FieldController
{
    @PostMapping()
    @PreAuthorize("#oauth2.hasScope('panel:write')")
    public ResponseEntity<?> defineField(@PathVariable String panelId, @RequestBody Field field) throws URISyntaxException
    {
        // TODO: Persistence.

        Resource<Field> resource = new Resource<>(
            field,
            linkTo(methodOn(FieldController.class).defineField(panelId, field)).withSelfRel()
        );

        return ResponseEntity
            .created(new URI(resource.getId().expand().getHref()))
            .body(resource);
    }
}
