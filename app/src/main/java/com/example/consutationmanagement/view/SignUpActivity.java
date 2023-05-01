package com.example.consutationmanagement.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.consutationmanagement.R;
import com.example.consutationmanagement.controller.PatientDatabase;
import com.example.consutationmanagement.model.Patient;

public class SignUpActivity extends AppCompatActivity  {

    private EditText editTextEmail, editTextPassword,editTextFirstName,editTextLastName,editTextAge;
    private Button buttonInscription;
    PatientDatabase db = new PatientDatabase(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
        buttonInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérer l'adresse email et le mot de passe
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String firstname= editTextFirstName.getText().toString();
                String lastname= editTextLastName.getText().toString();
                String age = editTextAge.getText().toString();



                // Ajouter l'utilisateur dans la base de données
                if (!email.isEmpty() && !password.isEmpty() && !firstname.isEmpty() && !lastname.isEmpty() && !age.isEmpty()){
                db.insertPatient(new Patient(firstname,lastname,Integer.parseInt(age),email,password));
                    Intent intent1 = new Intent(SignUpActivity.this, PatientActivity.class);
                    intent1.putExtra("nom_utilisateur", firstname);
                    intent1.putExtra("prenom_utilisateur", lastname);
                    startActivity(intent1);
                }
                // Enregistrer les informations de l'utilisateur dans la base de données
// ...

                // Définir les valeurs de nom et prénom dans l'Intent



                // Retourner à la page MainActivity
                /*
                Intent intent = new Intent(inscriptionActivity.this, MainActivity.class);
                startActivity(intent);*/

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

}