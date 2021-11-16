package com.example.springintro.service.impl;

import com.example.springintro.model.entity.*;
import com.example.springintro.repository.BookRepository;
import com.example.springintro.service.AuthorService;
import com.example.springintro.service.BookService;
import com.example.springintro.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Files
                .readAllLines(Path.of(BOOKS_FILE_PATH))
                .forEach(row -> {
                    String[] bookInfo = row.split("\\s+");
                    Book book = createBookFromInfo(bookInfo);
                    bookRepository.save(book);
                });
    }

    //P01
    @Override
    public List<String> findAllByAgeRestriction(AgeRestriction ageRestriction) {
        return this.bookRepository
                .findAllByAgeRestriction(ageRestriction)
                .stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());
    }

    //P02
    @Override
    public List<String> findAllByEditionTypeAndCopiesLessThan(EditionType editionType, Integer copies) {
        return this.bookRepository
                .findAllByEditionTypeAndCopiesLessThan(editionType, copies)
                .stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());
    }

    //P03
    @Override
    public List<String> findAllByPriceLessThanOrPriceGreaterThan(BigDecimal lower, BigDecimal upper) {
        return this.bookRepository
                .findAllByPriceLessThanOrPriceGreaterThan(lower, upper)
                .stream()
                .map(b -> String.format("%s - $%.2f", b.getTitle(), b.getPrice()))
                .collect(Collectors.toList());
    }

    //P04
    @Override
    public List<String> findAllByReleaseDateBeforeOrReleaseDateAfter(LocalDate start, LocalDate end) {
        return this.bookRepository.findAllByReleaseDateBeforeOrReleaseDateAfter(start, end)
                .stream().map(Book::getTitle)
                .collect(Collectors.toList());
    }

    //P05
    @Override
    public List<String> findAllByReleaseDateBefore(LocalDate releaseDate) {
        return this.bookRepository
                .findAllByReleaseDateBefore(releaseDate)
                .stream()
                .map(b -> String.format("%s %s %.2f", b.getTitle(), b.getEditionType().name(), b.getPrice()))
                .collect(Collectors.toList());
    }

    //P07
    @Override
    public List<String> findAllByTitleContaining(String pattern) {
        return this.bookRepository
                .findAllByTitleContaining(pattern);
    }

    //P08
    @Override
    public List<String> findAllByAuthor_FirstNameStartingWith(String pattern) {
        return this.bookRepository
                .findAllByAuthor_FirstNameStartingWith(pattern)
                .stream()
                .map(b -> String.format("%s (%s %s)",
                        b.getTitle(), b.getAuthor().getFirstName(), b.getAuthor().getLastName()))
                .collect(Collectors.toList());
    }

//    @Override
//    public List<String> findAllByTitleFromAuthorsWithFirstNameStartingWith(String pattern) {
//        return this.bookRepository.findAllByTitleFromAuthorsWithFirstNameStartingWith(pattern);
//    }

    //P09
    @Override
    public int findBooksCountWithTitleLongerThan(int length) {
        return this.bookRepository.findBooksCountWithTitleLongerThan(length);
    }

//    //P10
//    @Override
//    public int countBookCopiesByAuthor(String firstName, String lastName) {
//        return this.bookRepository.countBookCopiesByAuthor(firstName, lastName);
//    }

    @Override
    public List<Book> findAllByTitle(String title) {
        return this.bookRepository.findAllByTitle(title);
    }

    @Override
    @Transactional
    public int updateBookCopiesReleasedAfterGivenDateWith(LocalDate date, int increaseNumber) {
        return this.bookRepository.updateBookCopiesReleasedAfterGivenDateWith(date, increaseNumber);
    }

    @Override
    @Transactional
    public int removeAllByCopiesLessThan(Integer copies) {
        return this.bookRepository.removeAllByCopiesLessThan(copies);
    }

    @Override
    public int findBookTitlesByAuthorNames(String f_name, String l_name) {
        return this.bookRepository.findBookTitlesByAuthorFullName(f_name,l_name);
    }


    private Book createBookFromInfo(String[] bookInfo) {
        EditionType editionType = EditionType.values()[Integer.parseInt(bookInfo[0])];
        LocalDate releaseDate = LocalDate
                .parse(bookInfo[1], DateTimeFormatter.ofPattern("d/M/yyyy"));
        Integer copies = Integer.parseInt(bookInfo[2]);
        BigDecimal price = new BigDecimal(bookInfo[3]);
        AgeRestriction ageRestriction = AgeRestriction
                .values()[Integer.parseInt(bookInfo[4])];
        String title = Arrays.stream(bookInfo)
                .skip(5)
                .collect(Collectors.joining(" "));

        Author author = authorService.getRandomAuthor();
        Set<Category> categories = categoryService
                .getRandomCategories();

        return new Book(editionType, releaseDate, copies, price, ageRestriction, title, author, categories);

    }
}
