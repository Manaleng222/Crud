/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.crud.repository;

/**
 *
 * @author USER
 */

//import com.crud.Model.Employee;
import com.mycompany.crud.Model.Employee;
import java.sql.SQLException;
import java.util.List;

public interface EmployeeRepository {
    void add(Employee emp) throws SQLException;
    List<Employee> findAll() throws SQLException;
    Employee findById(int id) throws SQLException;
    void update(Employee emp) throws SQLException;
    void delete(int id) throws SQLException;
}
