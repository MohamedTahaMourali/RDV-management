package com.example.consutationmanagement.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RendezVous {
    private int doctorId;
    private int patientId;
    private LocalDate dateRdv;
    private LocalDateTime tempsRdv;

    public int getDoctorId() {
        return doctorId;
    }

    public int getPatientId() {
        return patientId;
    }

    public LocalDate getDateRdv() {
        return dateRdv;
    }

    public LocalDateTime getTempsRdv() {
        return tempsRdv;
    }

    public RendezVous(int doctorId, int patientId, LocalDate dateRdv, LocalDateTime tempsRdv) {
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.dateRdv = dateRdv;
        this.tempsRdv = tempsRdv;
    }
}
