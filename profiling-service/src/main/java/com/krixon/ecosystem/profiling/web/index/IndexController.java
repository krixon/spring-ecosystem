package com.krixon.ecosystem.profiling.web.index;

import com.krixon.ecosystem.profiling.domain.Field;
import com.krixon.ecosystem.profiling.web.answer.AnswerController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityLinks;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/")
@Slf4j
public class IndexController
{
    private final EntityLinks entityLinks;

    public IndexController(EntityLinks entityLinks)
    {
        this.entityLinks = entityLinks;
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public HttpEntity<?> optionsForRepositories()
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setAllow(Collections.singleton(HttpMethod.GET));

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.HEAD)
    public ResponseEntity<?> headForRepositories()
    {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<?> listRepositories()
    {
        IndexResource resource = new IndexResource();

        resource.add(entityLinks.linkToCollectionResource(Field.class).withRel("fields"));
        resource.add(linkTo(methodOn(AnswerController.class).collection(null, null, null)).withRel("answers"));

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }
}
