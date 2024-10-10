package com.carpetadigital.ecommerce.Repository;

import com.carpetadigital.ecommerce.Entity.DocumentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentsRepository extends JpaRepository<DocumentsEntity, Long> {

    Optional<DocumentsEntity> findByTitle(String title);
}
