package com.example.springintro.repository;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;
import com.example.springintro.model.entity.EditionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    //P01
    List<Book> findAllByAgeRestriction(AgeRestriction ageRestriction);

    //P02
    List<Book> findAllByEditionTypeAndCopiesLessThan(EditionType editionType, Integer copies);

    //P03
    List<Book> findAllByPriceLessThanOrPriceGreaterThan(BigDecimal lower, BigDecimal upper);

    //P04
    List<Book> findAllByReleaseDateBeforeOrReleaseDateAfter(LocalDate start, LocalDate end);

    //P05
    List<Book> findAllByReleaseDateBefore(LocalDate releaseDate);

    //P07
    @Query("SELECT b.title FROM Book b WHERE LOWER(b.title) LIKE concat('%', LOWER(:pattern),'%')")
    List<String> findAllByTitleContaining(String pattern);

    //P08
    // @Query("SELECT b.title FROM Book b WHERE b.author.firstName LIKE CONCAT(:pattern,'%')")
    // List<String> findAllByTitleFromAuthorsWithFirstNameStartingWith(String pattern);

    List<Book> findAllByAuthor_FirstNameStartingWith(String pattern);

    //P09
    @Query("SELECT COUNT(b) FROM Book b WHERE LENGTH(b.title) > :length")
    int findBooksCountWithTitleLongerThan(int length);

    //P10
    @Query("SELECT SUM(b.copies) FROM Book b WHERE b.author.firstName = :firstName AND b.author.lastName = :lastName")
    int countBookCopiesByAuthor(String firstName, String lastName);

    //P11
    List<Book> findAllByTitle(String title);

    //P12
    @Query("UPDATE Book b SET b.copies = b.copies+:increaseNumber WHERE b.releaseDate > :date")
    @Modifying
    int updateBookCopiesReleasedAfterGivenDateWith(LocalDate date, int increaseNumber);

    //P13
    @Modifying
    int removeAllByCopiesLessThan(Integer copies);

    //P14
    @Query(value = "CALL cdf_amount_of_books_by_author(:f_name,:l_name);", nativeQuery = true)
    int findBookTitlesByAuthorFullName(String f_name, String l_name);
}
