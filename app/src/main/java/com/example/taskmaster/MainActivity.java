package com.example.taskmaster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addTaskButton = findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // move to a new activity using an intent
                Intent goToAddTaskActivity = new Intent(MainActivity.this, AddTask.class);
                // append value to intent
                startActivity(goToAddTaskActivity);
            }
        });
        Button allTasksButton = findViewById(R.id.allTasks);
        allTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToAllTasksActivity = new Intent(MainActivity.this, AllTasks.class);
            startActivity(goToAllTasksActivity);

        }});

    }
}