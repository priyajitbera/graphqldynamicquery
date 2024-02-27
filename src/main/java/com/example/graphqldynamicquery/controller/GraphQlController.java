package com.example.graphqldynamicquery.controller;

import com.example.graphqldynamicquery.dto.graphqlquery.PersonQuery;
import com.example.graphqldynamicquery.entity.Person;
import com.example.graphqldynamicquery.model.Paginated;
import com.example.graphqldynamicquery.repository.PersonRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class GraphQlController {

    private final PersonRepository personRepository;

    public GraphQlController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @QueryMapping
    public Paginated<Person> searchPersons(
            @Argument PersonQuery personQuery
    ) {
        return personRepository.searchPerson(personQuery);
    }
}
