package com.seyrek.fileoperations.services;

import com.seyrek.fileoperations.entities.File;
import com.seyrek.fileoperations.repositories.FileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class FileService {
    private final FileRepository fileRepository;

    public File save(MultipartFile file) throws IOException{
        String name = StringUtils.cleanPath(file.getOriginalFilename());
        File f = File.builder().name(name).type(file.getContentType()).size(file.getSize())
                .data(file.getBytes()).path(file.getOriginalFilename()).build();
        return fileRepository.save(f);
    }

    public File getFileById(Long id){
        return fileRepository.findById(id).get();
    }

    public Stream<File> getAllFiles(){
        return fileRepository.findAll().stream();
    }

    public void deleteById(Long id){
        fileRepository.deleteById(id);
    }
}
