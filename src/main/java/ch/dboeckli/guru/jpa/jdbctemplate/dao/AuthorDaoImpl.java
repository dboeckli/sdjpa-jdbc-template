package ch.dboeckli.guru.jpa.jdbctemplate.dao;

import ch.dboeckli.guru.jpa.jdbctemplate.domain.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorDaoImpl implements AuthorDao {
    @Override
    public Author getById(Long id) {
        return null;
    }

    @Override
    public Author findAuthorByName(String firstName, String lastName) {
        return null;
    }

    @Override
    public Author saveNewAuthor(Author author) {
        return null;
    }

    @Override
    public Author updateAuthor(Author author) {
        return null;
    }

    @Override
    public void deleteAuthorById(Long id) {

    }
}
