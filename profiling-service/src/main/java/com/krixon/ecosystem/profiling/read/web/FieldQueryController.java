package com.krixon.ecosystem.profiling.read.web;

import com.krixon.ecosystem.profiling.domain.Field;
import com.krixon.ecosystem.profiling.domain.FieldRepository;
import com.krixon.ecosystem.profiling.write.web.FieldCommandController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController()
@RequestMapping("/fields")
public class FieldQueryController
{
    private final FieldRepository fieldRepository;

    @Autowired
    public FieldQueryController(FieldRepository fieldRepository)
    {
        this.fieldRepository = fieldRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> one(@PathVariable("id") String id) throws URISyntaxException
    {
        final Optional<Field> field = fieldRepository.findById(id);

        if (!field.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Resource<Field> resource = new Resource<>(
            field.get(),
            linkTo(methodOn(FieldQueryController.class).one(id)).withSelfRel(),
            linkTo(methodOn(FieldCommandController.class).define(null)).withRel("define")
        );

        return ResponseEntity
            .created(new URI(resource.getId().expand().getHref()))
            .body(resource);
    }
}
