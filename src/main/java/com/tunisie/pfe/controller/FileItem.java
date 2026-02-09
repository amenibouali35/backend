package com.tunisie.pfe.controller;


public class FileItem {

    private String name;
    private String path;
    private boolean directory;

    public FileItem(String name, String path, boolean directory) {
        this.name = name;
        this.path = path;
        this.directory = directory;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public boolean isDirectory() {
        return directory;
    }
}
