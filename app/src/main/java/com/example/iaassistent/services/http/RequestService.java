package com.example.iaassistent.services.http;

import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RequestService {

    private static final String TAG = "RequestService";
    private final ExecutorService executorService;

    public RequestService() {
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public Future<String> sendPostImage(String urlString, File imageFile) {
        return executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Log.d(TAG, "Sending POST request to: " + urlString);
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=---WebKitFormBoundary");
                connection.setDoOutput(true);

                String boundary = "---WebKitFormBoundary";
                String lineEnd = "\r\n";
                String twoHyphens = "--";

                try (OutputStream os = connection.getOutputStream()) {
                    os.write((twoHyphens + boundary + lineEnd).getBytes());
                    os.write(("Content-Disposition: form-data; name=\"file\"; filename=\"" + imageFile.getName() + "\"" + lineEnd).getBytes());
                    os.write(("Content-Type: image/jpeg" + lineEnd).getBytes());
                    os.write(lineEnd.getBytes());

                    Log.d(TAG, "Writing image file to request body: " + imageFile.getAbsolutePath());
                    FileInputStream fis = new FileInputStream(imageFile);
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                    fis.close();

                    os.write(lineEnd.getBytes());
                    os.write((twoHyphens + boundary + twoHyphens + lineEnd).getBytes());
                }

                int responseCode = connection.getResponseCode();
                Log.d(TAG, "Received response code: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                    StringBuilder response = new StringBuilder();
                    String inputLine;

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine.trim());
                    }
                    in.close();

                    Log.d(TAG, "Response: " + response.toString());
                    return response.toString();
                } else {
                    Log.e(TAG, "POST request failed with response code: " + responseCode);
                    throw new Exception("POST request failed with response code: " + responseCode);
                }
            }
        });
    }

    public void shutdown() {
        executorService.shutdown();
        Log.d(TAG, "Executor service shutdown");
    }
}
