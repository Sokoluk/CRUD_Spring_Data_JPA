package ru.sokoluk.test.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sokoluk.test.entity.Book;
import ru.sokoluk.test.entity.Person;
import ru.sokoluk.test.repositories.BooksRepository;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public Book findById(int id) {
        return booksRepository.findById(id).orElse(null);
    }

//    public List<Book> findByAll() {
//        return booksRepository.findAll();
//    }

    public List<Book> findByAll(boolean sortByYear) {
        if (sortByYear) {
            return booksRepository.findAll(Sort.by("year"));
        } else {
            return booksRepository.findAll();
        }
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book book) {
        book.setId(id);
        book.setPerson(booksRepository.findById(id).get().getPerson());
        booksRepository.save(book);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    public Person gettingTheBookOwner(int id) {
        return booksRepository.findById(id).map(Book::getPerson).orElse(null);
    }

    @Transactional
    public void assign(int id, Person person) {
        Book book = findById(id);
        book.setPerson(person);
        book.setDataOfIssue(new Date());
        booksRepository.save(book);
    }

    @Transactional
    public void release(int id) {
        Book book = findById(id);
        book.setPerson(null);
        book.setDataOfIssue(null);
        booksRepository.save(book);
    }

    public List<Book> findWithPagination(int page, int books_page, boolean sortByYear) {
        if (sortByYear) {
            return booksRepository.findAll(PageRequest.of(page, books_page, Sort.by("year"))).getContent();
        }
        return booksRepository.findAll(PageRequest.of(page, books_page)).getContent();
    }

    public List<Book> searchByTitle(String query){
    return booksRepository.findByTitleStartingWith(query);
    }

}

