package com.krixon.ecosystem.profiling.write.web;

import com.krixon.ecosystem.profiling.domain.Field;
import com.krixon.ecosystem.profiling.domain.FieldRepository;
import com.krixon.ecosystem.profiling.domain.NumberField;
import com.krixon.ecosystem.profiling.write.command.DefineField;
import com.krixon.ecosystem.profiling.write.command.DefineNumberField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/commands/fields")
public class FieldCommandController
{
    private final FieldRepository fieldRepository;

    @Autowired
    public FieldCommandController(FieldRepository fieldRepository)
    {
        this.fieldRepository = fieldRepository;
    }

    @PostMapping(value = "/define")
    public ResponseEntity<?> define(@Valid @RequestBody DefineField command)
    {
        // TODO FieldDefinitionService.

        Field field = null;

        if (command instanceof DefineNumberField) {
            field = defineNumberField((DefineNumberField) command);
        }

        if (field == null) {
            return ResponseEntity.badRequest().build();
        }

        fieldRepository.save(field);

        return ResponseEntity.accepted().build();
    }

    private Field defineNumberField(DefineNumberField command)
    {
        return NumberField.define(
            command.getId(),
            command.getPanelId(),
            command.getName(),
            command.getMin(),
            command.getMax()
        );
    }
}
