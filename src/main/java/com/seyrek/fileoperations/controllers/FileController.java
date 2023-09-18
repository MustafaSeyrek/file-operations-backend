package com.seyrek.fileoperations.controllers;

import com.seyrek.fileoperations.responses.FileResponse;
import com.seyrek.fileoperations.services.FileService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/files")
@AllArgsConstructor
public class FileController {
    private final FileService fileService;

    @GetMapping
    public ResponseEntity<List<FileResponse>> getAllFiles() {
        List<FileResponse> files = fileService.getAllFiles().map(db -> {

            return new FileResponse(
                    db.getId(),
                    db.getCode(),
                    db.getName(),
                    db.getSize(),
                    db.getType(),
                    db.getPath()
            );
        }).collect(Collectors.toList());
        return ResponseEntity.status(OK).body(files);
    }

    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String extensions = ".png,.jpeg,.jpg,.docx,.pdf,.xlsx";
            String name = file.getOriginalFilename();
            int lastIndex = name.lastIndexOf('.');
            String sub = name.substring(lastIndex, name.length());
            if (!extensions.contains(sub.toLowerCase())) {
                throw new IOException("This file type is not allowed!");
            }
            fileService.save(file);
            return new ResponseEntity<>("File uploaded successfully!", OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/download/{code}")
    public ResponseEntity<?> getFileByCode(@PathVariable String code) {
        Resource resource = null;
        try {
            resource = fileService.getFileAsResource(code);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
        if (resource == null) {
            return new ResponseEntity<>("File not found!", NOT_FOUND);
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteByCode(@PathVariable String code) throws IOException {
        Resource resource = null;
        try {
            resource = fileService.getFileAsResource(code);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
        if (resource == null) {
            return new ResponseEntity<>(NOT_FOUND);
        } else {
            fileService.deleteByCode(code);
            resource.getFile().delete();
            return new ResponseEntity<>(OK);
        }
    }

}
