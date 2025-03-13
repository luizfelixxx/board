package org.example.persistence.entity;

import lombok.Data;

@Data
public class Card {
    private Long id;
    private String title;
    private String description;
    private BoardColumn boardColumn = new BoardColumn();
}
