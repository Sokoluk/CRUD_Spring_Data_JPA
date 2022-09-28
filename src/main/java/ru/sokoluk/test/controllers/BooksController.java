package ru.sokoluk.test.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.sokoluk.test.entity.Book;
import ru.sokoluk.test.entity.Person;
import ru.sokoluk.test.services.BooksService;
import ru.sokoluk.test.services.PeopleService;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final BooksService booksService;
    private final PeopleService peopleService;

    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String list(Model model, @RequestParam(value = "page", required = false) Integer page,
                       @RequestParam(value = "books_page", required = false) Integer booksPage,
                       @RequestParam(value = "sort_by_year", required = false) boolean sortByYear) {
        if (page == null || booksPage == null) {
            model.addAttribute("books", booksService.findByAll(sortByYear));
        } else {
            model.addAttribute("books", booksService.findWithPagination(page, booksPage, sortByYear));
        }
        return "book/list";
    }

    @GetMapping("/{id}")
    public String index(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("book", booksService.findById(id));
        Person person1 = booksService.gettingTheBookOwner(id);
        if (person1 != null) {
            model.addAttribute("owner", person1);
        } else {
            model.addAttribute("people", peopleService.findAll());
        }

        return "book/index";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "book/new";
        }
        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("book") Book book) {
        return "book/new";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", booksService.findById(id));
        return "book/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "book/edit";
        }
        booksService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person person) {
        booksService.assign(id, person);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        booksService.release(id);
        return "redirect:/books/" + id;
    }


    @PostMapping("/search")
    public String makeSearch(Model model, @RequestParam("query") String query) {
        model.addAttribute("books",booksService.searchByTitle(query));
        return "book/search";
    }

    @GetMapping("/search")
    public String searchPage() {
        return "book/search";
    }

}

