package com.example.consutationmanagement.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.consutationmanagement.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class LogInActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);

        Button loginButton = findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                User user = new User(email, password);
                sendUserToServer(user);
            }
        });
    }

    private void sendUserToServer(User user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket("10.0.2.15", 8080);
                    System.out.println("connected");
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(user.toString().getBytes());

                    System.out.println("data sended");

                    InputStream inputStream = socket.getInputStream();

                    // lire les données envoyées par le client
                    byte[] buffer = new byte[1024];
                    int bytesRead = inputStream.read(buffer);
                    System.out.println(bytesRead);
                    String response = new String(buffer, 0, bytesRead);


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (response.equals("OK")) {
                                Intent intent = new Intent(LogInActivity.this, PatientActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LogInActivity.this, "Error: Invalid email or password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    inputStream.close();
                    outputStream.close();
                    socket.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
