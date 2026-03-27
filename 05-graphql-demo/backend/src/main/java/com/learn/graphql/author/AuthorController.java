package com.learn.graphql.author;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @QueryMapping(name = "authors")
    public List<Author> getAllAuthors() {
        return this.authorService.getAllAuthors();
    }

    @QueryMapping
    public Optional<Author> authorById(@Argument Integer id) {
        return this.authorService.getAuthorById(id);
    }
}
