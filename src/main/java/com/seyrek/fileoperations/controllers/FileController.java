package com.seyrek.fileoperations.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/files")
public class FileController {
    @GetMapping
    public ResponseEntity<String> getAllFiles() {
        return new ResponseEntity<>("Files are here!", OK);
    }
}
