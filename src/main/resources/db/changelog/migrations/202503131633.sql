-- Criar tabela BOARDS
CREATE TABLE BOARDS (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL
);

-- Criar tabela BOARDS_COLUMNS
CREATE TABLE BOARDS_COLUMNS (
                                id BIGSERIAL PRIMARY KEY,
                                name VARCHAR(255) NOT NULL,
                                "order" INT NOT NULL,
                                kind VARCHAR(7) NOT NULL,
                                board_id BIGINT NOT NULL,
                                CONSTRAINT boards__boards_columns_fk FOREIGN KEY (board_id) REFERENCES BOARDS(id) ON DELETE CASCADE,
                                CONSTRAINT unique_board_id_order UNIQUE (board_id, "order")
);

-- Criar tabela CARDS
CREATE TABLE CARDS (
                       id BIGSERIAL PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       description VARCHAR(255) NOT NULL,
                       board_column_id BIGINT NOT NULL,
                       CONSTRAINT boards_columns__cards_fk FOREIGN KEY (board_column_id) REFERENCES BOARDS_COLUMNS(id) ON DELETE CASCADE
);

-- Criar tabela BLOCKS
CREATE TABLE BLOCKS (
                        id BIGSERIAL PRIMARY KEY,
                        blocked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        block_reason VARCHAR(255) NOT NULL,
                        unblocked_at TIMESTAMP NULL,
                        unblock_reason VARCHAR(255) NULL,
                        card_id BIGINT NOT NULL,
                        CONSTRAINT cards__blocks_fk FOREIGN KEY (card_id) REFERENCES CARDS(id) ON DELETE CASCADE
);
