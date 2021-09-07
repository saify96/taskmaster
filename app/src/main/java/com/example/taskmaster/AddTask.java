package com.example.taskmaster;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;

public class AddTask extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "submitted!", Toast.LENGTH_LONG).show();

            EditText taskTitleField = findViewById(R.id.taskTitle);
            String taskTitle = taskTitleField.getText().toString();

            EditText taskDescField = findViewById(R.id.taskDesc);
            String taskDesc = taskDescField.getText().toString();

            com.amplifyframework.datastore.generated.model.Task task = com.amplifyframework.datastore.generated.model.Task.builder()
                    .title(taskTitle)
                    .description(taskDesc)
                    .build();

            Amplify.API.mutate(
                    ModelMutation.create(task),
                    response -> Log.i("MyAmplifyApp", "Added Todo with id: " + response.getData().getId()),
                    error -> Log.e("MyAmplifyApp", "Create failed", error)
            );

//            Task newTask = new Task(taskTitle,taskDesc,"new");
//            TaskDataBase db = Room.databaseBuilder(getApplicationContext(),TaskDataBase.class,"tasks-db").allowMainThreadQueries().build();
//            TaskDao taskDao=db.taskDao();
//            taskDao.insertDishes(newTask);

            Intent intent = new Intent(AddTask.this, MainActivity.class);
            startActivity(intent);
        });
    }
}