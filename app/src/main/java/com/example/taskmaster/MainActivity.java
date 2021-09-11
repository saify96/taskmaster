package com.example.taskmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Task> tasksList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView allTasksRecyclerView = findViewById(R.id.allTasksRecyclerView);

        Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                allTasksRecyclerView.getAdapter().notifyDataSetChanged();
                return false;
            }
        });

        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.configure(getApplicationContext());
            Log.i("MyAmplifyApp", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }

        Amplify.API.query(
                ModelQuery.list(com.amplifyframework.datastore.generated.model.Task.class),
                response -> {
                    for (com.amplifyframework.datastore.generated.model.Task t : response.getData()) {
                        tasksList.add(t);
                        Log.i("MyAmplifyApp", t.getTitle());
                        Log.i("MyAmplifyApp", t.getDescription());
                    }
                    handler.sendEmptyMessage(1);
                },
                error -> Log.e("MyAmplifyApp", "Query failure", error)
        );

        Button addTaskButton = findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToAddTaskActivity = new Intent(MainActivity.this, AddTask.class);
                goToAddTaskActivity.putExtra("totalTasks",tasksList.size());
                System.out.println("sizeeeee "+ tasksList.size());
                startActivity(goToAddTaskActivity);
            }
        });

        Button settingButton = findViewById(R.id.settingButton);
        settingButton.setOnClickListener(view -> {
            Intent goToSettingActivity = new Intent(MainActivity.this, SettingPage.class);
            startActivity(goToSettingActivity);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        RecyclerView allTasksRecyclerView = findViewById(R.id.allTasksRecyclerView);
        allTasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        allTasksRecyclerView.setAdapter(new TaskAdapter(tasksList));
    }

    @Override
    protected void onResume() {
        super.onResume();

        String tasks = "'s Tasks";

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String userName = sharedPreferences.getString("userName", "User");

        TextView userNameView = findViewById(R.id.userNameView);
        userNameView.setText(userName + tasks);
    }
}



