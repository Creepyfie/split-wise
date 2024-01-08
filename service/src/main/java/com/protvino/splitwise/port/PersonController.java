package com.protvino.splitwise.port;

import com.protvino.splitwise.port.dto.PersonDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/person")
public class PersonController {

    Map<Long, PersonDto> persons = new HashMap<>();

    @GetMapping("/{id}")
    public PersonDto getPerson(@PathVariable Long id) {
        return persons.get(id);
    }

    @PostMapping
    public Long createPerson() {
        PersonDto person = PersonDto.builder()
            .id(ThreadLocalRandom.current().nextLong())
            .firstName("firstName_" + ThreadLocalRandom.current().nextInt(1000))
            .lastName("lastName_" + ThreadLocalRandom.current().nextInt(1000))
            .build();
        persons.put(person.getId(), person);
        return person.getId();
    }
}
