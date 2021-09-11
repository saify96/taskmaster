package com.example.taskmaster;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.HashMap;

public class AddTask extends AppCompatActivity {
    HashMap<String ,Team> teamList= new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Amplify.API.query(
                ModelQuery.list(Team.class),
                response -> {
                    System.out.println("response" + response.getData());
                    for (Team t : response.getData()) {
                        teamList.put(t.getName(),t);
                        Log.i("MyAmplifyApp", t.getName());
                    }
//                        handler.sendEmptyMessage(1);
                },
                error -> Log.e("MyAmplifyApp", "Query failure", error)
        );

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "submitted!", Toast.LENGTH_LONG).show();

            EditText taskTitleField = findViewById(R.id.taskTitle);
            String taskTitle = taskTitleField.getText().toString();

            EditText taskDescField = findViewById(R.id.taskDesc);
            String taskDesc = taskDescField.getText().toString();

            RadioGroup group = findViewById(R.id.hammoudeh);
            int id = group.getCheckedRadioButtonId();
            RadioButton team = (RadioButton) findViewById(id);
            String radioButoon = (String)team.getText();
            System.out.println(radioButoon);

            com.amplifyframework.datastore.generated.model.Task task = com.amplifyframework.datastore.generated.model.Task.builder()
                    .title(taskTitle)
                    .description(taskDesc)
                    .status("NEW")
                    .team(teamList.get(radioButoon))
                    .build();

            Amplify.API.mutate(
                    ModelMutation.create(task),
                    response -> Log.i("MyAmplifyApp", "Added Todo with id: " + response.getData().getId()),
                    error -> Log.e("MyAmplifyApp", "Create failed", error)
            );

            Intent intent = new Intent(AddTask.this, MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        Integer totalTasks = intent.getExtras().getInt("totalTasks");

        TextView totalTasksView = findViewById(R.id.totalTasks);

        totalTasksView.setText("Total Tasks: "+totalTasks);
    }
}

//            RadioButton radioButton = (RadioButton)group.getCheckedRadioButtonId();
//            RadioButton team2 = findViewById(R.id.radioButtonTeam2);
//            RadioButton team3 = findViewById(R.id.radioButtonTeam3);
//            if (team1.isChecked()){
//            }

//            Team team1 = Team.builder()
//                    .name("Team 3")
//                    .build();
//
//            Amplify.API.mutate(
//                    ModelMutation.create(team1),
//                    response -> Log.i("MyAmplifyApp", "Added team with id: " + response.getData().getId()),
//                    error -> Log.e("MyAmplifyApp", "Create failed", error)
//
//            );


//            Task newTask = new Task(taskTitle,taskDesc,"new");
//            TaskDataBase db = Room.databaseBuilder(getApplicationContext(),TaskDataBase.class,"tasks-db").allowMainThreadQueries().build();
//            TaskDao taskDao=db.taskDao();
//            taskDao.insertDishes(newTask);
