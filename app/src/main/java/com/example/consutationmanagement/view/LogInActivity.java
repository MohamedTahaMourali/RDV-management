package com.example.consutationmanagement.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.consutationmanagement.R;

import java.io.IOException;
import java.io.InputStream;

import java.io.OutputStream;
import java.net.Socket;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText emailEditText;
    private EditText passwordEditText;
    Button loginButton,signUpButton;
    private OutputStream outputStream = null;
    private InputStream inputStream = null;
    private Socket socket = null;

    void init(){
        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        signUpButton = findViewById(R.id.buttonCreateAccount);
        loginButton = findViewById(R.id.buttonLogin);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        init();
        ecoutClick();

    }
    void ecoutClick(){
        loginButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonCreateAccount:
                navigateToActivity(SignUpActivity.class);
                break;
            case R.id.buttonLogin:
                sendUserToServer(emailEditText.getText().toString(),passwordEditText.getText().toString());
                break;
            default:break;
        }

    }
    private void navigateToActivity(Class nextActivity) {
        Intent intent = new Intent(LogInActivity.this, nextActivity);
        startActivity(intent);
        finish();
    }


    private void sendUserToServer(String email,String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                     socket = new Socket("10.0.2.15", 8080);
                    System.out.println("connected");
                    String data = email+" "+password;
                    sendMessage(socket ,data);



                    System.out.println("data sended");


                    String response =receiveMessage(socket);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (response.equals("utiliateur existant")) {
                                navigateToActivity(PatientActivity.class);
                            } else {
                                Toast.makeText(LogInActivity.this, "Error: Invalid email or password", Toast.LENGTH_SHORT).show();
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
