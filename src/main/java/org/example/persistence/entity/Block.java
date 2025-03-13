package org.example.persistence.entity;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class Block {
    private Long id;
    private OffsetDateTime blockedAt;
    private String blockReason;
    private OffsetDateTime unblockedAt;
    private String unblockReason;
}
