package org.example.service;

import lombok.AllArgsConstructor;
import org.example.persistence.dao.BoardColumnDAO;
import org.example.persistence.dao.BoardDAO;
import org.example.persistence.entity.Board;

import java.sql.Connection;
import java.sql.SQLException;

@AllArgsConstructor
public class BoardService {

    private final Connection connection;

    public Board insert(final Board entity) throws SQLException {
        var dao = new BoardDAO(connection);
        var boardColumnDAO = new BoardColumnDAO(connection);
        try{
            dao.insert(entity);
            var columns = entity.getBoardColumns().stream().peek(c -> c.setBoard(entity)).toList();
            for (var column :  columns){
                boardColumnDAO.insert(column);
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
        return entity;
    }

    public boolean delete(final Long id) throws SQLException {
        var dao = new BoardDAO(connection);
        try{
            if (!dao.exists(id)) {
                return false;
            }
            dao.delete(id);
            connection.commit();
            return true;
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

}