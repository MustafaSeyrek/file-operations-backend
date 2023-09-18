package com.seyrek.fileoperations.services;

import com.seyrek.fileoperations.entities.File;
import com.seyrek.fileoperations.repositories.FileRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final Path fileStorageLocation;

    @Autowired
    public FileService(FileRepository fileRepository, Environment env) {
        this.fileRepository = fileRepository;

        this.fileStorageLocation = Paths.get(env.getProperty("app.file.upload-dir", "./uploads/files"))
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public File save(MultipartFile file) throws IOException {
        String name = StringUtils.cleanPath(file.getOriginalFilename());
        String code = RandomStringUtils.randomAlphanumeric(8);
        Path targetLocation = this.fileStorageLocation.resolve(code + "-" + name);
        File f = File.builder().name(name).path("/uploads/files/" + code + "-" + name).type(file.getContentType()).size(file.getSize())
                .code(code).build();
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        return fileRepository.save(f);

    }

    public Stream<File> getAllFiles() {
        return fileRepository.findAll().stream();
    }

    public Resource getFileAsResource(String code) throws IOException {
        Path dirPath = this.fileStorageLocation;
        AtomicReference<Path> foundFile = new AtomicReference<>();
        Files.list(dirPath).forEach(file -> {
            if (file.getFileName().toString().startsWith(code)) {
                foundFile.set(file);
                return;
            }
        });
        if (foundFile.get() != null) {
            return new UrlResource(foundFile.get().toUri());
        }
        return null;
    }

    public void deleteByCode(String code) {
        fileRepository.deleteByCode(code);
    }

}
