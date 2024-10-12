package com.carpetadigital.ecommerce.Repository;

import com.carpetadigital.ecommerce.entity.DocumentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentsRepository extends JpaRepository<DocumentsEntity, Long> {

    Optional<DocumentsEntity> findByTitle(String title);
    List<DocumentsEntity> findByBorradoLogicoFalse();
}
