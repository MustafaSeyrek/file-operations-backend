package com.seyrek.fileoperations.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileResponse {
    String path;

    String name;

    Long size;

    String type;
}