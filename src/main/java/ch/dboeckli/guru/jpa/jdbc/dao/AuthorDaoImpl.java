package ch.dboeckli.guru.jpa.jdbc.dao;

import ch.dboeckli.guru.jpa.jdbc.domain.Author;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

import static ch.dboeckli.guru.jpa.jdbc.dao.ConnectionHandler.closeConnection;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthorDaoImpl implements AuthorDao {

    private final DataSource dataSource;

    @Override
    public Author getById(Long id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("SELECT * FROM author where id = ?");
            statement.setLong(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return getAuthorFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            log.error("Error while retrieving author by ID: {}", e.getMessage(), e);
            return null;
        } finally {
            closeConnection(resultSet, statement, connection);
        }
        return null;
    }

    @Override
    public Author findAuthorByName(String firstName, String lastName) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("SELECT * FROM author where first_name = ? and last_name = ?");
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return getAuthorFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            log.error("Error while retrieving author by Name: {}", e.getMessage(), e);
            return null;
        } finally {
            closeConnection(resultSet, statement, connection);
        }
        return null;
    }

    @Override
    public Author createAuthor(Author author) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("INSERT INTO author (first_name, last_name) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, author.getFirstName());
            statement.setString(2, author.getLastName());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating author failed, no rows affected.");
            }

            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                Long generatedId = resultSet.getLong(1);
                return this.getById(generatedId);
            } else {
                throw new SQLException("Creating author failed, no ID obtained.");
            }
        } catch (SQLException e) {
            log.error("Error while creating author: {}", e.getMessage(), e);
            return null;
        } finally {
            closeConnection(resultSet, statement, connection);
        }
    }

    @Override
    public Author updateAuthor(Author author) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("UPDATE author set first_name = ?, last_name = ? where author.id = ?");
            statement.setString(1, author.getFirstName());
            statement.setString(2, author.getLastName());
            statement.setLong(3, author.getId());
            statement.execute();
        } catch (SQLException e) {
            log.error("Error while updating author: {}", e.getMessage(), e);
        } finally {
            closeConnection(null, statement, connection);
        }
        return this.getById(author.getId());
    }

    @Override
    public void deleteAuthorById(Long id) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("DELETE from author where id = ?");
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException ex) {
            log.error("Error while deleting author: {}", ex.getMessage(), ex);
        } finally {
            closeConnection(null, statement, connection);
        }
    }

    private Author getAuthorFromResultSet(ResultSet resultSet) throws SQLException {
        Author author = new Author();
        author.setId(resultSet.getLong("id"));
        author.setFirstName(resultSet.getString("first_name"));
        author.setLastName(resultSet.getString("last_name"));
        return author;
    }
}
