package com.example.iaassistent.services;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.ExecutionException;

public class CameraXService {
    private final ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private final Context context;
    private final PreviewView previewView;
    public CameraXService(Context context, PreviewView previewView){
        this.previewView = previewView;
        this.context = context;
        this.cameraProviderFuture = ProcessCameraProvider.getInstance(context);
    }

    public void startCamera(){
        this.cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                this.bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(this.context, "Camera provider initialization error", Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(this.context));
    }

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(this.previewView.createSurfaceProvider());

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this.context, cameraSelector, preview);
    }
}
