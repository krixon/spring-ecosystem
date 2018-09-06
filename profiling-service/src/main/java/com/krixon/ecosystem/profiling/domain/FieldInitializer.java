package com.krixon.ecosystem.profiling.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class FieldInitializer
{
    private final @NonNull FieldRepository fieldRepository;

    @EventListener
    public void init(ApplicationReadyEvent event) {

        if (fieldRepository.count() != 0) {
            return;
        }

        Field ageField = Field.define("age", "Age", Field.AnswerType.NUMERIC, "demo");
        Field dobField = Field.define("dob", "Date of Birth", "demo");

        fieldRepository.saveAll(Arrays.asList(ageField, dobField));
    }
}
