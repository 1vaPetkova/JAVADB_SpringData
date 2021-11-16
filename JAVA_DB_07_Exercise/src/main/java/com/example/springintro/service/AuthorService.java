package com.example.springintro.service;

import com.example.springintro.model.entity.Author;

import java.io.IOException;
import java.util.List;

public interface AuthorService {
    void seedAuthors() throws IOException;

    Author getRandomAuthor();

    //P06
    List<String> findAllByFirstNameEndingWith(String end);

    //P10
    List<String> findAllAuthorsAndTheirTotalCopies(String firstName, String lastName);
}
