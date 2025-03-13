package org.example.service;

import lombok.AllArgsConstructor;
import org.example.persistence.dao.BoardColumnDAO;
import org.example.persistence.entity.BoardColumn;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class BoardColumnQueryService {
    private final Connection connection;

    public Optional<BoardColumn> findById(final Long id) throws SQLException {
        var dao = new BoardColumnDAO(connection);
        return dao.findById(id);
    }
}
