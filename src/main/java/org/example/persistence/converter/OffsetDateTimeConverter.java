package org.example.persistence.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.OffsetDateTime;

import static java.time.ZoneOffset.UTC;
import static java.util.Objects.nonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OffsetDateTimeConverter {

    public static OffsetDateTime toOffsetDateTime(Timestamp timestamp) {
        return nonNull(timestamp) ? OffsetDateTime.ofInstant(timestamp.toInstant(), UTC) : null;
    }

    public static java.sql.Timestamp toTimestamp(final OffsetDateTime value) {
        return nonNull(value) ? Timestamp.valueOf(value.atZoneSameInstant(UTC).toLocalDateTime()) : null;
    }
}
