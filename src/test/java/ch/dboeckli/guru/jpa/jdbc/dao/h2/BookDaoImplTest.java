package ch.dboeckli.guru.jpa.jdbc.dao.h2;

import ch.dboeckli.guru.jpa.jdbc.dao.AuthorDaoImpl;
import ch.dboeckli.guru.jpa.jdbc.dao.BookDao;
import ch.dboeckli.guru.jpa.jdbc.dao.BookDaoImpl;
import ch.dboeckli.guru.jpa.jdbc.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@Import({ BookDaoImpl.class, AuthorDaoImpl.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class BookDaoImplTest {

    @Autowired
    BookDao bookDao;

    @Test
    void getById() {
        Book book = new Book();
        book.setIsbn("1234");
        book.setPublisher("Self");
        book.setTitle("Guguseli Name");
        Book saved = bookDao.saveNewBook(book);

        Book retrievedBook = bookDao.getById(saved.getId());
        assertThat(retrievedBook.getId()).isNotNull();
    }

    @Test
    void findBookByTitle() {
        Book book = new Book();
        book.setIsbn("1234");
        book.setPublisher("Self");
        book.setTitle("Giguseli Name");
        bookDao.saveNewBook(book);

        Book retrievedBook = bookDao.findBookByTitle("Giguseli Name");
        assertThat(retrievedBook).isNotNull();
    }

    @Test
    void saveNewBook() {
        Book book = new Book();
        book.setIsbn("1234");
        book.setPublisher("Self");
        book.setTitle("my book");
        Book saved = bookDao.saveNewBook(book);

        assertThat(saved).isNotNull();
    }

    @Test
    void updateBook() {
        Book book = new Book();
        book.setIsbn("1234");
        book.setPublisher("Self");
        book.setTitle("my book");
        Book saved = bookDao.saveNewBook(book);

        saved.setTitle("New Book");
        bookDao.updateBook(saved);

        Book fetched = bookDao.getById(saved.getId());

        assertThat(fetched.getTitle()).isEqualTo("New Book");
    }

    @Test
    void deleteBookById() {
        Book book = new Book();
        book.setIsbn("1234");
        book.setPublisher("Self");
        book.setTitle("my book");
        Book saved = bookDao.saveNewBook(book);

        bookDao.deleteBookById(saved.getId());

        Book deleted = bookDao.getById(saved.getId());

        assertThat(deleted).isNull();
    }
}