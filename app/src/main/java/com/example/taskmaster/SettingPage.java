package com.example.taskmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SettingPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);

        Button backToHome = findViewById(R.id.backToHome);
        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMainActivity = new Intent(SettingPage.this, MainActivity.class);
                startActivity(goToMainActivity);
            }
        });


        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener((view) -> {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SettingPage.this);
            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

            EditText userNameField = findViewById(R.id.userNameInput);
            String userName = userNameField.getText().toString();

            sharedPreferencesEditor.putString("userName", userName);
            sharedPreferencesEditor.apply();

            Intent intent = new Intent(SettingPage.this, MainActivity.class);
            startActivity(intent);

        });

    }

}