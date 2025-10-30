package com.github.hemantsonu20.spring.ai.server.service;

import com.github.hemantsonu20.spring.ai.server.model.Person;
import com.github.hemantsonu20.spring.ai.server.repository.PersonRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class exposing methods to be used as tools in MCP.
 * Each method is annotated with @Tool to indicate its purpose.
 * {@link Tool} annotation's description provides a brief overview of what the method does, which can be utilized by AI models.
 * These methods interact with the PersonRepository to fetch data based on various criteria.
 *
 */
@Service
public class PersonMcpService {

    private final PersonRepository repository;

    public PersonMcpService(PersonRepository repository) {
        this.repository = repository;
    }

    @Tool(description = "Find all people in a given city")
    public List<Person> findPeopleByCity(String city) {
        return repository.findByCityIgnoreCase(city);
    }

    @Tool(description = "Find all people in a given age range")
    public List<Person> findPeopleByAgeBetween(int min, int max) {
        return repository.findByAgeBetween(min, max);
    }

    @Tool(description = "Find all people older than a given age")
    public List<Person> findPeopleOlderThan(int age) {
        return repository.findByAgeGreaterThan(age);
    }

    @Tool(description = "Find people by last name")
    public List<Person> findPeopleByLastName(String lastName) {
        return repository.findByLastName(lastName);
    }

    @Tool(description = "Find all people in a given state")
    public List<Person> findPeopleByState(String state) {
        return repository.findByStateIgnoreCase(state);
    }

    @Tool(description = "List all people")
    public List<Person> findAllPeople() {
        return repository.findAll();
    }
}
