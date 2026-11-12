/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.crud.repository;

/**
 *
 * @author USER
 */


import com.crud.db.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractJdbcRepository {

    // Template Method for any SELECT that returns a list
    protected <T> List<T> queryList(String sql,
                                    PreparedStatementSetter setter,
                                    RowMapper<T> mapper) throws SQLException {
        List<T> results = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (setter != null) {
                setter.setParameters(pstmt);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapper.mapRow(rs));
                }
            }
        }
        return results;
    }

    // Template Method for a SELECT that returns one object (or null)
    protected <T> T querySingle(String sql,
                                PreparedStatementSetter setter,
                                RowMapper<T> mapper) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (setter != null) {
                setter.setParameters(pstmt);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapper.mapRow(rs);
                }
            }
        }
        return null;
    }

    // Template Method for INSERT, UPDATE, DELETE
    protected int executeUpdate(String sql,
                                PreparedStatementSetter setter) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (setter != null) {
                setter.setParameters(pstmt);
            }

            return pstmt.executeUpdate();
        }
    }

    // Functional interfaces for the variable parts
    @FunctionalInterface
    protected interface PreparedStatementSetter {
        void setParameters(PreparedStatement pstmt) throws SQLException;
    }

    @FunctionalInterface
    protected interface RowMapper<T> {
        T mapRow(ResultSet rs) throws SQLException;
    }
}