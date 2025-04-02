package ch.dboeckli.guru.jpa.jdbctemplate.dao;

import ch.dboeckli.guru.jpa.jdbctemplate.domain.Book;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookDao {
    Book getById(Long id);

    Book findBookByTitle(String title);

    Book saveNewBook(Book book);

    Book updateBook(Book book);

    void deleteBookById(Long id);

    List<Book> findAllBooks();

    List<Book> findAllBooks(int pageSize, int offset);

    List<Book> findAllBooks(Pageable pageable);
}
