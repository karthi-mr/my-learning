package com.learn.graphql.author;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    private final List<Author> authors = Arrays.asList(
            new Author(1, "John Doe"),
            new Author(2, "Kevin Peterson"),
            new Author(3, "Kiran Anwar")
    );

    public List<Author> getAllAuthors() {
        return authors;
    }

    public Optional<Author> getAuthorById(Integer id) {
        return this.authors.stream()
                .filter(author -> author.id().equals(id))
                .findFirst();
    }
}
