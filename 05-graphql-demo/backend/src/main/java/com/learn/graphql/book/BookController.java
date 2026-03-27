package com.learn.graphql.book;

import com.learn.graphql.author.Author;
import com.learn.graphql.author.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;

    @QueryMapping(name = "books")
    public List<Book> getAllBooks() {
        return this.bookService.getAllBooks();
    }

    @QueryMapping
    public Optional<Book> bookById(@Argument Integer id) {
        return this.bookService.getBookById(id);
    }

    @SchemaMapping(field = "author")
    public Optional<Author> getAuthorById(Book book) {
        return this.authorService.getAuthorById(book.authorId());
    }
}
