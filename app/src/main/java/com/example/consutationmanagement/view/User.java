package com.example.consutationmanagement.view;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class User implements Serializable {
    private String email , password;
    public User(String email, String password) {
        this.email=email;
        this.password=password;
    }


    @Override
    public String toString() {
        return email + " "+ password;

    }
}
