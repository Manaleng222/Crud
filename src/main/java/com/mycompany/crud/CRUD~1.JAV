/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.crud;

import com.crud.view.EmployeeApp;
import com.crud.view.EmployeeGUI;
import javafx.application.Application;
import javax.swing.SwingUtilities;

/**
 *
 * @author USER
 */
public class Crud {

    public static void main(String[] args) {
         /* Launch the GUI on the Swing Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new EmployeeGUI().setVisible(true);
            
        });*/
          // Launch the JavaFX Employee CRUD application
        Application.launch(EmployeeApp.class, args);
    }
}
