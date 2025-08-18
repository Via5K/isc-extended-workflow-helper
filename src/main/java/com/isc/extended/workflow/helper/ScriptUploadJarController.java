package com.isc.extended.workflow.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload-jar")
public class ScriptUploadJarController {

    @Value("${app.upload.dir:uploaded-jars}")  // fallback if not set
    private String uploadDir;

    @PostMapping
    public ResponseEntity<Map<String, Object>> uploadJar(@RequestParam("file") MultipartFile file) throws IOException {
        Map<String, Object> response = new HashMap<>();

        if (file.isEmpty()) {
            response.put("error", "File is empty");
            return ResponseEntity.badRequest().body(response);
        }
        if (!file.getOriginalFilename().endsWith(".jar")) {
            response.put("error", "Only .jar files are allowed");
            return ResponseEntity.badRequest().body(response);
        }

        // Resolve path relative to project root if not absolute
        File dir = new File(uploadDir);
        if (!dir.isAbsolute()) {
            dir = new File(System.getProperty("user.dir"), uploadDir);
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File destinationFile = new File(dir, file.getOriginalFilename());
        Path destinationPath = destinationFile.toPath();

        if (destinationFile.exists()) {
            response.put("status", "already_exists");
            response.put("path", destinationFile.getAbsolutePath());
            response.put("timestamp", Files.getLastModifiedTime(destinationPath).toString());
            return ResponseEntity.ok(response);
        }

        // Use NIO copy to bypass Tomcat temp behavior
        Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

        response.put("status", "uploaded");
        response.put("path", destinationFile.getAbsolutePath());
        response.put("timestamp", Instant.now().toString());

        return ResponseEntity.ok(response);
    }
}
