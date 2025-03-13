package org.example.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoardColumn {

    private Long id;
    private String name;
    private int order;
    private BoardColumnKindEnum kind;
    private Board board = new Board();
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Card> cards = new ArrayList<>();
}
