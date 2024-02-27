package com.example.graphqldynamicquery.repository;

import com.example.graphqldynamicquery.dto.graphqlquery.PersonQuery;
import com.example.graphqldynamicquery.entity.Person;
import com.example.graphqldynamicquery.model.Paginated;

public interface PersonRepository {

    Paginated<Person> searchPerson(PersonQuery personQuery);
}
