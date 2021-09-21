package com.example.taskmaster;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
import androidx.core.app.ActivityCompat;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AddTask extends AppCompatActivity {
    HashMap<String, Team> teamList = new HashMap<>();
    String uploadedFileName;
    Uri dataFromS3;
    double longitude;
    double latitude;

    private FusedLocationProviderClient fusedLocationClient;

    void handleSendText(Intent intentFotShared) {
        String sharedText = intentFotShared.getStringExtra(intentFotShared.EXTRA_TEXT);
        if (sharedText != null) {
            TextView desc = findViewById(R.id.taskDesc);
            desc.setText(sharedText);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Intent intentFotShared = getIntent();
        String action = intentFotShared.getAction();
        String type = intentFotShared.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intentFotShared); // Handle text being sent
            }
        }

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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, 100);

            boolean x = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
//                            Geocoder geocoder = new Geocoder(AddTask.this, Locale.getDefault());
//                            try {
//                                List<Address> potato= geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),2);
//                                potato.get(0).getCountryName();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
                            longitude = location.getLongitude();
                            latitude = location.getLatitude();
                            System.out.println("Latitude: " + latitude + " - " + "Longitude: " + longitude);
                        }
                    }
                });

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

            System.out.println("longitude " + longitude);
            System.out.println("latitude " + latitude);

            com.amplifyframework.datastore.generated.model.Location location =
                    com.amplifyframework.datastore.generated.model.Location.builder().lon(longitude).lat(latitude).build();

            Amplify.API.mutate(
                    ModelMutation.create(location),
                    response -> Log.i("MyAmplifyApp", "Added location with id: " + response.getData().getId()),
                    error -> Log.e("MyAmplifyApp", "Create failed", error)
            );

            com.amplifyframework.datastore.generated.model.Task task = com.amplifyframework.datastore.generated.model.Task.builder()
                    .title(taskTitle)
                    .description(taskDesc)
                    .status("NEW")
                    .team(teamList.get(radioButoon))
                    .image(uploadedFileName)
                    .location(location)
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
