package com.carpetadigital.ecommerce.Repository;

import com.carpetadigital.ecommerce.Entity.DocumentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentsRepository extends JpaRepository<DocumentsEntity, Long> {
}
