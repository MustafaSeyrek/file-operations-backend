package com.seyrek.fileoperations.repositories;

import com.seyrek.fileoperations.entities.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    @Transactional
    void deleteByCode(String code);
}
