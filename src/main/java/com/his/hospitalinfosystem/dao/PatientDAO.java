package com.his.hospitalinfosystem.dao;

import com.his.hospitalinfosystem.model.Patient;
import java.sql.SQLException;
import java.util.List;

public interface PatientDAO {
    /**
     * Create a new patient record in the database
     * @param patient The patient object to be inserted
     * @return The created patient with generated ID
     * @throws SQLException If a database error occurs
     */
    Patient create(Patient patient) throws SQLException;

    /**
     * Retrieve a patient by their ID
     * @param patientId The ID of the patient to retrieve
     * @return The patient object if found, null otherwise
     * @throws SQLException If a database error occurs
     */
    Patient findById(Long patientId) throws SQLException;

    /**
     * Retrieve all patients from the database
     * @return A list of all patients
     * @throws SQLException If a database error occurs
     */
    List<Patient> findAll() throws SQLException;

    /**
     * Update an existing patient record
     * @param patient The patient object with updated information
     * @return true if the update was successful, false otherwise
     * @throws SQLException If a database error occurs
     */
    boolean update(Patient patient) throws SQLException;

    /**
     * Delete a patient record by ID
     * @param patientId The ID of the patient to delete
     * @return true if the deletion was successful, false otherwise
     * @throws SQLException If a database error occurs
     */
    boolean delete(Long patientId) throws SQLException;

    /**
     * Find patients by surname
     * @param surname The surname to search for
     * @return A list of patients matching the surname
     * @throws SQLException If a database error occurs
     */
    List<Patient> findBySurname(String surname) throws SQLException;
}