package com.example.iaassistent;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.iaassistent.utils.PermissionHelper;
import com.example.iaassistent.services.CameraXService;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        // Check and request permissions
        if (PermissionHelper.allPermissionsGranted(this)) {

            PreviewView previewView = findViewById(R.id.camera);
            CameraXService cameraService = new CameraXService(this, previewView);
            cameraService.startCamera();
        } else {
            Toast.makeText(this, "Request permission", Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(this, "Permissions granted!", Toast.LENGTH_SHORT).show();

                    PreviewView previewView = findViewById(R.id.camera);
                    CameraXService cameraService = new CameraXService(this, previewView);
                    cameraService.startCamera();
                },
                () -> {
                    Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }
}
