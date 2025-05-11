package com.his.hospitalinfosystem.controller;

import com.his.hospitalinfosystem.dao.PatientDAO;
import com.his.hospitalinfosystem.dao.PatientDAOImpl;
import com.his.hospitalinfosystem.model.Patient;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PatientController implements Initializable {

    // TableView and columns
    @FXML private TableView<Patient> patientTable;
    @FXML private TableColumn<Patient, Long> colId;
    @FXML private TableColumn<Patient, String> colSurname;
    @FXML private TableColumn<Patient, String> colFirstName;
    @FXML private TableColumn<Patient, String> colAddress;
    @FXML private TableColumn<Patient, String> colTelephone;
    @FXML private TableColumn<Patient, String> colCreatedAt;

    // Form fields
    @FXML private TextField txtId;
    @FXML private TextField txtSurname;
    @FXML private TextField txtFirstName;
    @FXML private TextField txtAddress;
    @FXML private TextField txtTelephone;
    @FXML private TextField txtSearch;

    // Buttons
    @FXML private Button btnSave;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClear;
    @FXML private Button btnSearch;

    // Data access object
    private final PatientDAO patientDAO = new PatientDAOImpl();
    // Observable list for TableView
    private ObservableList<Patient> patientList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize the table columns
        colId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephoneNr"));
        colCreatedAt.setCellValueFactory(cellData -> {
            Timestamp timestamp = cellData.getValue().getCreatedAt();
            String formattedDate = timestamp != null ? timestamp.toString() : "";
            return new SimpleStringProperty(formattedDate);
        });

        // Load patients
        loadPatients();

        // Set up table row selection listener
        patientTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        showPatientDetails(newSelection);
                        btnUpdate.setDisable(false);
                        btnDelete.setDisable(false);
                    } else {
                        clearForm();
                        btnUpdate.setDisable(true);
                        btnDelete.setDisable(true);
                    }
                }
        );

        // Initially disable update and delete buttons
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);

        // Disable ID field as it's auto-generated
        txtId.setDisable(true);
    }

    /**
     * Load all patients from the database into the TableView
     */
    @FXML
    private void loadPatients() {
        try {
            List<Patient> patients = patientDAO.findAll();
            patientList = FXCollections.observableArrayList(patients);
            patientTable.setItems(patientList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error",
                    "Error loading patients", e.getMessage());
        }
    }

    /**
     * Save a new patient to the database
     */
    @FXML
    private void savePatient() {
        if (!validateForm()) {
            return;
        }

        try {
            Patient patient = new Patient(
                    txtSurname.getText(),
                    txtFirstName.getText(),
                    txtAddress.getText(),
                    txtTelephone.getText()
            );

            // Set current timestamp
            patient.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

            Patient savedPatient = patientDAO.create(patient);

            if (savedPatient != null) {
                patientList.add(savedPatient);
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Patient Saved", "Patient was successfully saved to the database.");
                clearForm();
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error",
                    "Error saving patient", e.getMessage());
        }
    }

    /**
     * Update an existing patient in the database
     */
    @FXML
    private void updatePatient() {
        if (!validateForm()) {
            return;
        }

        try {
            Patient selectedPatient = patientTable.getSelectionModel().getSelectedItem();
            if (selectedPatient == null) {
                showAlert(Alert.AlertType.WARNING, "No Selection",
                        "No Patient Selected", "Please select a patient in the table.");
                return;
            }

            // Update a patient object with form data
            selectedPatient.setSurname(txtSurname.getText());
            selectedPatient.setFirstName(txtFirstName.getText());
            selectedPatient.setAddress(txtAddress.getText());
            selectedPatient.setTelephoneNr(txtTelephone.getText());

            boolean success = patientDAO.update(selectedPatient);

            if (success) {
                // Refresh table
                int selectedIndex = patientTable.getSelectionModel().getSelectedIndex();
                patientList.set(selectedIndex, selectedPatient);

                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Patient Updated", "Patient was successfully updated in the database.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error",
                        "Update Failed", "Failed to update patient record.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error",
                    "Error updating patient", e.getMessage());
        }
    }

    /**
     * Delete a patient from the database
     */
    @FXML
    private void deletePatient() {
        Patient selectedPatient = patientTable.getSelectionModel().getSelectedItem();
        if (selectedPatient == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "No Patient Selected", "Please select a patient in the table.");
            return;
        }

        // Confirm deletion
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete Patient");
        confirmAlert.setContentText("Are you sure you want to delete this patient?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean success = patientDAO.delete(selectedPatient.getPatientId());

                if (success) {
                    patientList.remove(selectedPatient);
                    showAlert(Alert.AlertType.INFORMATION, "Success",
                            "Patient Deleted", "Patient was successfully deleted from the database.");
                    clearForm();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error",
                            "Deletion Failed", "Failed to delete patient record.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error",
                        "Error deleting patient", e.getMessage());
            }
        }
    }

    /**
     * Search for patients by surname
     */
    @FXML
    private void searchPatients() {
        String searchText = txtSearch.getText().trim();

        if (searchText.isEmpty()) {
            loadPatients();
            return;
        }

        try {
            List<Patient> searchResults = patientDAO.findBySurname(searchText);
            patientList = FXCollections.observableArrayList(searchResults);
            patientTable.setItems(patientList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error",
                    "Error searching patients", e.getMessage());
        }
    }

    /**
     * Clear all form fields and selection
     */
    @FXML
    private void clearForm() {
        txtId.clear();
        txtSurname.clear();
        txtFirstName.clear();
        txtAddress.clear();
        txtTelephone.clear();

        patientTable.getSelectionModel().clearSelection();
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
    }

    /**
     * Display the details of a selected patient in the form
     * @param patient The patient to display
     */
    private void showPatientDetails(Patient patient) {
        txtId.setText(patient.getPatientId().toString());
        txtSurname.setText(patient.getSurname());
        txtFirstName.setText(patient.getFirstName());
        txtAddress.setText(patient.getAddress());
        txtTelephone.setText(patient.getTelephoneNr());
    }

    /**
     * Validate form fields before saving or updating
     * @return true if valid, false otherwise
     */
    private boolean validateForm() {
        StringBuilder errorMessage = new StringBuilder();

        if (txtSurname.getText().trim().isEmpty()) {
            errorMessage.append("Surname cannot be empty\n");
        }

        if (txtFirstName.getText().trim().isEmpty()) {
            errorMessage.append("First name cannot be empty\n");
        }

        if (txtAddress.getText().trim().isEmpty()) {
            errorMessage.append("Address cannot be empty\n");
        }

        String phone = txtTelephone.getText().trim();
        if (phone.isEmpty()) {
            errorMessage.append("Telephone number cannot be empty\n");
        } else {
            String phonePattern = "^(?:\\+250|0)?7[9832]\\d{7}$";
            if (!phone.matches(phonePattern)) {
                errorMessage.append("Invalid telephone number format\n");
            }
        }

        if (!errorMessage.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input",
                    "Please correct the following errors:", errorMessage.toString());
            return false;
        }

        return true;
    }


    /**
     * Show an alert dialog
     */
    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}