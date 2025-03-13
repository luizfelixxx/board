package org.example.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static org.example.persistence.entity.BoardColumnKindEnum.CANCEL;
import static org.example.persistence.entity.BoardColumnKindEnum.INITIAL;


@Data
public class Board{

    private Long id;
    private String name;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<BoardColumn> boardColumns = new ArrayList<>();

    public BoardColumn getInitialColumn() {
        return getFilteredColumn(bc -> bc.getKind().equals(INITIAL));
    }

    public BoardColumn getCancelColumn() {
        return getFilteredColumn(bc -> bc.getKind().equals(CANCEL));
    }

    private BoardColumn getFilteredColumn(Predicate<BoardColumn> filter) {
        return boardColumns.stream()
                .filter(filter)
                .findFirst().orElseThrow();
    }
}