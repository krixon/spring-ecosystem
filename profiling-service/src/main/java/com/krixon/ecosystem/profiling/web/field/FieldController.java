package com.krixon.ecosystem.profiling.web.field;

import com.krixon.ecosystem.profiling.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@ExposesResourceFor(Field.class)
@RequestMapping("/fields")
public class FieldController
{
    private final FieldRepository fieldRepository;
    private final FieldResourceAssembler assembler;

    public FieldController(FieldRepository fieldRepository, FieldResourceAssembler assembler)
    {
        this.fieldRepository = fieldRepository;
        this.assembler = assembler;
    }

    @GetMapping
    public ResponseEntity<? extends ResourceSupport> collection(
        @PageableDefault Pageable p,
        PagedResourcesAssembler<Field> pageAssembler
    ) {
        Page<Field> page = fieldRepository.findAll(p);
        PagedResources resource = pageAssembler.toResource(page, assembler);

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends ResourceSupport> one(@PathVariable("id") String id)
    {
        Optional<Field> field = fieldRepository.findById(id);

        if (!field.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        FieldResource resource = assembler.toResource(field.get());

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> define(
        @PathVariable("id") String id,
        @RequestBody FieldResource resource
    ) {
        Field field = fieldRepository
            .findById(id)
            .map(f -> {
                if (resource.getPanel() == null) {
                    f.promoteToGlobal();
                } else {
                    f.transferToPanel(resource.getPanel());
                }

                f.rename(resource.getName());

                return f;
            })
            .orElseGet(() -> Field.define(id, resource.getName(), resource.getPanel()));

        Field savedField = fieldRepository.save(field);
        FieldResource newResource = assembler.toResource(savedField);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(newResource.getLink("self").getTemplate().expand());

        return new ResponseEntity<>(newResource, headers, HttpStatus.CREATED);
    }
}
