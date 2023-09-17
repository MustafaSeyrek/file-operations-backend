package com.seyrek.fileoperations.controllers;

import com.seyrek.fileoperations.entities.File;
import com.seyrek.fileoperations.responses.FileResponse;
import com.seyrek.fileoperations.services.FileService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/files")
@AllArgsConstructor
public class FileController {
    private final FileService fileService;

    @GetMapping
    public ResponseEntity<List<FileResponse>> getAllFiles() {
        List<FileResponse> files = fileService.getAllFiles().map(db -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files/")
                    .path(String.valueOf(db.getId()))
                    .toUriString();
            return new FileResponse(
                    db.getId(),
                    fileDownloadUri,
                    db.getName(),
                    db.getSize(),
                    db.getType()
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

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFileById(@PathVariable Long id) {
        File file = fileService.getFileById(id);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(file.getData());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        fileService.deleteById(id);
        return new ResponseEntity<>(OK);
    }
}
