package com.example.consutationmanagement.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.consutationmanagement.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class LogInActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editTextEmail, editTextPassword;
    Button buttonLogin, buttonCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        init();
        ecouteClick();
    }

    private void init() {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);
    }

    private void ecouteClick() {
        buttonLogin.setOnClickListener((View.OnClickListener) this);
        buttonCreateAccount.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLogin:
                new ConnexionTask().execute();
                break;
            case R.id.buttonCreateAccount:
                navigateToActivity(SignUpActivity.class);
                break;
            default:
                break;
        }
    }


    private void navigateToActivity(Class nextActivity) {
        Intent intent = new Intent(LogInActivity.this, nextActivity);
        startActivity(intent);
        finish();
    }

    boolean verifConx() {
        // Création de la connexion au serveur
        Socket socket = null;
        try {
            socket = new Socket("10.0.2.15", 8080);
        } catch (IOException e) {
            System.out.println("erreur connexion : " + e.getMessage());
        }
        InputStream inputStream = null;

        // Obtention des flux d'entrée et de sortie pour communiquer avec le serveur
        OutputStream outputStream = null;
        try {
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        // Création d'un objet de type DataOutputStream pour envoyer les données d'accès au serveur
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        // Envoi des données d'accès au serveur sous forme de chaîne de caractères
        String email = editTextEmail.getText().toString();
        String motDePasse = editTextPassword.getText().toString();
        String message = "CONNEXION\n" + email + "\n" + motDePasse;
        try {
            dataOutputStream.writeUTF(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Lecture de la réponse du serveur
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        String reponse = null;
        try {
            reponse = dataInputStream.readUTF();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Traitement de la réponse du serveur
        return (reponse.equals("OK"));
    }

    private class ConnexionTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            // Connectez-vous au serveur ici
            return verifConx();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                navigateToActivity(PatientActivity.class);
            } else {
                Toast.makeText(LogInActivity.this, "Login failed, please try again", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
