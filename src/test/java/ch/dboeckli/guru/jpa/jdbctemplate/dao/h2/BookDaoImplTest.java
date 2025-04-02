package ch.dboeckli.guru.jpa.jdbctemplate.dao.h2;

import ch.dboeckli.guru.jpa.jdbctemplate.dao.BookDao;
import ch.dboeckli.guru.jpa.jdbctemplate.dao.BookDaoImpl;
import ch.dboeckli.guru.jpa.jdbctemplate.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Import({ BookDaoImpl.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class BookDaoImplTest {

    @Autowired
    BookDao bookDao;

    @Test
    void testDeleteBook() {
        Book book = new Book();
        book.setIsbn("1234");
        book.setPublisher("Self");
        book.setTitle("my book");
        Book saved = bookDao.saveNewBook(book);

        bookDao.deleteBookById(saved.getId());

        assertThrows(EmptyResultDataAccessException.class, () -> bookDao.getById(saved.getId()));
    }

    @Test
    void updateBookTest() {
        Book book = new Book();
        book.setIsbn("1234");
        book.setPublisher("Self");
        book.setTitle("my book");
        book.setAuthorId(1L);
        Book saved = bookDao.saveNewBook(book);

        saved.setTitle("New Book");
        bookDao.updateBook(saved);

        Book fetched = bookDao.getById(saved.getId());

        assertThat(fetched.getTitle()).isEqualTo("New Book");
    }

    @Test
    void testSaveBook() {
        Book book = new Book();
        book.setIsbn("1234");
        book.setPublisher("Self");
        book.setTitle("my book");
        book.setAuthorId(1L);

        Book saved = bookDao.saveNewBook(book);

        assertThat(saved).isNotNull();
    }

    @Test
    void testGetBookByName() {
        Book book = bookDao.findBookByTitle("Clean Code");

        assertThat(book).isNotNull();
    }

    @Test
    void testGetBook() {
        Book book = bookDao.getById(3L);

        assertThat(book.getId()).isNotNull();
    }

    @Test
    void testFindAllBook() {
        List<Book> books = bookDao.findAllBooks();
        assertThat(books.size()).isGreaterThan(0);
    }

    @Test
    void testFindAllBookPage1() {
        List<Book> books = bookDao.findAllBooks(10, 0);
        assertThat(books.size()).isEqualTo(10);
    }

    @Test
    void testFindAllBookPage2() {
        List<Book> books = bookDao.findAllBooks(10, 10);
        assertThat(books.size()).isEqualTo(10);
    }

    @Test
    void testFindAllBookPage10() {
        List<Book> books = bookDao.findAllBooks(10, 100);
        assertThat(books.size()).isEqualTo(0);
    }
}