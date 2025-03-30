package ch.dboeckli.guru.jpa.jdbc.dao;

import ch.dboeckli.guru.jpa.jdbc.domain.Author;

public interface AuthorDao {

    Author getById(Long id);

    Author findAuthorByName(String firstName, String lastName);

    Author createAuthor(Author author);

    Author updateAuthor(Author author);

    void deleteAuthorById(Long id);

}
