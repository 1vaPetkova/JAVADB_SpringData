package com.example.java_db_06_exercise.service.interfaces;

import com.example.java_db_06_exercise.model.entity.Author;

import java.io.IOException;
import java.util.List;

public interface AuthorService{
   void seedAuthors() throws IOException;

    Author getRandomAuthor();


    List<Author> getAllAuthorsByCountOfBooksDesc();
}
