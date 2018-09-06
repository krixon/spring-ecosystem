package com.krixon.ecosystem.profiling.web.answer;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

@Value
@EqualsAndHashCode(callSuper = true)
@Relation(value = "answer", collectionRelation = "answers")
class AnswerResource extends ResourceSupport
{
    @NonNull Object value;
}

