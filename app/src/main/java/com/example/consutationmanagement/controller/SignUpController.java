package com.example.consutationmanagement.controller;

import android.content.Context;

import com.example.consutationmanagement.model.Patient;

public final class SignUpController {
    private PatientDatabase databaseHelper;

    public SignUpController(Context context) {
        databaseHelper = new PatientDatabase(context);
    }

    public boolean addPatient(String nom, String prenom, int age, String email, String password) {
        if (!databaseHelper.checkUserByEmail(email)){
            Patient pt = new Patient(nom,prenom,age,email,password);
            return databaseHelper.insertPatient(pt);
        }
        return false;
    }
}
