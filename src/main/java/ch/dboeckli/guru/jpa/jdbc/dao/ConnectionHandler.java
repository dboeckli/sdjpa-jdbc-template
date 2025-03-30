package ch.dboeckli.guru.jpa.jdbc.dao;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@UtilityClass
@Slf4j
public class ConnectionHandler {

    public static void closeConnection(ResultSet resultSet, PreparedStatement statement, Connection connection) {
        try{
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        } catch (SQLException ex){
            log.error("Error closing database resources: {}", ex.getMessage(), ex);
        }
    }

}
