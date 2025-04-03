package ch.dboeckli.guru.jpa.jdbctemplate.dao.mysql;

import ch.dboeckli.guru.jpa.jdbctemplate.dao.AuthorDao;
import ch.dboeckli.guru.jpa.jdbctemplate.dao.AuthorDaoImpl;
import ch.dboeckli.guru.jpa.jdbctemplate.domain.Author;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test_mysql")
@Import(AuthorDaoImpl.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
class AuthorDaoImplIT {

    @Autowired
    AuthorDao authorDao;

    @Test
    void testDeleteAuthor() {
        Author author = new Author();
        author.setFirstName("john");
        author.setLastName("t");

        Author saved = authorDao.saveNewAuthor(author);

        authorDao.deleteAuthorById(saved.getId());

        TransientDataAccessResourceException ex = assertThrows(
            TransientDataAccessResourceException.class, () -> authorDao.getById(saved.getId())
        );
        log.info("### Exception message: " + ex.getMessage());
        assertThat(ex.getMessage()).contains("Illegal operation on empty result set");
    }

    @Test
    void testUpdateAuthor() {
        Author author = new Author();
        author.setFirstName("john");
        author.setLastName("t");

        Author saved = authorDao.saveNewAuthor(author);

        saved.setLastName("Thompson");
        Author updated = authorDao.updateAuthor(saved);

        assertThat(updated.getLastName()).isEqualTo("Thompson");
    }

    @Test
    void testInsertAuthor() {
        Author author = new Author();
        author.setFirstName("john");
        author.setLastName("t");

        Author saved = authorDao.saveNewAuthor(author);

        assertThat(saved).isNotNull();
    }

    @Test
    void testGetAuthorByName() {
        Author author = authorDao.findAuthorByName("Craig", "Walls");

        assertThat(author).isNotNull();
    }

    @Test
    void testGetAuthorWithSixBooks() {
        Author author = authorDao.getById(1L);

        assertAll(
            () -> assertThat(author.getId()).isNotNull(),
            () -> assertThat(author.getBooks()).hasSize(6)
        );
    }

    @Test
    void testGetAuthorWithThreeBooks() {
        Author author = authorDao.getById(2L);

        assertAll(
            () -> assertThat(author.getId()).isNotNull(),
            () -> assertThat(author.getBooks()).hasSize(3)
        );
    }

    @Test
    void testFindAllAuthors() {
        List<Author> authors = authorDao.findAllAuthors();
        log.info("### Found Authors count: {}, with: {}", authors.size(), authors);

        AssertionsForClassTypes.assertThat(authors.size()).isGreaterThan(0);
    }

    @Test
    void findAllAuthorsByLastName() {
        List<Author> authors = authorDao.findAllAuthorsByLastName("Smith", PageRequest.of(0, 10));

        assertAll(
            () -> assertThat(authors).isNotNull(),
            () -> assertThat(authors).hasSize(10)
        );
    }

    @Test
    void findAllAuthorsByLastNameSortLastNameDesc() {
        List<Author> authors = authorDao.findAllAuthorsByLastName("Smith",
            PageRequest.of(0, 10, Sort.by(Sort.Order.desc("firstname"))));

        assertAll(
            () -> assertThat(authors).isNotNull(),
            () -> assertThat(authors).hasSize(10),
            () -> {
                assert authors != null;
                assertThat(authors.getFirst().getFirstName()).isEqualTo("Yugal");
            }
        );
    }

    @Test
    void findAllAuthorsByLastNameSortLastNameAsc() {
        List<Author> authors = authorDao.findAllAuthorsByLastName("Smith",
            PageRequest.of(0, 10, Sort.by(Sort.Order.asc("firstname"))));

        assertAll(
            () -> assertThat(authors).isNotNull(),
            () -> assertThat(authors).hasSize(10),
            () -> {
                assert authors != null;
                assertThat(authors.getFirst().getFirstName()).isEqualTo("Ahmed");
            }
        );
    }

    @Test
    void findAllAuthorsByLastNameAllRecs() {
        List<Author> authors = authorDao.findAllAuthorsByLastName("Smith", PageRequest.of(0, 100));

        assertAll(
            () -> assertThat(authors).isNotNull(),
            () -> assertThat(authors).hasSize(40)
        );
    }

    @Test
    void testGetAuthorWithoutBooks() {
        Author author = new Author();
        author.setFirstName("john");
        author.setLastName("belushi");

        Author saved = authorDao.saveNewAuthor(author);

        Author authorWithoutBooks = authorDao.getById(saved.getId());

        assertAll(
            () -> assertThat(authorWithoutBooks.getId()).isNotNull(),
            () -> assertNull(authorWithoutBooks.getBooks())
        );
    }
}