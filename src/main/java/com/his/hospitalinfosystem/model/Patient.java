package com.his.hospitalinfosystem.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Patient {
    private Long patientId;
    private String surname;
    private String firstName;
    private String address;
    private String telephoneNr;
    private Timestamp createdAt;

    // Default constructor
    public Patient() {
        this.createdAt = Timestamp.valueOf(LocalDateTime.now());
    }

    // Constructor with all fields except ID (for creating new patients)
    public Patient(String surname, String firstName, String address, String telephoneNr) {
        this.surname = surname;
        this.firstName = firstName;
        this.address = address;
        this.telephoneNr = telephoneNr;
        this.createdAt = Timestamp.valueOf(LocalDateTime.now());
    }

    // Full constructor
    public Patient(Long patientId, String surname, String firstName, String address,
                   String telephoneNr, Timestamp createdAt) {
        this.patientId = patientId;
        this.surname = surname;
        this.firstName = firstName;
        this.address = address;
        this.telephoneNr = telephoneNr;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephoneNr() {
        return telephoneNr;
    }

    public void setTelephoneNr(String telephoneNr) {
        this.telephoneNr = telephoneNr;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "patientId=" + patientId +
                ", surname='" + surname + '\'' +
                ", firstName='" + firstName + '\'' +
                ", address='" + address + '\'' +
                ", telephoneNr='" + telephoneNr + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}