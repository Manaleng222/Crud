/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//package com.mycompany.crud.view;

/**
 *
 * @author USER
 */
package com.crud.view;

import com.crud.dao.MySQLEmployeeRepository;
import com.mycompany.crud.Model.Employee;
import com.mycompany.crud.repository.EmployeeRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EmployeeGUI extends JFrame {

    private final EmployeeRepository repository;
    private final DefaultTableModel tableModel;

    // Input fields
    private final JTextField txtId = new JTextField(5);
    private final JTextField txtName = new JTextField(10);
    private final JTextField txtDept = new JTextField(10);
    private final JTextField txtSalary = new JTextField(10);

    private final JButton btnAdd = new JButton("Add");
    private final JButton btnUpdate = new JButton("Update");
    private final JButton btnDelete = new JButton("Delete");
    private final JButton btnClear = new JButton("Clear");

    private final JTable tblEmployee;
    private final JScrollPane scrollPane;

    public EmployeeGUI() {
        // Inject concrete implementation – could be changed to Oracle later
        repository = new MySQLEmployeeRepository();

        setTitle("Employee CRUD Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        // --- Top panel: input form ---
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formPanel.add(new JLabel("ID:"));
        txtId.setEditable(false); // ID is auto-generated, only used for update/delete
        formPanel.add(txtId);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(txtName);
        formPanel.add(new JLabel("Department:"));
        formPanel.add(txtDept);
        formPanel.add(new JLabel("Salary:"));
        formPanel.add(txtSalary);

        // --- Button panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        // --- Table ---
        String[] columns = {"ID", "Name", "Department", "Salary"};
        tableModel = new DefaultTableModel(columns, 0);
        tblEmployee = new JTable(tableModel);
        tblEmployee.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane = new JScrollPane(tblEmployee);

        // Add components to frame
        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // --- Load data ---
        refreshTable();

        // --- Button actions ---
        btnAdd.addActionListener(e -> addEmployee());
        btnUpdate.addActionListener(e -> updateEmployee());
        btnDelete.addActionListener(e -> deleteEmployee());
        btnClear.addActionListener(e -> clearFields());

        // --- Table row click -> fill fields ---
        tblEmployee.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tblEmployee.getSelectedRow();
                if (row >= 0) {
                    txtId.setText(tableModel.getValueAt(row, 0).toString());
                    txtName.setText(tableModel.getValueAt(row, 1).toString());
                    txtDept.setText(tableModel.getValueAt(row, 2).toString());
                    txtSalary.setText(tableModel.getValueAt(row, 3).toString());
                }
            }
        });
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        try {
            List<Employee> list = repository.findAll();
            for (Employee e : list) {
                tableModel.addRow(new Object[]{
                        e.getId(), e.getName(), e.getDepartment(), e.getSalary()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage());
        }
    }

    private void addEmployee() {
        try {
            String name = txtName.getText().trim();
            String dept = txtDept.getText().trim();
            double salary = Double.parseDouble(txtSalary.getText().trim());

            if (name.isEmpty() || dept.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and department required.");
                return;
            }

            repository.add(new Employee(name, dept, salary));
            refreshTable();
            clearFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid salary.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateEmployee() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            String name = txtName.getText().trim();
            String dept = txtDept.getText().trim();
            double salary = Double.parseDouble(txtSalary.getText().trim());

            if (name.isEmpty() || dept.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be filled.");
                return;
            }

            Employee emp = new Employee(id, name, dept, salary);
            repository.update(emp);
            refreshTable();
            clearFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID or salary.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteEmployee() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            int confirm = JOptionPane.showConfirmDialog(this, "Delete employee #" + id + "?",
                    "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                repository.delete(id);
                refreshTable();
                clearFields();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid ID to delete.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtDept.setText("");
        txtSalary.setText("");
        tblEmployee.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmployeeGUI().setVisible(true));
    }
}
