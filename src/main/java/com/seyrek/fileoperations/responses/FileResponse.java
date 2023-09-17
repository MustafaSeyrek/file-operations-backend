package com.seyrek.fileoperations.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileResponse {
    Long id;

    String path;

    String name;

    Long size;

    String type;
}
