package com.krixon.ecosystem.profiling.web.answer;

import com.krixon.ecosystem.profiling.domain.Answer;
import com.krixon.ecosystem.profiling.domain.Field;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class AnswerResourceAssembler extends ResourceAssemblerSupport<Answer, AnswerResource>
{
    private final EntityLinks entityLinks;

    public AnswerResourceAssembler(EntityLinks entityLinks)
    {
        super(AnswerController.class, AnswerResource.class);

        this.entityLinks = entityLinks;
    }

    @Override
    public AnswerResource toResource(Answer entity)
    {
        Assert.notNull(entity, "Entity must not be null.");

        AnswerResource resource = new AnswerResource(entity.getValue());

        resource.add(
//            linkTo(methodOn(AnswerController.class)., entity.getMemberId(), entity.getFieldId()).withSelfRel(),
            entityLinks.linkToSingleResource(Field.class, entity.getFieldId()).withRel("field")
        );

        return resource;
    }
}
