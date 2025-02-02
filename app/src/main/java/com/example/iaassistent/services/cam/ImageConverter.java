package com.example.iaassistent.services.cam;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ImageConverter {

    private File file;
    public ImageConverter(File file){
        this.file = file;
    }

    public byte[] convertFileToBytes() throws IOException {
        FileInputStream fis = new FileInputStream(this.file);
        byte[] bytesArray = new byte[(int) this.file.length()];
        fis.read(bytesArray);
        fis.close();
        return bytesArray;
    }
}
