package com.example.graphqldynamicquery;

import com.example.graphqldynamicquery.entity.Person;
import com.example.graphqldynamicquery.repository.PersonRepositoryQueryMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class GraphqldynamicqueryApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(GraphqldynamicqueryApplication.class, args);
    }

    @Autowired
    PersonRepositoryQueryMethod personRepositoryQueryMethod;

    @Override
    public void run(String... args) throws Exception {

        personRepositoryQueryMethod.saveAllAndFlush(
                List.of(
                        Person.builder()
                                .name("Abhay Agarwal").email("abay@agarwal.com")
                                .creditScore(782).dateOfBirth(LocalDate.of(1998, 2, 1))
                                .build(),
                        Person.builder()
                                .name("Virender Kumar").email("virendar@kumar.com")
                                .creditScore(790).dateOfBirth(LocalDate.of(1996, 3, 17))
                                .build(),
                        Person.builder()
                                .name("Aniket Ghosh").email("aniket@ghosh.com")
                                .creditScore(785).dateOfBirth(LocalDate.of(2001, 8, 19))
                                .build(),
                        Person.builder()
                                .name("Virender Singh").email("virendar@singh.com")
                                .creditScore(790).dateOfBirth(LocalDate.of(1996, 3, 17))
                                .build(),
                        Person.builder()
                                .name("Ankita Bansal").email("ankita@bansal.com")
                                .creditScore(790).dateOfBirth(LocalDate.of(2001, 12, 30))
                                .build(),
                        Person.builder()
                                .name("Ritika Singh").email("aniket@ghosh.com")
                                .creditScore(785).dateOfBirth(LocalDate.of(1998, 1, 10))
                                .build(),
                        Person.builder()
                                .name("Adithiyan Gopalram").email("adithiyan@gopalram.com")
                                .creditScore(785).dateOfBirth(LocalDate.of(2000, 6, 15))
                                .build()
                )
        );
    }
}
