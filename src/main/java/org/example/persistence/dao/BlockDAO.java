package org.example.persistence.dao;

import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.OffsetDateTime;

import static org.example.persistence.converter.OffsetDateTimeConverter.toTimestamp;

@AllArgsConstructor
public class BlockDAO {

    private final Connection connection;

    public void block(final String reason, final Long cardId) throws SQLException {
        executeBlockUpdate(
                "INSERT INTO BLOCKS (blocked_at, block_reason, card_id) VALUES (?, ?, ?);",
                reason,
                cardId
        );
    }

    public void unblock(final String reason, final Long cardId) throws SQLException {
        executeBlockUpdate(
                "UPDATE BLOCKS SET unblocked_at = ?, unblock_reason = ? WHERE card_id = ? AND unblock_reason IS NULL;",
                reason,
                cardId
        );
    }

    private void executeBlockUpdate(final String sql, final String reason, final Long cardId) throws SQLException {
        try (var statement = connection.prepareStatement(sql)) {
            var i = 1;
            statement.setTimestamp(i++, toTimestamp(OffsetDateTime.now()));
            statement.setString(i++, reason);
            statement.setLong(i, cardId);
            statement.executeUpdate();
        }
    }
}