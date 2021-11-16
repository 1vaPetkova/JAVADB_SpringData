package com.example.java_db_06_exercise.service.impl;


import com.example.java_db_06_exercise.model.entity.Author;
import com.example.java_db_06_exercise.model.entity.Book;
import com.example.java_db_06_exercise.model.entity.Category;
import com.example.java_db_06_exercise.model.enums.AgeRestriction;
import com.example.java_db_06_exercise.model.enums.EditionType;
import com.example.java_db_06_exercise.repository.BookRepository;
import com.example.java_db_06_exercise.service.interfaces.AuthorService;
import com.example.java_db_06_exercise.service.interfaces.BookService;
import com.example.java_db_06_exercise.service.interfaces.CategoryService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private static final String BOOKS_FILE_PATH = "src/main/resources/files/books.txt";
    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final CategoryService categoryService;


    public BookServiceImpl(BookRepository bookRepository, AuthorService authorService, CategoryService categoryService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.categoryService = categoryService;
    }

    @Override
    public void seedBooks() throws IOException {
        if (bookRepository.count() > 0) {
            return;
        }

       Files.readAllLines(Path.of(BOOKS_FILE_PATH))
                .stream()
                .filter(l -> !l.isEmpty()).collect(Collectors.toList())
                .forEach(b -> {
                    String[] tokens = b.split("\\s+");
                    Book book = createBook(tokens);
                    this.bookRepository.save(book);
                });

    }

    @Override
    public List<Book> findAllBooksAfterYear(int year) {
        return bookRepository.findAllByReleaseDateAfter(LocalDate.of(year, 12, 31));
    }

    @Override
    public List<Book> findAllBooksBeforeYear(int year) {
        return bookRepository.findAllByReleaseDateBefore(LocalDate.of(year, 1, 1));
    }

    @Override
    public List<Book> findAllBooksByAuthorFirstAndLastNameOrderByReleaseDateAndTitle(String firstName, String lastName) {
        return this.bookRepository
                .findAllByAuthor_FirstNameAndAuthor_LastNameOrderByReleaseDateDescTitle(firstName,lastName);
    }


    private Book createBook(String[] tokens) {
        Book book = new Book();

        EditionType editionType = EditionType.values()[Integer.parseInt(tokens[0])];
        book.setEditionType(editionType);

        LocalDate releaseDate = LocalDate.parse(tokens[1], DateTimeFormatter.ofPattern("d/M/yyyy"));
        book.setReleaseDate(releaseDate);

        int copies = Integer.parseInt(tokens[2]);
        book.setCopies(copies);

        BigDecimal price = new BigDecimal(tokens[3]);
        book.setPrice(price);
        AgeRestriction ageRestriction = AgeRestriction.values()[Integer.parseInt(tokens[4])];
        book.setAgeRestriction(ageRestriction);

        String title = Arrays.stream(tokens).skip(5).collect(Collectors.joining(" "));
        book.setTitle(title);

        Author author = this.authorService.getRandomAuthor();
        book.setAuthor(author);

        Set<Category> categories = this.categoryService.getRandomCategories();
        book.setCategories(categories);

        return book;
    }
}
