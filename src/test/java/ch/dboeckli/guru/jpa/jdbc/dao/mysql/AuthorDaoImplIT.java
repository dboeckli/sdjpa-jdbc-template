package ch.dboeckli.guru.jpa.jdbc.dao.mysql;

import ch.dboeckli.guru.jpa.jdbc.dao.AuthorDao;
import ch.dboeckli.guru.jpa.jdbc.dao.AuthorDaoImpl;
import ch.dboeckli.guru.jpa.jdbc.domain.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test_mysql")
@Import(AuthorDaoImpl.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthorDaoImplIT {

    @Autowired
    AuthorDao authorDao;

    @Test
    void testGetAuthor() {
        Author author = authorDao.getById(1L);
        assertThat(author).isNotNull();
    }

    @Test
    void findAuthorByName() {
        Author foundAuthor = authorDao.findAuthorByName("Craig", "Walls");
        assertThat(foundAuthor).isNotNull();
    }

    @Test
    void testCreateAuthor() {
        Author author = new Author();
        author.setFirstName("Hansjakob");
        author.setLastName("Studer");

        Author saved = authorDao.createAuthor(author);

        assertThat(saved).isNotNull();
    }

    @Test
    void testUpdateAuthor() {
        Author author = new Author();
        author.setFirstName("Meister");
        author.setLastName("Eder");

        Author saved = authorDao.createAuthor(author);

        saved.setLastName("Thompson");
        Author updated = authorDao.updateAuthor(saved);

        assertThat(updated.getFirstName()).isEqualTo("Meister");
        assertThat(updated.getLastName()).isEqualTo("Thompson");
    }

    @Test
    void deleteAuthorById() {
        Author authorToDelete = new Author();
        authorToDelete.setFirstName("The");
        authorToDelete.setLastName("Ghost");

        Author saved = authorDao.createAuthor(authorToDelete);
        assertThat(saved).isNotNull();

        authorDao.deleteAuthorById(saved.getId());

        Author deletedAuthor = authorDao.getById(saved.getId());
        assertThat(deletedAuthor).isNull();
    }
}