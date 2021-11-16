package com.example.springintro.service;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;
import com.example.springintro.model.entity.EditionType;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface BookService {
    void seedBooks() throws IOException;

    //P01
    List<String> findAllByAgeRestriction(AgeRestriction ageRestriction);

    //P02
    List<String> findAllByEditionTypeAndCopiesLessThan(EditionType editionType, Integer copies);

    //P03
    List<String> findAllByPriceLessThanOrPriceGreaterThan(BigDecimal lower, BigDecimal upper);

    //P04
    List<String> findAllByReleaseDateBeforeOrReleaseDateAfter(LocalDate start, LocalDate end);

    //P05
    List<String> findAllByReleaseDateBefore(LocalDate releaseDate);

    //P07
    List<String> findAllByTitleContaining(String pattern);

    //P08
   // List<String> findAllByTitleFromAuthorsWithFirstNameStartingWith(String pattern);
    List<String> findAllByAuthor_FirstNameStartingWith(String pattern);

    //P09
    int findBooksCountWithTitleLongerThan(int length);

    //P10 - Option 2 (Option 1 - through Author Service)
   // int countBookCopiesByAuthor(String firstName, String lastName);

    //P11
    List<Book> findAllByTitle(String title);

    //P12
    int updateBookCopiesReleasedAfterGivenDateWith(LocalDate date, int increaseNumber);

    //P13
    int removeAllByCopiesLessThan(Integer copies);

    //P14
    int findBookTitlesByAuthorNames(String f_name, String l_name);
}
