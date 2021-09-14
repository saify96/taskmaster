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

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.options.AuthSignOutOptions;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.auth.result.AuthSignInResult;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Task> tasksList = new ArrayList<>();
    boolean isSignedIn;
    String userName;

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
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSS3StoragePlugin());

            Amplify.configure(getApplicationContext());
            Log.i("MyAmplifyApp", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }

        Amplify.Auth.fetchAuthSession(
                result -> {
                    Log.i("AmplifyQuickstart", result.toString());
                    isSignedIn = result.isSignedIn();
                    if (isSignedIn) {
                        userName = Amplify.Auth.getCurrentUser().getUsername();
                        TextView welcome = findViewById(R.id.welcomeMsg);
                        welcome.setText(" هلا والله " + userName);
                        findViewById(R.id.login).setVisibility(View.INVISIBLE);
                        findViewById(R.id.logout).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.logout).setVisibility(View.INVISIBLE);
                        findViewById(R.id.login).setVisibility(View.VISIBLE);
                    }
                },
                error -> Log.e("AmplifyQuickstart", error.toString())
        );

        Button loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Amplify.Auth.signInWithWebUI(
                        MainActivity.this,
                        result -> {
                            Log.i("AuthQuickStart", result.toString());
                            finish();
                            startActivity(getIntent());
                        },
                        error -> Log.e("AuthQuickStart", error.toString())
                );
            }
        });

        Button logoutButton = findViewById(R.id.logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Amplify.Auth.signOut(
                        () -> {
                            Log.i("AuthQuickstart", "Signed out successfully");
                            finish();
                            startActivity(getIntent());
                        },
                        error -> Log.e("AuthQuickstart", error.toString())
                );
            }
        });

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
                goToAddTaskActivity.putExtra("totalTasks", tasksList.size());
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



