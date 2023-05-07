package com.example.consutationmanagement.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.consutationmanagement.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SignUpActivity extends AppCompatActivity  {

    private EditText editTextEmail, editTextPassword,editTextFirstName,editTextLastName,editTextAge;
    private Button buttonInscription;
    private Socket socket= null;
    private OutputStream outputStream=null;
    private InputStream inputStream =null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
        buttonInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName =editTextFirstName.getText().toString();
                String lastName =editTextLastName.getText().toString();
                String age =editTextAge.getText().toString();
                String email =editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                User user = new User(firstName,lastName,age,email,password);
                sendUserToServer(user);

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
    private void sendUserToServer(User user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket("10.0.2.15", 8080);
                    System.out.println("connected");
                    String data = user.toString();
                    sendMessage(socket ,data);



                    System.out.println("data sended");


                    String response =receiveMessage(socket);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (response.equals("Ajout Validé")) {
                                navigateToActivity(LogInActivity.class);
                            } else {
                                Toast.makeText(SignUpActivity.this, "Impossible de créer le compte", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void sendMessage(Socket socket,String data ){

        try {
            outputStream = socket.getOutputStream();
            outputStream.write(data.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private String receiveMessage (Socket socket){

        try {
            inputStream = socket.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead = inputStream.read(buffer);
            System.out.println(bytesRead);
            String response = new String(buffer, 0, bytesRead);
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void close (){
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



}