package com.github.hemantsonu20.spring.ai.server.repository;

import com.github.hemantsonu20.spring.ai.server.model.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends MongoRepository<Person, String> {

    List<Person> findByCityIgnoreCase(String city);

    List<Person> findByLastName(String lastName);

    List<Person> findByAgeBetween(int min, int max);

    List<Person> findByAgeGreaterThan(int age);

    List<Person> findByStateIgnoreCase(String state);
}
