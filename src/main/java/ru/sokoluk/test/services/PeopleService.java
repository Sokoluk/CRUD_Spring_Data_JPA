package ru.sokoluk.test.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sokoluk.test.entity.Book;
import ru.sokoluk.test.entity.Person;
import ru.sokoluk.test.repositories.BooksRepository;
import ru.sokoluk.test.repositories.PeopleRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;


    @Autowired
    public PeopleService(PeopleRepository peopleRepository, BooksRepository booksRepository) {
        this.peopleRepository = peopleRepository;
    }

    public Person findOne(int id) {
        return peopleRepository.findById(id).orElse(null);
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    public List<Book> takenBooksOnPersonId(int id) {
        if (peopleRepository.findById(id).get().getBooks().isEmpty())
            return Collections.emptyList();
        else {
            List<Book> books = peopleRepository.findById(id).get().getBooks();
            for (Book it : books) {
                long time = Math.abs(it.getDataOfIssue().getTime() - new Date().getTime());
                if (time > 864000000) {
                    it.setDelay(true);
                }
            }
            return peopleRepository.findById(id).get().getBooks();
        }
    }


}
