package com.example.consutationmanagement.view;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class User implements Serializable {
    private String email , password,firstName,lastName,age;

    public User(String email, String password, String firstName, String lastName, String age) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    @Override
    public String toString() {
        return email + " "+ password+ " "+ firstName+ " "+ lastName+ " "+ age;

    }
}
