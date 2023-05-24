package ru.job4j.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.job4j.domain.Person;
import ru.job4j.repository.PersonRepository;

import java.util.List;
import java.util.Optional;
/**
 * PersonService.
 *
 * @author fourbarman (maks.java@yandex.ru).
 * @version %I%, %G%.
 * @since 23.05.2023.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;

    public List<Person> findAll() {
        return this.personRepository.findAll();
    }

    public Optional<Person> findById(int id) {
        return this.personRepository.findById(id);
    }

    public Optional<Person> save(Person person) {
        Optional<Person> savedPerson = Optional.empty();
        try {
            savedPerson = Optional.of(this.personRepository.save(person));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return savedPerson;
    }

    public boolean delete(Person person) {
        if (personRepository.existsById(person.getId())) {
            personRepository.delete(person);
            return true;
        }
        return false;
    }
}
