package org.example.persistence.dao;

import lombok.RequiredArgsConstructor;
import org.example.dto.BoardColumnDTO;
import org.example.persistence.entity.BoardColumn;
import org.example.persistence.entity.Card;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static org.example.persistence.entity.BoardColumnKindEnum.findByName;

@RequiredArgsConstructor
public class BoardColumnDAO {

    private final Connection connection;

    public BoardColumn insert(final BoardColumn entity) throws SQLException {
        var sql = "INSERT INTO BOARDS_COLUMNS (name, \"order\", kind, board_id) VALUES (?, ?, ?, ?) RETURNING id;";
        try (var statement = connection.prepareStatement(sql)) {
            var i = 1;
            statement.setString(i++, entity.getName());
            statement.setInt(i++, entity.getOrder());
            statement.setString(i++, entity.getKind().name());
            statement.setLong(i, entity.getBoard().getId());

            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    entity.setId(resultSet.getLong(1));
                }
            }
            return entity;
        }
    }

    public List<BoardColumn> findByBoardId(final Long boardId) throws SQLException {
        List<BoardColumn> entities = new ArrayList<>();
        var sql = "SELECT id, name, \"order\", kind FROM BOARDS_COLUMNS WHERE board_id = ? ORDER BY \"order\"";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, boardId);
            statement.executeQuery();
            var resultSet = statement.getResultSet();
            while (resultSet.next()) {
                var entity = new BoardColumn();
                entity.setId(resultSet.getLong("id"));
                entity.setName(resultSet.getString("name"));
                entity.setOrder(resultSet.getInt("order"));
                entity.setKind(findByName(resultSet.getString("kind")));
                entities.add(entity);
            }
            return entities;
        }
    }

    public List<BoardColumnDTO> findByBoardIdWithDetails(final Long boardId) throws SQLException {
        List<BoardColumnDTO> dtos = new ArrayList<>();
        var sql =
                """
                        SELECT bc.id,
                               bc.name,
                               bc.kind,
                               (SELECT COUNT(c.id)
                                       FROM CARDS c
                                      WHERE c.board_column_id = bc.id) cards_amount
                          FROM BOARDS_COLUMNS bc
                         WHERE board_id = ?
                         ORDER BY \"order\";
                        """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, boardId);
            statement.executeQuery();
            var resultSet = statement.getResultSet();
            while (resultSet.next()) {
                var dto = new BoardColumnDTO(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        findByName(resultSet.getString("kind")),
                        resultSet.getInt("cards_amount")
                );
                dtos.add(dto);
            }
            return dtos;
        }
    }

    public Optional<BoardColumn> findById(final Long boardId) throws SQLException {
        var sql =
                """
                        SELECT bc.name,
                               bc.kind,
                               c.id,
                               c.title,
                               c.description
                          FROM BOARDS_COLUMNS bc
                          LEFT JOIN CARDS c
                            ON c.board_column_id = bc.id
                         WHERE bc.id = ?;
                        """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, boardId);
            statement.executeQuery();
            var resultSet = statement.getResultSet();
            if (resultSet.next()) {
                var entity = new BoardColumn();
                entity.setName(resultSet.getString("name"));
                entity.setKind(findByName(resultSet.getString("kind")));
                do {
                    var card = new Card();
                    if (isNull(resultSet.getString("title"))) {
                        break;
                    }
                    card.setId(resultSet.getLong("id"));
                    card.setTitle(resultSet.getString("title"));
                    card.setDescription(resultSet.getString("description"));
                    entity.getCards().add(card);
                } while (resultSet.next());
                return Optional.of(entity);
            }
            return Optional.empty();
        }
    }

}