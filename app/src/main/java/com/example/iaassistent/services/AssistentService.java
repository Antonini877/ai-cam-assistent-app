package com.example.iaassistent.services;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import com.example.iaassistent.services.cam.CameraXService;
import com.example.iaassistent.services.http.RequestService;
import com.example.iaassistent.services.texttospeech.TextToSpeechService;

public class AssistentService {
    private static final String TAG = "AssistentService";
    private final Context context;
    private final CameraXService cameraService;

    public AssistentService(Context context, CameraXService cameraService) {
        this.context = context;
        this.cameraService = cameraService;
    }

    public void describe() {
        File photoFile = this.generateImageFile();
        Log.d(TAG, "Generated image file: " + photoFile.getAbsolutePath());

        this.cameraService.takePhoto(photoFile, new CameraXService.PhotoCaptureCallback() {
            @Override
            public void onPhotoCaptured(File capturedFile) {
                Log.d(TAG, "Photo captured successfully: " + capturedFile.getAbsolutePath());

                String description = GetDescriptionFromImage(capturedFile);
                Log.d(TAG, "Extracted description: " + description);

                TextToSpeechService speecher = new TextToSpeechService(context);
                speecher.speak(description);
                speecher.shutdown();
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error capturing photo", e);
                TextToSpeechService speecher = new TextToSpeechService(context);
                speecher.speak("Houve um erro ao capturar a foto.");
                speecher.shutdown();
            }
        });
    }

    private File generateImageFile() {
        File file = new File(
                this.context.getExternalFilesDir(null),
                "photo_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg"
        );
        Log.d(TAG, "Image file created: " + file.getAbsolutePath());
        return file;
    }

    private String GetDescriptionFromImage(File capturedFile) {
        Log.d(TAG, "Sending image for description: " + capturedFile.getAbsolutePath());

        RequestService requester = new RequestService();
        try {
            String response = requester.sendPostImage("http://192.168.1.8:5000/upload", capturedFile).get();
            Log.d(TAG, "Server response: " + response);

            JSONObject jsonResponse = new JSONObject(response);
            return jsonResponse.optString("description", "Descrição não encontrada");

        } catch (InterruptedException | ExecutionException | JSONException e) {
            Log.e(TAG, "Error getting description from image", e);
        } finally {
            requester.shutdown();
            Log.d(TAG, "Request service shutdown");
        }
        return "Erro ao obter a descrição";
    }
}
