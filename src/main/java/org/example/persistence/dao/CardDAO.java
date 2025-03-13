package org.example.persistence.dao;

import lombok.AllArgsConstructor;
import org.example.dto.CardDetailsDTO;
import org.example.persistence.entity.Card;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static java.util.Objects.nonNull;
import static org.example.persistence.converter.OffsetDateTimeConverter.toOffsetDateTime;

@AllArgsConstructor
public class CardDAO {

    private Connection connection;

    public Card insert(final Card entity) throws SQLException {
        var sql = "INSERT INTO CARDS (title, description, board_column_id) values (?, ?, ?) RETURNING id;";
        try (var statement = connection.prepareStatement(sql)) {
            var i = 1;
            statement.setString(i++, entity.getTitle());
            statement.setString(i++, entity.getDescription());
            statement.setLong(i, entity.getBoardColumn().getId());

            try (var rs = statement.executeQuery()) {
                if (rs.next()) {
                    entity.setId(rs.getLong(1));
                }
            }
        }
        return entity;
    }

    public void moveToColumn(final Long columnId, final Long cardId) throws SQLException {
        var sql = "UPDATE CARDS SET board_column_id = ? WHERE id = ?;";
        try (var statement = connection.prepareStatement(sql)) {
            var i = 1;
            statement.setLong(i++, columnId);
            statement.setLong(i, cardId);
            statement.executeUpdate();
        }
    }

    public Optional<CardDetailsDTO> findById(final Long id) throws SQLException {
        var sql =
                """
                       
                        SELECT
                                 c.id,
                                 c.title,
                                 c.description,
                                 b.blocked_at,
                                 b.block_reason,
                                 c.board_column_id,
                                 bc.name AS board_column_name, 
                                 (SELECT COUNT(sub_b.id) FROM BLOCKS sub_b WHERE sub_b.card_id = c.id) AS blocks_amount
                             FROM CARDS c
                             LEFT JOIN BLOCKS b ON c.id = b.card_id\s
                             INNER JOIN BOARDS_COLUMNS bc ON bc.id = c.board_column_id
                             WHERE c.id = ?\s
                             AND (b.unblocked_at IS NULL OR b.unblocked_at IS NULL);
                        """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeQuery();
            var resultSet = statement.getResultSet();
            if (resultSet.next()) {
                var dto = new CardDetailsDTO(
                        resultSet.getLong("id"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        nonNull(resultSet.getString("block_reason")),
                        toOffsetDateTime(resultSet.getTimestamp("blocked_at")),
                        resultSet.getString("block_reason"),
                        resultSet.getInt("blocks_amount"),
                        resultSet.getLong("board_column_id"),
                        resultSet.getString("board_column_name")
                );
                return Optional.of(dto);
            }
        }
        return Optional.empty();
    }
}