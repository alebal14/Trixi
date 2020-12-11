package com.trixibackend.entity;

import java.io.File;

public class Image {

    private File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "Image{" +
                "file=" + file +
                '}';
    }
}
