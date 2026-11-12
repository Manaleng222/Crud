/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//package com.mycompany.crud.dao;

/**
 *
 * @author USER
 */
package com.crud.dao;




import com.mycompany.crud.Model.Employee;
import com.mycompany.crud.repository.AbstractJdbcRepository;
import com.mycompany.crud.repository.EmployeeRepository;

import java.sql.*;
import java.util.List;

public class MySQLEmployeeRepository
        extends AbstractJdbcRepository
        implements EmployeeRepository {

    private static final String TABLE = "employee";

    // RowMapper: converts a ResultSet row into an Employee object
    private final RowMapper<Employee> employeeMapper = rs -> new Employee(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("department"),
            rs.getDouble("salary")
    );

    @Override
    public void add(Employee emp) throws SQLException {
        String sql = "INSERT INTO " + TABLE + " (name, department, salary) VALUES (?, ?, ?)";
        executeUpdate(sql, pstmt -> {
            pstmt.setString(1, emp.getName());
            pstmt.setString(2, emp.getDepartment());
            pstmt.setDouble(3, emp.getSalary());
        });
    }

    @Override
    public List<Employee> findAll() throws SQLException {
        String sql = "SELECT * FROM " + TABLE;
        return queryList(sql, null, employeeMapper);
    }

    @Override
    public Employee findById(int id) throws SQLException {
        String sql = "SELECT * FROM " + TABLE + " WHERE id = ?";
        return querySingle(sql,
                pstmt -> pstmt.setInt(1, id),
                employeeMapper);
    }

    @Override
    public void update(Employee emp) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET name=?, department=?, salary=? WHERE id=?";
        executeUpdate(sql, pstmt -> {
            pstmt.setString(1, emp.getName());
            pstmt.setString(2, emp.getDepartment());
            pstmt.setDouble(3, emp.getSalary());
            pstmt.setInt(4, emp.getId());
        });
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM " + TABLE + " WHERE id = ?";
        executeUpdate(sql, pstmt -> pstmt.setInt(1, id));
    }
}
