package com.example.consutationmanagement.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.consutationmanagement.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;

public class PatientActivity extends AppCompatActivity {
    EditText editTextPhone,editTextDesc;
    DatePicker datePicker;
    TimePicker timePicker;
    Button buttonConf;
    private Socket socket;
    private OutputStream outputStream;
    private InputStream inputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        init();
        buttonConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tel = editTextPhone.getText().toString();
                String desc = editTextPhone.getText().toString();
                String date = String.format("%04d-%02d-%02d",datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth());
                String time = String.format("%02d:%02d",timePicker.getHour(),timePicker.getMinute());
                Rdv rdv= new Rdv(tel,desc,date,time);
                sendUserToServer(rdv);
            }
        });


    }
    void init(){
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextDesc = findViewById(R.id.editTextDesc);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        buttonConf = findViewById(R.id.buttonConf);
    }
    private void navigateToActivity(Class nextActivity) {
        Intent intent = new Intent(PatientActivity.this, nextActivity);
        startActivity(intent);
        finish();
    }
    private void sendUserToServer(Rdv rdv) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket("10.0.2.15", 8080);
                    System.out.println("connected");
                    String data = rdv.toString();
                    sendMessage(socket ,data);



                    System.out.println("data sended");


                    String response =receiveMessage(socket);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (response.equals("Ajout Validé")) {
                                navigateToActivity(PatientActivity.class);
                            } else {
                                Toast.makeText(PatientActivity.this, "Impossible de créer le compte", Toast.LENGTH_SHORT).show();
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