package com.example.taskmaster;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.core.Amplify;

import java.io.File;

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

        String desc = intent.getExtras().getString("description");
        TextView taskDetailDesc = findViewById(R.id.taskDetailDesc);
        taskDetailDesc.setText(desc);

        String state = intent.getExtras().getString("state");
        TextView taskDetailState = findViewById(R.id.taskDetailState);
        taskDetailState.setText(state);

        String lon = intent.getExtras().getString("lon");
        String lat = intent.getExtras().getString("lat");
        if(lon!=null||lat!=null){
            TextView location = findViewById(R.id.location);
            location.setText("lat "  +lat + " , lon "+lon);

        }

        String image = intent.getExtras().getString("image");
        ImageView imageView = findViewById(R.id.imageView);

        Amplify.Storage.downloadFile(
                image,
                new File(getApplicationContext().getFilesDir() + "/download.txt"),
                result -> {
                    Log.i("MyAmplifyApp", "Successfully downloaded: " + result.getFile().getName());
                    imageView.setImageBitmap(BitmapFactory.decodeFile(result.getFile().getPath()));
                },
                error -> Log.e("MyAmplifyApp",  "Download Failure", error)

        );
    }
}