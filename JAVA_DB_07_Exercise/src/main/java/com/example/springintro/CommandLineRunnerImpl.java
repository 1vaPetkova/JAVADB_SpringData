package com.example.springintro;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.EditionType;
import com.example.springintro.service.AuthorService;
import com.example.springintro.service.BookService;
import com.example.springintro.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;
    private final BufferedReader reader;

    public CommandLineRunnerImpl(CategoryService categoryService, AuthorService authorService, BookService bookService, BufferedReader reader) {
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;
        this.reader = reader;
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();
        System.out.println("Enter problem: ");
        int problem = Integer.parseInt(reader.readLine());
        switch (problem) {
            case 1 -> p01BookTitlesByAgeRestriction();
            case 2 -> p02GoldenBooks();
            case 3 -> p03BooksByPrice();
            case 4 -> p04NotReleasedBooks();
            case 5 -> p05BooksReleasedBeforeDate();
            case 6 -> p06AuthorsSearch();
            case 7 -> p07BooksSearch();
            case 8 -> p08BookTitlesSearch();
            case 9 -> p09CountBooks();
            case 10 -> p10TotalBookCopies();
            case 11 -> p11ReducedBook();
            case 12 -> p12IncreaseBookCopies();
            case 13 -> p13RemoveBooks();
            case 14 -> p14StoredProcedure();
        }
    }

    private void p14StoredProcedure() throws IOException {
        System.out.println("Enter author name: ");
        String fullName = reader.readLine();
        String f_name = fullName.split("\\s+")[0];
        String l_name = fullName.split("\\s+")[1];
        System.out.printf("Books by author %s: %d\n",
                fullName, this.bookService
                        .findBookTitlesByAuthorNames(f_name, l_name));
    }

    private void p13RemoveBooks() throws IOException {
        System.out.println("Enter book copies limit");
        int copies = Integer.parseInt(reader.readLine());
        System.out.println("Removed books: " + this.bookService.removeAllByCopiesLessThan(copies));
    }

    private void p12IncreaseBookCopies() throws IOException {
        System.out.println("Enter date and increase number: ");
        LocalDate date = LocalDate.parse(reader.readLine(), DateTimeFormatter.ofPattern("dd MMM yyyy"));
        int increaseNumber = Integer.parseInt(reader.readLine());
        System.out.println("Total number of books added: "
                + increaseNumber * this.bookService
                .updateBookCopiesReleasedAfterGivenDateWith(date, increaseNumber));
    }

    private void p11ReducedBook() throws IOException {
        System.out.println("Enter book title");
        String title = reader.readLine();
        this.bookService.findAllByTitle(title)
                .forEach(System.out::println);
    }

    private void p10TotalBookCopies() throws IOException {
        System.out.println("Enter author name: ");
        String fullName = reader.readLine();
        String firstName = fullName.split(" ")[0];
        String lastName = fullName.split(" ")[1];

        this.authorService.findAllAuthorsAndTheirTotalCopies(firstName, lastName)
                .forEach(System.out::println);
    }
//    private void p10TotalBookCopies() throws IOException {
//        System.out.println("Enter author name: ");
//        String fullName = reader.readLine();
//        String firstName = fullName.split(" ")[0];
//        String lastName = fullName.split(" ")[1];
//        System.out.printf("%s %s - %d\n",
//                firstName, lastName, this.bookService.countBookCopiesByAuthor(firstName, lastName));
//    }

    private void p09CountBooks() throws IOException {
        System.out.println("Enter title length: ");
        int length = Integer.parseInt(reader.readLine());
        System.out.printf("There are %d books with longer title than %d symbols\n",
                this.bookService.findBooksCountWithTitleLongerThan(length), length);
    }

    private void p08BookTitlesSearch() throws IOException {
        System.out.println("Enter author's first name start: ");
        String pattern = reader.readLine();
        this.bookService
                .findAllByAuthor_FirstNameStartingWith(pattern)
                .forEach(System.out::println);

    }

    //P08Option2
//    private void p08BookTitlesSearch() throws IOException {
//        System.out.println("Enter author's first name start: ");
//        String pattern = reader.readLine();
//        this.bookService
//                .findAllByTitleFromAuthorsWithFirstNameStartingWith(pattern)
//                .forEach(System.out::println);
//    }


    private void p07BooksSearch() throws IOException {
        System.out.println("Enter pattern: ");
        String pattern = reader.readLine();
        this.bookService
                .findAllByTitleContaining(pattern)
                .forEach(System.out::println);
    }

    private void p06AuthorsSearch() throws IOException {
        System.out.println("Enter author's first name end: ");
        String endsWith = reader.readLine();
        this.authorService
                .findAllByFirstNameEndingWith(endsWith)
                .forEach(System.out::println);

    }

    private void p05BooksReleasedBeforeDate() throws IOException {
        System.out.println("Enter release date in format dd-MM-yyyy: ");
        String date = reader.readLine();
        LocalDate releaseDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        this.bookService
                .findAllByReleaseDateBefore(releaseDate)
                .forEach(System.out::println);
    }

    private void p04NotReleasedBooks() throws IOException {
        System.out.println("Enter release year");
        int releaseYear = Integer.parseInt(reader.readLine());

        LocalDate start = LocalDate.of(releaseYear, 1, 1);
        LocalDate end = LocalDate.of(releaseYear, 12, 31);
        this.bookService
                .findAllByReleaseDateBeforeOrReleaseDateAfter(start, end)
                .forEach(System.out::println);
    }

    private void p03BooksByPrice() {
        BigDecimal lowerPrice = BigDecimal.valueOf(5);
        BigDecimal upperPrice = BigDecimal.valueOf(40);
        this.bookService
                .findAllByPriceLessThanOrPriceGreaterThan(lowerPrice, upperPrice)
                .forEach(System.out::println);
    }

    private void p02GoldenBooks() {
        EditionType editionType = EditionType.GOLD;
        int copies = 5000;
        this.bookService
                .findAllByEditionTypeAndCopiesLessThan(editionType, copies)
                .forEach(System.out::println);
    }

    private void p01BookTitlesByAgeRestriction() throws IOException {
        System.out.println("Enter age restriction: ");
        AgeRestriction ageRestriction = AgeRestriction.valueOf(reader.readLine().toUpperCase());
        this.bookService
                .findAllByAgeRestriction(ageRestriction)
                .forEach(System.out::println);
    }


    private void seedData() throws IOException {
        categoryService.seedCategories();
        authorService.seedAuthors();
        bookService.seedBooks();
    }
}
