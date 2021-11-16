package com.example.java_db_06_exercise.service.impl;

import com.example.java_db_06_exercise.model.entity.Author;
import com.example.java_db_06_exercise.repository.AuthorRepository;
import com.example.java_db_06_exercise.service.interfaces.AuthorService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AuthorServiceImpl implements AuthorService {

    private static final String AUTHORS_FILE_PATH = "src/main/resources/files/authors.txt";

    private final AuthorRepository authorsRepository;

    public AuthorServiceImpl(AuthorRepository authorsRepository) {
        this.authorsRepository = authorsRepository;
    }

    @Override
    public void seedAuthors() throws IOException {
        if (authorsRepository.count() > 0) {
            return;
        }
        Files.readAllLines(Path.of(AUTHORS_FILE_PATH))
                .stream()
                .filter(l -> !l.isEmpty())
                .forEach(au -> {
                    String[] names = au.split("\\s+");
                    Author author;
                    if (names.length > 1) {
                        author = new Author(names[0], names[1]);
                    } else {
                        author = new Author(names[0]);
                    }
                    this.authorsRepository.saveAndFlush(author);
                });
    }

    @Override
    public Author getRandomAuthor() {
        long randomId = ThreadLocalRandom.current().nextLong(1, this.authorsRepository.count() + 1);
        return this.authorsRepository.findById(randomId).orElse(null);
    }

    @Override
    public List<Author> getAllAuthorsByCountOfBooksDesc() {
        return this.authorsRepository.findAllByBooksSizeDESC();
    }

}

