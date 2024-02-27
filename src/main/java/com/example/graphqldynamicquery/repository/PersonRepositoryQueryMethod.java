package com.example.graphqldynamicquery.repository;


import com.example.graphqldynamicquery.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepositoryQueryMethod extends JpaRepository<Person, String> {
}
