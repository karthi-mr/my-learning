package com.learn.graphql.book;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final List<Book> books = Arrays.asList(
            new Book(1, "Atomic Habits", 200, 2),
            new Book(2, "5AM Club", 400, 1),
            new Book(3, "Java: The complete guide", 700, 2),
            new Book(4, "Spring Boot masters", 500, 3),
            new Book(5, "React complete course", 800, 1)
    );

    public List<Book> getAllBooks() {
        return this.books;
    }

    public Optional<Book> getBookById(Integer id) {
        return books.stream()
                .filter(book -> book.id().equals(id))
                .findFirst();
    }
}
