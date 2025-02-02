package com.example.iaassistent.services.cam;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.example.iaassistent.utils.LoggerTags;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.concurrent.ExecutionException;

public class CameraXService {
    private final ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private final Context context;
    private final PreviewView previewView;
    private ImageCapture imageCapture;

    public CameraXService(Context context, PreviewView previewView) {
        this.previewView = previewView;
        this.context = context;
        this.cameraProviderFuture = ProcessCameraProvider.getInstance(context);
    }

    public void startCamera() {
        this.cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                this.bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Log.e(LoggerTags.CAMERAX_SERVICE.getTag(), "Camera provider initialization error");
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

        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) this.context, cameraSelector, preview, imageCapture);
    }

    // Novo método para capturar a foto com callback
    public void takePhoto(File file, PhotoCaptureCallback callback) {
        if (imageCapture == null) {
            Log.e(LoggerTags.CAMERAX_SERVICE.getTag(), "ImageCapture is not initialized");
            callback.onError(new Exception("ImageCapture is not initialized"));
            return;
        }

        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();

        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(context), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                Log.d(LoggerTags.CAMERAX_SERVICE.getTag(), "Photo capture succeeded: " + file.getAbsolutePath());
                callback.onPhotoCaptured(file); // Notifica o sucesso e passa o arquivo
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Log.e(LoggerTags.CAMERAX_SERVICE.getTag(), "Photo capture failed: " + exception.getMessage());
                callback.onError(exception); // Notifica o erro
            }
        });
    }

    // Interface para callback da captura de foto
    public interface PhotoCaptureCallback {
        void onPhotoCaptured(File capturedFile); // Sucesso na captura da foto

        void onError(Exception e); // Erro na captura da foto
    }
}
