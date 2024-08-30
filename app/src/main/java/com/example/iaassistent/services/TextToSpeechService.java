package com.example.iaassistent.services;

import android.util.Log;
import android.speech.tts.TextToSpeech;
import android.content.Context;
import java.util.Locale;

import com.example.iaassistent.utils.LoggerTags;

public class TextToSpeechService implements TextToSpeech.OnInitListener {
    private TextToSpeech textToSpeech;
    private boolean isInitialized = false;

    public TextToSpeechService(Context context) {
        textToSpeech = new TextToSpeech(context, this);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.getDefault());

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(LoggerTags.TEXT_TO_SPEECH_SERVICE.getTag(), "Idiom not supported on Text-To-Speech.");
            } else {
                isInitialized = true;
                Log.i(LoggerTags.TEXT_TO_SPEECH_SERVICE.getTag(), "TextToSpeech initialized.");
            }
        } else {
            Log.e(LoggerTags.TEXT_TO_SPEECH_SERVICE.getTag(), "Fail to initialize Text-to-Speech service.");
        }
    }

    public void speak(String text) {
        if (isInitialized) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            Log.e(LoggerTags.TEXT_TO_SPEECH_SERVICE.getTag(), "Text-To-Speech is not initializated.");
        }
    }

    public void stop() {
        if (isInitialized && textToSpeech.isSpeaking()) {
            textToSpeech.stop();
        }
    }

    public void shutdown() {
        if (textToSpeech != null) {
            textToSpeech.shutdown();
        }
    }
}
