package com.github.hemantsonu20.spring.ai.server.seed;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hemantsonu20.spring.ai.server.model.Person;
import com.github.hemantsonu20.spring.ai.server.repository.PersonRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

/**
 * Component that seeds the MongoDB database with initial Person data from a JSON file upon application startup.
 * It implements InitializingBean to execute the seeding logic after all properties are set.
 * If the Person collection is empty, it reads the data from 'persons.json' and saves it to the repository.
 */
@Component
public class PersonDataSeeder implements InitializingBean {

    private final PersonRepository repository;
    private final ObjectMapper mapper;

    public PersonDataSeeder(PersonRepository repository, ObjectMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (repository.count() == 0) {
            InputStream is = getClass().getResourceAsStream("/persons.json");
            List<Person> people = mapper.readValue(is, new TypeReference<>() {
            });
            repository.saveAll(people);
            System.out.println("✅ Seeded " + people.size() + " people into MongoDB");
        }
    }
}
