package com.example.consutationmanagement.view;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.consutationmanagement.R;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editTextEmail,editTextPassword ;
    Button buttonLogin,buttonCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        init();
        ecouteClick();
    }
    void init(){
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);
    }
    private void ecouteClick(){
        buttonLogin.setOnClickListener((View.OnClickListener)this );
        buttonCreateAccount.setOnClickListener((View.OnClickListener)this );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonLogin :
                navigateToActivity(PatientActivity.class);
                break;
            case R.id.buttonCreateAccount:
                navigateToActivity(SignUpActivity.class);
                break;
            default:break;
        }
    }

    private void navigateToActivity(Class nextActivity) {
        Intent intent = new Intent(LogInActivity.this, nextActivity);
        startActivity(intent);
        finish();
    }
}
