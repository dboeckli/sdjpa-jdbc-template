package ch.dboeckli.guru.jpa.jdbc.dao.h2;

import ch.dboeckli.guru.jpa.jdbc.dao.AuthorDao;
import ch.dboeckli.guru.jpa.jdbc.dao.AuthorDaoImpl;
import ch.dboeckli.guru.jpa.jdbc.domain.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@Import({ AuthorDaoImpl.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class AuthorDaoImplTest {

    @Autowired
    AuthorDao authorDao;

    @Test
    void testGetAuthor() {
        Author author = new Author();
        author.setFirstName("JohnX");
        author.setLastName("DoeX");

        Author createdAuthor = authorDao.createAuthor(author);
        assertThat(createdAuthor).isNotNull();

        Author authorCreated = authorDao.getById(createdAuthor.getId());
        assertThat(authorCreated).isNotNull();
    }

    @Test
    void testCreateAndGetAuthor() {
        Author author = new Author();
        author.setFirstName("John");
        author.setLastName("Doe");

        Author createdAuthor = authorDao.createAuthor(author);
        assertThat(createdAuthor).isNotNull();

        Author foundAuthor = authorDao.getById(createdAuthor.getId());
        assertThat(foundAuthor).isNotNull();
    }

    @Test
    void findAuthorByName() {
        Author author = new Author();
        author.setFirstName("Jim");
        author.setLastName("Digger");

        Author createdAuthor = authorDao.createAuthor(author);
        assertThat(createdAuthor).isNotNull();

        Author foundAuthor = authorDao.findAuthorByName("Jim", "Digger");
        assertThat(foundAuthor).isNotNull();
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