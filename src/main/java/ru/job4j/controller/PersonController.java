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

import javax.validation.Valid;
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
    public ResponseEntity<Person> findById(@Valid @PathVariable int id) {
        var person = this.persons.findById(id);
        if (person.isEmpty()) {
            throw new PersonNotFoundException("Person with id " + id + " was not found");
        }
        return new ResponseEntity<>(person.get(), HttpStatus.OK);
    }

    @PostMapping(value = "/", consumes = {"application/json", "text/plain"})
    public ResponseEntity<Person> create(@Valid @RequestBody Person person) {
        var savedPerson = this.persons.save(person)
                .orElseThrow(() -> new SaveOrUpdateException("Person already exists"));
        return new ResponseEntity<>(savedPerson, HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@Valid @RequestBody Person person) {
        this.persons.save(person).orElseThrow(() -> new SaveOrUpdateException("Person already exists"));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Valid @PathVariable int id) {
        Person person = new Person();
        person.setId(id);
        if (!this.persons.delete(person)) {
            throw new PersonNotFoundException("Person with id " + id + " was not found");
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sign-up")
    public void signUp(@Valid @RequestBody Person person) {
        person.setPassword(encoder.encode(person.getPassword()));
        persons.save(person);
    }

    @GetMapping("/all")
    public List<Person> findAllPersons() {
        return this.persons.findAll();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Person> partialUpdate(@Valid @PathVariable int id, @Valid @RequestBody PersonDTO personDTO) {
        Person savedPerson = this.persons.save(
                        new Person(
                                id,
                                personDTO.getLogin(),
                                encoder.encode(personDTO.getPassword()))
                )
                .orElseThrow(
                        () -> new SaveOrUpdateException("Person with login " + personDTO.getLogin() + " already exists")
                );
        return new ResponseEntity<>(savedPerson, HttpStatus.OK);
    }
}
