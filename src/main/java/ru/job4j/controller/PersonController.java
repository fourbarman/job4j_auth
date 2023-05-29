package ru.job4j.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.dto.PersonDTO;
import ru.job4j.service.PersonService;
import ru.job4j.domain.Person;
import ru.job4j.util.PersonNotFoundException;
import ru.job4j.util.SaveOrUpdateException;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * PersonController.
 *
 * @author fourbarman (maks.java@yandex.ru).
 * @version %I%, %G%.
 * @since 23.05.2023.
 */
@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService persons;
    private final BCryptPasswordEncoder encoder;

    @GetMapping("/")
    public ResponseEntity<List<Person>> findAll() {
        return new ResponseEntity<>(this.persons.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        if (id <= 0) {
            throw new IllegalStateException("id must be greater than 0");
        }
        var person = this.persons.findById(id);
        if (person.isEmpty()) {
            throw new PersonNotFoundException("Person with id " + id + " was not found");
        }
        return new ResponseEntity<>(person.get(), HttpStatus.OK);
    }

    @PostMapping(value = "/", consumes = {"application/json", "text/plain"})
    public ResponseEntity<Person> create(@RequestBody Person person) {
        String login = person.getLogin();
        String password = person.getPassword();
        if (login == null || password == null) {
            throw new NullPointerException("Login and Password should be not null");
        }
        if (login.isBlank()) {
            throw new NullPointerException("Login should not be empty");
        }
        if (password.length() < 6) {
            throw new IllegalStateException("Password should have 6 or more symbols");
        }
        var savedPerson = this.persons.save(person);
        if (savedPerson.isEmpty()) {
            throw new SaveOrUpdateException("Person already exists");
        }
        return new ResponseEntity<>(savedPerson.get(), HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Person person) {
        if (person.getId() <= 0 || person.getLogin() == null || person.getPassword() == null) {
            throw new NullPointerException("id should be grater 0 and login and Password should be not null");
        }
        var savedPerson = this.persons.save(person);
        if (savedPerson.isEmpty()) {
            throw new SaveOrUpdateException("Person already exists");
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (id <= 0) {
            throw new IllegalStateException("id must be greater than 0");
        }
        Person person = new Person();
        person.setId(id);
        if (!this.persons.delete(person)) {
            throw new PersonNotFoundException("Person with id " + id + " was not found");
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody Person person) {
        person.setPassword(encoder.encode(person.getPassword()));
        persons.save(person);
    }

    @GetMapping("/all")
    public List<Person> findAllPersons() {
        return this.persons.findAll();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PersonDTO> partialUpdate(@PathVariable int id,
                                                   @RequestBody PersonDTO personDTO)
            throws IllegalAccessException, InvocationTargetException {
        Person person = new Person(id, personDTO.getLogin(), encoder.encode(personDTO.getPassword()));
        personDTO = this.persons.partialUpdate(id, person);
        return new ResponseEntity<>(personDTO, HttpStatus.OK);
    }
}
