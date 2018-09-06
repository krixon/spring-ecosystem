package com.krixon.ecosystem.profiling.web.answer;

import com.krixon.ecosystem.profiling.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@ExposesResourceFor(Answer.class)
@RequestMapping("/answers/{memberId}")
public class AnswerController
{
    private final FieldRepository fieldRepository;
    private final AnswerRepository answerRepository;
    private final AnswerResourceAssembler assembler;

    public AnswerController(
        FieldRepository fieldRepository,
        AnswerRepository answerRepository,
        AnswerResourceAssembler assembler
    ) {
        this.fieldRepository = fieldRepository;
        this.answerRepository = answerRepository;
        this.assembler = assembler;
    }

    @GetMapping
    public ResponseEntity<? extends ResourceSupport> collection(
        @PathVariable("memberId") String memberId,
        @PageableDefault Pageable p,
        PagedResourcesAssembler<Answer> pageAssembler
    ) {
        Page<Answer> page = answerRepository.findAllByIdMemberId(memberId, p);
        PagedResources resource = pageAssembler.toResource(page, assembler);

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @PutMapping("/{fieldId}")
    public ResponseEntity<? extends ResourceSupport> submitAnswer(
        @PathVariable("memberId") String memberId,
        @PathVariable("fieldId") String fieldId,
        @RequestBody AnswerSubmission payload
    ) {
        if (memberId == null || fieldId == null) {
            return ResponseEntity.notFound().build();
        }

        Optional<Field> field = fieldRepository.findById(fieldId);

        if (!field.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        AnswerId id = new AnswerId(memberId, fieldId);
        Answer answer;

        try {
            answer = answerRepository
                .findById(id)
                .map(a -> {
                    a.resubmit(payload);
                    return a;
                })
                .orElseGet(() -> {
                    switch (field.get().getAnswerType()) {
                        case NUMERIC: return NumericAnswer.submit(id, payload);
                        case TEXTUAL: return TextualAnswer.submit(id, payload);
                        default: throw new UnsupportedOperationException();
                    }
                });
        } catch (UnsupportedOperationException e) {
            return ResponseEntity.badRequest().build();
        }

        Answer savedAnswer = answerRepository.save(answer);

        return new ResponseEntity<>(assembler.toResource(savedAnswer), HttpStatus.CREATED);
    }
}
