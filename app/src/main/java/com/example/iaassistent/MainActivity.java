package com.example.iaassistent;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.iaassistent.utils.PermissionHelper;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        // Check and request permissions
        if (PermissionHelper.allPermissionsGranted(this)) {
            Toast.makeText(this, "Turn on camera", Toast.LENGTH_SHORT).show();

            // Start your camera or any functionality that requires permissions
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
                    // Permissions granted, start your camera or functionality
                    Toast.makeText(this, "Permissions granted!", Toast.LENGTH_SHORT).show();
                },
                () -> {
                    Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }
}
