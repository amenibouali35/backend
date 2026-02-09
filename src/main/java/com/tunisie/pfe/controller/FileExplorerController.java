package com.tunisie.pfe.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FileExplorerController {

    private final String baseDir = "documents";

    @GetMapping("/files")
    public String explore(
            @RequestParam(value = "path", required = false) String path,
            Model model) {

        Path currentPath = Paths.get(baseDir);
        if (path != null && !path.isEmpty()) {
            currentPath = currentPath.resolve(path);
        }

        File folder = currentPath.toFile();
        if (!folder.exists() || !folder.isDirectory()) {
            model.addAttribute("error", "Dossier introuvable");
            return "file-explorer";
        }

        List<FileItem> items = new ArrayList<>();

        for (File file : folder.listFiles()) {
            String relativePath =
                    (path == null || path.isEmpty())
                            ? file.getName()
                            : path + "/" + file.getName();

            items.add(new FileItem(
                    file.getName(),
                    relativePath,
                    file.isDirectory()
            ));
        }

        model.addAttribute("files", items);
        model.addAttribute("currentPath", path);

        return "file-explorer";
    }
}
