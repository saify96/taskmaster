package com.example.taskmaster;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class AddTask extends AppCompatActivity {
    HashMap<String, Team> teamList = new HashMap<>();
    String uploadedFileName;
    Uri dataFromS3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Amplify.API.query(
                ModelQuery.list(Team.class),
                response -> {
                    System.out.println("response" + response.getData());
                    for (Team t : response.getData()) {
                        teamList.put(t.getName(), t);
                        Log.i("MyAmplifyApp", t.getName());
                    }
                },
                error -> Log.e("MyAmplifyApp", "Query failure", error)
        );

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(view -> {

            try {
                if (dataFromS3 != null) {
                    InputStream inputStream = getContentResolver().openInputStream(dataFromS3);
                    Amplify.Storage.uploadInputStream(
                            uploadedFileName,
                            inputStream,
                            result -> Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey()),
                            storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
                    );
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            EditText taskTitleField = findViewById(R.id.taskTitle);
            String taskTitle = taskTitleField.getText().toString();

            EditText taskDescField = findViewById(R.id.taskDesc);
            String taskDesc = taskDescField.getText().toString();

            RadioGroup group = findViewById(R.id.hammoudeh);
            int id = group.getCheckedRadioButtonId();
            RadioButton team = (RadioButton) findViewById(id);
            String radioButoon = (String) team.getText();
            System.out.println(radioButoon);

            com.amplifyframework.datastore.generated.model.Task task = com.amplifyframework.datastore.generated.model.Task.builder()
                    .title(taskTitle)
                    .description(taskDesc)
                    .status("NEW")
                    .team(teamList.get(radioButoon))
                    .image(uploadedFileName)
                    .build();

            Amplify.API.mutate(
                    ModelMutation.create(task),
                    response -> Log.i("MyAmplifyApp", "Added Todo with id: " + response.getData().getId()),
                    error -> Log.e("MyAmplifyApp", "Create failed", error)
            );

            Intent intent = new Intent(AddTask.this, MainActivity.class);
            startActivity(intent);
        });

        Button uploadButton = findViewById(R.id.uploadFile);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFile();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        Integer totalTasks = intent.getExtras().getInt("totalTasks");

        TextView totalTasksView = findViewById(R.id.totalTasks);

        totalTasksView.setText("Total Tasks: " + totalTasks);
    }

    private void pickFile() {
        Intent selectedFile = new Intent(Intent.ACTION_GET_CONTENT);
        selectedFile.setType(("*/*"));
        selectedFile = Intent.createChooser(selectedFile, "Select File");
        startActivityForResult(selectedFile, 1234);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dataFromS3 = data.getData();
        File file = new File(dataFromS3.getPath());
        uploadedFileName = file.getName();
    }
}
