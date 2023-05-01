package com.example.consutationmanagement.view;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.consutationmanagement.R;
import com.example.consutationmanagement.controller.DataBaseDoctor;

import java.util.ArrayList;
import java.util.List;

public class PatientActivity extends AppCompatActivity {
    EditText editTextFirstName,editTextLastName,editTextAge,editTextPhone,editTextDesc;
    DatePicker datePicker;
    TimePicker timePicker;
    Button buttonConf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        init();
    }
    void init(){
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextAge = findViewById(R.id.editTextAge);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextDesc = findViewById(R.id.editTextDesc);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        buttonConf = findViewById(R.id.buttonConf);
    }
}