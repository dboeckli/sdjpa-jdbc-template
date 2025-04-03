package ch.dboeckli.guru.jpa.jdbctemplate.dao;

import ch.dboeckli.guru.jpa.jdbctemplate.domain.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthorDaoImpl implements AuthorDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Author getById(Long id) {
        String sql = "select author.id as id, " +
            "first_name, last_name, book.id as book_id, book.isbn, book.publisher, book.title from author\n" +
            "left outer join book on author.id = book.author_id where author.id = ?";

        return jdbcTemplate.query(sql, new AuthorExtractor(), id);
    }

    @Override
    public Author findAuthorByName(String firstName, String lastName) {
        return jdbcTemplate.queryForObject("SELECT * FROM author WHERE first_name = ? and last_name = ?",
            getRowMapper(),
            firstName, lastName);
    }

    @Override
    public List<Author> findAllAuthors() {
        return jdbcTemplate.query("SELECT * FROM author", getRowMapper());
    }

    @Override
    public List<Author> findAllAuthorsByLastName(String lastname, Pageable pageable) {
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT * FROM author WHERE last_name = ? ");

        if (pageable.getSort().getOrderFor("firstname") != null) {
            sb.append("order by first_name ").append(pageable.getSort()
                .getOrderFor("firstname").getDirection().name());
        }

        sb.append(" limit ? offset ?");

        return jdbcTemplate.query(sb.toString(), getRowMapper(), lastname, pageable.getPageSize(), pageable.getOffset());
    }

    @Override
    public Author saveNewAuthor(Author author) {
        jdbcTemplate.update("INSERT INTO author (first_name, last_name) VALUES (?, ?)",
            author.getFirstName(), author.getLastName());

        Long createdId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

        return this.getById(createdId);
    }

    @Override
    public Author updateAuthor(Author author) {
        jdbcTemplate.update("UPDATE author SET first_name = ?, last_name = ? WHERE id = ?",
            author.getFirstName(), author.getLastName(), author.getId());

        return this.getById(author.getId());
    }

    @Override
    public void deleteAuthorById(Long id) {
        jdbcTemplate.update("DELETE FROM author WHERE id = ?", id);
    }

    private RowMapper<Author> getRowMapper(){
        return new AuthorMapper();
    }
}
