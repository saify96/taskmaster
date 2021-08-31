package com.example.taskmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button anasButton = findViewById(R.id.anasButton);
        anasButton.setOnClickListener(view -> {
            Intent goToTaskDetailActivity = new Intent(MainActivity.this, TaskDetail.class);
            goToTaskDetailActivity.putExtra("title", "Anas");
            startActivity(goToTaskDetailActivity);
        });
        Button majdButton = findViewById(R.id.majdButton);
        majdButton.setOnClickListener(view -> {
            Intent goToTaskDetailActivity = new Intent(MainActivity.this, TaskDetail.class);
            goToTaskDetailActivity.putExtra("title", "Majd");
            startActivity(goToTaskDetailActivity);
        });
        Button ayyoubButton = findViewById(R.id.ayyoubButton);
        ayyoubButton.setOnClickListener(view -> {
            Intent goToTaskDetailActivity = new Intent(MainActivity.this, TaskDetail.class);
            goToTaskDetailActivity.putExtra("title", "Ayyoub");
            startActivity(goToTaskDetailActivity);
        });
        Button settingButton = findViewById(R.id.settingButton);
        settingButton.setOnClickListener(view -> {
            Intent goToSettingActivity = new Intent(MainActivity.this, SettingPage.class);
            startActivity(goToSettingActivity);
        });

        List<Task> tasksList = new ArrayList<>();
        tasksList.add(new Task("Task 1", "lab", "new"));
        tasksList.add(new Task("Task 2", "Code Challenge", "assigned"));
        tasksList.add(new Task("Task 3", "Repeat", "in progress"));

        RecyclerView allTasksRecyclerView = findViewById(R.id.allTasksRecyclerView);
        allTasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        allTasksRecyclerView.setAdapter(new TaskAdapter(tasksList));
    }

    @Override
    protected void onResume() {
        super.onResume();
        String tasks = "'s Tasks ";

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String userName = sharedPreferences.getString("userName", "User");

        TextView userNameView = findViewById(R.id.userNameView);
        userNameView.setText(userName + tasks);

    }
}


//lab26
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        Button addTaskButton = findViewById(R.id.addTaskButton);
//        addTaskButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent goToAddTaskActivity = new Intent(MainActivity.this, AddTask.class);
//                startActivity(goToAddTaskActivity);
//            }
//        });
//        Button allTasksButton = findViewById(R.id.allTasks);
//        allTasksButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent goToAllTasksActivity = new Intent(MainActivity.this, AllTasks.class);
//            startActivity(goToAllTasksActivity);
//
//        }});
//
//    }
