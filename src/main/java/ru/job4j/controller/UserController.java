package ru.job4j.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Person;
import ru.job4j.service.PersonService;

import java.util.List;
/**
 * UserController.
 *
 * @author fourbarman (maks.java@yandex.ru).
 * @version %I%, %G%.
 * @since 24.05.2023.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final PersonService persons;
    private final BCryptPasswordEncoder encoder;

    @PostMapping("/sign-up")
    public void signUp(@RequestBody Person person) {
        person.setPassword(encoder.encode(person.getPassword()));
        persons.save(person);
    }

    @GetMapping("/all")
    public List<Person> findAll() {
        return this.persons.findAll();
    }
}
