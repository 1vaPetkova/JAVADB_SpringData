package com.example.java_db_06_exercise;

import com.example.java_db_06_exercise.model.entity.Book;
import com.example.java_db_06_exercise.service.interfaces.AuthorService;
import com.example.java_db_06_exercise.service.interfaces.BookService;
import com.example.java_db_06_exercise.service.interfaces.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;

    public CommandLineRunnerImpl(CategoryService categoryService, AuthorService authorService, BookService bookService) {
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();
        // Queries:
        // printAllBookTitlesAfterYear(2000);
        // printAllAuthorsWithAtLeastOneBookReleasedBeforeYear(1990);
        // printAllAuthorsByNumberOfBooks();
        printAllBooksByAuthorNameOrderedByReleaseDate("George Powell");

    }

    private void printAllBooksByAuthorNameOrderedByReleaseDate(String name) {
        String firstName = name.split("\\s+")[0];
        String lastName = name.split("\\s+")[1];
        this.bookService
                .findAllBooksByAuthorFirstAndLastNameOrderByReleaseDateAndTitle(firstName, lastName)
                .forEach(b -> System.out.printf("%s %s %d%n", b.getTitle(), b.getReleaseDate(), b.getCopies()));
    }

    private void printAllAuthorsByNumberOfBooks() {
        this.authorService
                .getAllAuthorsByCountOfBooksDesc()
                .forEach(a -> System.out.printf("%s %s %d%n", a.getFirstName(), a.getLastName(), a.getBooks().size()));
    }


    private void printAllAuthorsWithAtLeastOneBookReleasedBeforeYear(int year) {
        this.bookService.findAllBooksBeforeYear(year)
                .stream()
                .map(Book::getAuthor)
                .distinct()
                .forEach(a -> System.out.println(a.getFirstName() + " " + a.getLastName()));
    }

    private void printAllBookTitlesAfterYear(int year) {
        bookService.findAllBooksAfterYear(2000)
                .stream()
                .map(Book::getTitle)
                .forEach(System.out::println);
    }

    private void seedData() throws IOException {
        categoryService.seedCategories();
        authorService.seedAuthors();
        bookService.seedBooks();
    }
}
