package com.example.memorryaplicationproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

public class SetUpAccountActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText surnameEditText;
    private Button addMedication;
    private Button continueSetup;
    private DatePicker dateOfBirth;
    private Spinner genderSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_account);

        nameEditText = findViewById(R.id.nameSetUp);
        surnameEditText = findViewById(R.id.surnameSetUp);
        addMedication = findViewById(R.id.addPill);
        continueSetup = findViewById(R.id.continueSetup);
        dateOfBirth = findViewById(R.id.dateOfBirthPicker);
        genderSpinner = findViewById(R.id.genderInput);

        continueSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SetUpAccountActivity.this, MainMenuActivity.class));
            }
        });

    }
}