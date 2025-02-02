package com.example.iaassistent;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.iaassistent.utils.PermissionHelper;
import com.example.iaassistent.services.cam.CameraXService;
import com.example.iaassistent.utils.LoggerTags;
import com.example.iaassistent.services.texttospeech.TextToSpeechService;
//import com.example.iaassistent.services.DummyAssistent;
import com.example.iaassistent.services.AssistentService;



public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        //DummyAssistent assistent = new DummyAssistent();

        TextToSpeechService speecher = new TextToSpeechService(this);




        // Check and request permissions
        if (PermissionHelper.allPermissionsGranted(this)) {

            Log.i(LoggerTags.MAIN_ACTIVITY.getTag(), "All permission granted for enabling system");

            PreviewView previewView = findViewById(R.id.camera);

            CameraXService cameraService = new CameraXService(this, previewView);
            cameraService.startCamera();


            findViewById(R.id.captureImg).setOnClickListener(v -> {

                AssistentService assistent = new AssistentService(this, cameraService);
                assistent.describe();

                //speecher.speak(
                //        assistent.call()
                //);


            });
        } else {
            Log.i(LoggerTags.MAIN_ACTIVITY.getTag(), "Pending permission request");

            PermissionHelper.requestPermissions(this);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelper.handlePermissionsResult(requestCode, grantResults, this,
                () -> {
                    Log.i(LoggerTags.MAIN_ACTIVITY.getTag(), "Permissions granted, starting functionalities...");

                    PreviewView previewView = findViewById(R.id.camera);
                    CameraXService cameraService = new CameraXService(this, previewView);
                    cameraService.startCamera();
                },
                () -> {
                    Log.i(LoggerTags.MAIN_ACTIVITY.getTag(), "Permissions not granted, exiting app...");
                    finish();
                });
    }


}
