package com.example.consutationmanagement.model;

public class Doctor {
    private static int idDoctor = 1;
    private String firstName;
    private String lastName;
    private String email;
    private String mdp;

    public static int getIdDoctor() {
        return idDoctor;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getMdp() {
        return mdp;
    }

    public String getSpecialite() {
        return specialite;
    }

    public Doctor(String firstName, String lastName, String email, String mdp, String specialite) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mdp = mdp;
        this.specialite = specialite;
    }

    private String specialite;
}
