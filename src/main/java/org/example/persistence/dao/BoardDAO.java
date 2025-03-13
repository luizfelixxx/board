package org.example.persistence.dao;

import lombok.AllArgsConstructor;
import org.example.persistence.entity.Board;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class BoardDAO {

    private Connection connection;

    public Board insert(Board entity) throws SQLException {
        var sql = "INSERT INTO BOARDS (name) values (?) RETURNING id;";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getName());

            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    entity.setId(resultSet.getLong(1));
                }
            }
        }
        return entity;
    }

    public void delete(final Long id) throws SQLException {
        var sql = "DELETE FROM BOARDS WHERE id = ?;";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    public Optional<Board> findById(final Long id) throws SQLException {
        var sql = "SELECT id, name FROM BOARDS WHERE id = ?;";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeQuery();
            var resultSet = statement.getResultSet();
            if (resultSet.next()) {
                var entity = new Board();
                entity.setId(resultSet.getLong("id"));
                entity.setName(resultSet.getString("name"));
                return Optional.of(entity);
            }
            return Optional.empty();
        }
    }

    public boolean exists(final Long id) throws SQLException {
        var sql = "SELECT 1 FROM BOARDS WHERE id = ?;";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeQuery();
            return statement.getResultSet().next();
        }
    }
}