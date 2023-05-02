package com.example.consutationmanagement.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.consutationmanagement.R;

public class SignUpActivity extends AppCompatActivity  {

    private EditText editTextEmail, editTextPassword,editTextFirstName,editTextLastName,editTextAge;
    private Button buttonInscription;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
        buttonInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    void init(){
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextFirstName=findViewById(R.id.edit_text_prenom);
        editTextLastName=findViewById(R.id.edit_text_nom);
        editTextAge = findViewById(R.id.edit_text_age);
        buttonInscription = findViewById(R.id.button_create_account);
    }
    private void navigateToActivity(Class nextActivity) {
        Intent intent = new Intent(SignUpActivity.this, nextActivity);
        startActivity(intent);
        finish();
    }



}