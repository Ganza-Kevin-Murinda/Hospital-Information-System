package com.his.hospitalinfosystem.dao;

import com.his.hospitalinfosystem.model.Patient;
import com.his.hospitalinfosystem.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAOImpl implements PatientDAO {

    // SQL queries
    private static final String INSERT_PATIENT =
            "INSERT INTO patient (patient_id, surname, first_name, address, telephone_nr, \"createdAt\") VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_PATIENT_BY_ID =
            "SELECT * FROM patient WHERE patient_id = ?";
    private static final String SELECT_ALL_PATIENTS =
            "SELECT * FROM patient";
    private static final String UPDATE_PATIENT =
            "UPDATE patient SET surname = ?, first_name = ?, address = ?, telephone_nr = ? WHERE patient_id = ?";
    private static final String DELETE_PATIENT =
            "DELETE FROM patient WHERE patient_id = ?";
    private static final String SELECT_PATIENTS_BY_SURNAME =
            "SELECT * FROM patient WHERE surname LIKE ?";
    private static final String SELECT_MAX_ID =
            "SELECT MAX(patient_id) FROM patient";

    @Override
    public Patient create(Patient patient) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) {
                throw new SQLException("Failed to connect to database");
            }

            // Get the next available ID
            Long nextId = getNextId(conn);
            patient.setPatientId(nextId);

            stmt = conn.prepareStatement(INSERT_PATIENT);
            stmt.setLong(1, patient.getPatientId());
            stmt.setString(2, patient.getSurname());
            stmt.setString(3, patient.getFirstName());
            stmt.setString(4, patient.getAddress());
            stmt.setString(5, patient.getTelephoneNr());
            stmt.setTimestamp(6, patient.getCreatedAt());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating patient failed, no rows affected.");
            }

            return patient;
        } catch (SQLException e) {
            System.err.println("Error creating patient: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    @Override
    public Patient findById(Long patientId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Patient patient = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) {
                throw new SQLException("Failed to connect to database");
            }

            stmt = conn.prepareStatement(SELECT_PATIENT_BY_ID);
            stmt.setLong(1, patientId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                patient = mapResultSetToPatient(rs);
            }

            return patient;
        } catch (SQLException e) {
            System.err.println("Error finding patient by ID: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    @Override
    public List<Patient> findAll() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Patient> patients = new ArrayList<>();

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) {
                throw new SQLException("Failed to connect to database");
            }

            stmt = conn.prepareStatement(SELECT_ALL_PATIENTS);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Patient patient = mapResultSetToPatient(rs);
                patients.add(patient);
            }

            return patients;
        } catch (SQLException e) {
            System.err.println("Error finding all patients: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    @Override
    public boolean update(Patient patient) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) {
                throw new SQLException("Failed to connect to database");
            }

            //checks if patient exists
            Patient patientId = findById(patient.getPatientId());
            if (patientId == null) {
                throw new SQLException("No patient found with ID: " + patient.getPatientId());
            }

            stmt = conn.prepareStatement(UPDATE_PATIENT);
            stmt.setString(1, patient.getSurname());
            stmt.setString(2, patient.getFirstName());
            stmt.setString(3, patient.getAddress());
            stmt.setString(4, patient.getTelephoneNr());
            stmt.setLong(5, patient.getPatientId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating patient: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    @Override
    public boolean delete(Long patientId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) {
                throw new SQLException("Failed to connect to database");
            }

            //checks if patient exists
            Patient patient = findById(patientId);
            if (patient == null) {
                throw new SQLException("No patient found with ID: " + patientId);
            }

            stmt = conn.prepareStatement(DELETE_PATIENT);
            stmt.setLong(1, patientId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting patient: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    @Override
    public List<Patient> findBySurname(String surname) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Patient> patients = new ArrayList<>();

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) {
                throw new SQLException("Failed to connect to database");
            }

            stmt = conn.prepareStatement(SELECT_PATIENTS_BY_SURNAME);
            stmt.setString(1, "%" + surname + "%");
            rs = stmt.executeQuery();

            while (rs.next()) {
                Patient patient = mapResultSetToPatient(rs);
                patients.add(patient);
            }

            return patients;
        } catch (SQLException e) {
            System.err.println("Error finding patients by surname: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // Helper method to map ResultSet to Patient object
    private Patient mapResultSetToPatient(ResultSet rs) throws SQLException {
        Patient patient = new Patient();
        patient.setPatientId(rs.getLong("patient_id"));
        patient.setSurname(rs.getString("surname"));
        patient.setFirstName(rs.getString("first_name"));
        patient.setAddress(rs.getString("address"));
        patient.setTelephoneNr(rs.getString("telephone_nr"));
        patient.setCreatedAt(rs.getTimestamp("createdAt"));
        return patient;
    }

    // Helper method to close resources
    private void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }

    // Helper method to get the next available ID
    private Long getNextId(Connection conn) throws SQLException {

        try (PreparedStatement stmt = conn.prepareStatement(SELECT_MAX_ID); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                long maxId = rs.getLong(1);
                return maxId == 0 ? 1 : maxId + 1;
            }
            return 1L;
        }
    }
}