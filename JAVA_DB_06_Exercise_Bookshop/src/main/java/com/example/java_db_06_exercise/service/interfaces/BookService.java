package com.example.java_db_06_exercise.service.interfaces;

import com.example.java_db_06_exercise.model.entity.Book;

import java.io.IOException;
import java.util.List;

public interface BookService {

    void seedBooks() throws IOException;

    List<Book> findAllBooksAfterYear(int year);
    List<Book> findAllBooksBeforeYear(int year);

    List<Book> findAllBooksByAuthorFirstAndLastNameOrderByReleaseDateAndTitle(String firstName,String lastName);
}
