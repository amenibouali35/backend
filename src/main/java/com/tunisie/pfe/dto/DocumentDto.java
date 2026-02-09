package com.tunisie.pfe.dto;

public class DocumentDto {

    private String name;
    private String path;
    private boolean directory;

    public DocumentDto(String name, String path, boolean directory) {
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
