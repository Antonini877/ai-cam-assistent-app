package com.example.iaassistent.utils;

public enum LoggerTags {
    MAIN_ACTIVITY("Main-Activity"),
    TEXT_TO_SPEECH_SERVICE("Text-To-Speech-Service"),
    CAMERAX_SERVICE("CameraX-Service");


    private final String tag;

    LoggerTags(String tag) {
        this.tag = tag;
    }

    public String getTag(){
        return this.tag;
    }
}
