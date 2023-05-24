package ru.job4j.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.service.PersonService;
import ru.job4j.domain.Person;

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
    public List<Person> findAll() {
        return this.persons.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        var person = this.persons.findById(id);
        return new ResponseEntity<>(
                person.orElse(new Person()),
                person.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping(value = "/", consumes = {"application/json", "text/plain"})
    public ResponseEntity<Person> create(@RequestBody Person person) {
        var savedPerson = this.persons.save(person);
        return new ResponseEntity<>(
                savedPerson.orElse(new Person()),
                savedPerson.isPresent() ? HttpStatus.CREATED : HttpStatus.CONFLICT
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Person person) {
        var savedPerson = this.persons.save(person);
        return new ResponseEntity<>(
                savedPerson.isPresent() ? HttpStatus.OK : HttpStatus.NOT_MODIFIED
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Person person = new Person();
        person.setId(id);
        if (!this.persons.delete(person)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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
}
