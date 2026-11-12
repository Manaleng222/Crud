/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.crud.view;

/**
 *
 * @author USER
 */



import com.crud.dao.MySQLEmployeeRepository;
import com.mycompany.crud.Model.Employee;
import com.mycompany.crud.repository.EmployeeRepository;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class EmployeeApp extends Application {

    // Backend
    private final EmployeeRepository repository = new MySQLEmployeeRepository();

    // UI elements
    private final TableView<EmployeeFX> table = new TableView<>();
    private final ObservableList<EmployeeFX> data = FXCollections.observableArrayList();

    private final TextField tfName = new TextField();
    private final TextField tfDept = new TextField();
    private final TextField tfSalary = new TextField();

    private final Button btnAdd = new Button("Add");
    private final Button btnUpdate = new Button("Update");
    private final Button btnDelete = new Button("Delete");
    private final Button btnClear = new Button("Clear");

    private int selectedId = -1;   // keeps ID of currently selected row

    // Wrapper class for JavaFX TableView (propertise for columns)
    public static class EmployeeFX {
        private final SimpleIntegerProperty id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty department;
        private final SimpleDoubleProperty salary;

        public EmployeeFX(int id, String name, String department, double salary) {
            this.id = new SimpleIntegerProperty(id);
            this.name = new SimpleStringProperty(name);
            this.department = new SimpleStringProperty(department);
            this.salary = new SimpleDoubleProperty(salary);
        }

        public int getId() { return id.get(); }
        public String getName() { return name.get(); }
        public String getDepartment() { return department.get(); }
        public double getSalary() { return salary.get(); }

        public SimpleIntegerProperty idProperty() { return id; }
        public SimpleStringProperty nameProperty() { return name; }
        public SimpleStringProperty departmentProperty() { return department; }
        public SimpleDoubleProperty salaryProperty() { return salary; }
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Employee CRUD (JavaFX)");
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));

        // ----- Table -----
        setupTable();
        VBox.setVgrow(table, Priority.ALWAYS);

        // ----- Form -----
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10));
        form.add(new Label("Name:"), 0, 0);
        form.add(tfName, 1, 0);
        form.add(new Label("Department:"), 0, 1);
        form.add(tfDept, 1, 1);
        form.add(new Label("Salary:"), 0, 2);
        form.add(tfSalary, 1, 2);

        // ----- Buttons -----
        HBox buttons = new HBox(10, btnAdd, btnUpdate, btnDelete, btnClear);
        buttons.setPadding(new Insets(10));

        // Layout
        VBox topSection = new VBox(10, form, buttons);
        root.setTop(topSection);
        root.setCenter(table);

        // Load data
        refreshTable();

        // Button actions
        btnAdd.setOnAction(e -> addEmployee());
        btnUpdate.setOnAction(e -> updateEmployee());
        btnDelete.setOnAction(e -> deleteEmployee());
        btnClear.setOnAction(e -> clearForm());

        // Table row selection → fill form
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedId = newVal.getId();
                tfName.setText(newVal.getName());
                tfDept.setText(newVal.getDepartment());
                tfSalary.setText(String.valueOf(newVal.getSalary()));
            } else {
                clearForm();
            }
        });

        Scene scene = new Scene(root, 700, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupTable() {
        TableColumn<EmployeeFX, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(50);

        TableColumn<EmployeeFX, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colName.setPrefWidth(150);

        TableColumn<EmployeeFX, String> colDept = new TableColumn<>("Department");
        colDept.setCellValueFactory(new PropertyValueFactory<>("department"));
        colDept.setPrefWidth(150);

        TableColumn<EmployeeFX, Double> colSalary = new TableColumn<>("Salary");
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colSalary.setPrefWidth(100);

        table.getColumns().addAll(colId, colName, colDept, colSalary);
        table.setItems(data);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    // Load all employees from DB into TableView
    private void refreshTable() {
        try {
            List<Employee> list = repository.findAll();
            data.clear();
            for (Employee e : list) {
                data.add(new EmployeeFX(e.getId(), e.getName(), e.getDepartment(), e.getSalary()));
            }
        } catch (Exception e) {
            showError("Failed to load employees", e);
        }
    }

    private void addEmployee() {
        try {
            String name = tfName.getText().trim();
            String dept = tfDept.getText().trim();
            double salary = Double.parseDouble(tfSalary.getText().trim());

            if (name.isEmpty() || dept.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Validation", "Name and department are required.");
                return;
            }

            repository.add(new Employee(name, dept, salary));
            refreshTable();
            clearForm();
            showInfo("Employee added successfully.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Salary must be a number.");
        } catch (Exception e) {
            showError("Add failed", e);
        }
    }

    private void updateEmployee() {
        if (selectedId == -1) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an employee from the table.");
            return;
        }
        try {
            String name = tfName.getText().trim();
            String dept = tfDept.getText().trim();
            double salary = Double.parseDouble(tfSalary.getText().trim());

            if (name.isEmpty() || dept.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Validation", "Name and department are required.");
                return;
            }

            Employee updated = new Employee(selectedId, name, dept, salary);
            repository.update(updated);
            refreshTable();
            clearForm();
            showInfo("Employee updated.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Salary must be a number.");
        } catch (Exception e) {
            showError("Update failed", e);
        }
    }

    private void deleteEmployee() {
        if (selectedId == -1) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an employee to delete.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete employee #" + selectedId + "?");
        confirm.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                repository.delete(selectedId);
                refreshTable();
                clearForm();
                showInfo("Employee deleted.");
            } catch (Exception e) {
                showError("Delete failed", e);
            }
        }
    }

    private void clearForm() {
        tfName.clear();
        tfDept.clear();
        tfSalary.clear();
        table.getSelectionModel().clearSelection();
        selectedId = -1;
    }

    // ---- Helper dialogs ----
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String msg) {
        showAlert(Alert.AlertType.INFORMATION, "Success", msg);
    }

    private void showError(String context, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(context);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
        e.printStackTrace();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
