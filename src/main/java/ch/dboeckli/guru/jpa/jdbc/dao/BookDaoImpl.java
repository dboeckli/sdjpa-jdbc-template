package ch.dboeckli.guru.jpa.jdbc.dao;

import ch.dboeckli.guru.jpa.jdbc.domain.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

import static ch.dboeckli.guru.jpa.jdbc.dao.ConnectionHandler.closeConnection;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookDaoImpl implements BookDao {

    private final DataSource dataSource;

    private final AuthorDao authorDao;

    @Override
    public Book getById(Long id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("SELECT * FROM book where id = ?");
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getBookFromRS(resultSet);
            }
        } catch (SQLException e) {
            log.error("Error while retrieving book by ID: {}", e.getMessage(), e);
            return null;
        } finally {
            closeConnection(resultSet, statement, connection);
        }
        return null;
    }

    @Override
    public Book findBookByTitle(String title) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("SELECT * FROM book where title = ?");
            statement.setString(1, title);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return getBookFromRS(resultSet);
            }
        } catch (SQLException e) {
            log.error("Error while retrieving book by Title: {}", e.getMessage(), e);
            return null;
        } finally {
            closeConnection(resultSet, statement, connection);
        }
        return null;
    }

    @Override
    public Book saveNewBook(Book book) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("INSERT INTO book (isbn, publisher, title, author_id) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, book.getIsbn());
            statement.setString(2, book.getPublisher());
            statement.setString(3, book.getTitle());
            if (book.getAuthorId()!= null) {
                statement.setLong(4, book.getAuthorId().getId());
            } else {
                statement.setNull(4, Types.BIGINT);
            }
            statement.execute();

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating book failed, no rows affected.");
            }

            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                Long generatedId = resultSet.getLong(1);
                return this.getById(generatedId);
            } else {
                throw new SQLException("Creating book failed, no ID obtained.");
            }
        } catch (SQLException e) {
            log.error("Error while creating book: {}", e.getMessage(), e);
            return null;
        } finally {
            closeConnection(resultSet, statement, connection);
        }
    }

    @Override
    public Book updateBook(Book book) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("UPDATE book set isbn = ?, publisher = ?, title = ?, author_id = ? where id = ?");
            statement.setString(1, book.getIsbn());
            statement.setString(2, book.getPublisher());
            statement.setString(3, book.getTitle());
            if (book.getAuthorId()!= null) {
                statement.setLong(4, book.getAuthorId().getId());
            } else {
                statement.setNull(4, Types.BIGINT);
            }
            statement.setLong(5, book.getId());
            statement.execute();
        } catch (SQLException e) {
            log.error("Error while updating book: {}", e.getMessage(), e);
            return null;
        } finally {
            closeConnection(resultSet, statement, connection);
        }
        return getById(book.getId());
    }

    @Override
    public void deleteBookById(Long id) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("DELETE from book where id = ?");
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException ex) {
            log.error("Error while deleting book: {}", ex.getMessage(), ex);
        } finally {
            closeConnection(null, statement, connection);
        }
    }

    private Book getBookFromRS(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getLong("id"));
        book.setIsbn(resultSet.getString("isbn"));
        book.setPublisher(resultSet.getString("publisher"));
        book.setTitle(resultSet.getString("title"));
        book.setAuthorId(authorDao.getById(resultSet.getLong("author_id")));
        return book;
    }
}
