package com.example.taskmaster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TaskDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMainActivity = new Intent(TaskDetail.this, MainActivity.class);
                startActivity(goToMainActivity);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        String title = intent.getExtras().getString("title");
        TextView taskDetailTitle = findViewById(R.id.taskDetailTitle);
        taskDetailTitle.setText(title);

    }
}